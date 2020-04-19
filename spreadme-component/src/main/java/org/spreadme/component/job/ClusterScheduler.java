/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.component.job;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.cache.CacheClient;
import org.spreadme.commons.lang.Bools;
import org.spreadme.commons.lang.Reflect;
import org.spreadme.commons.message.MessageListener;
import org.spreadme.commons.message.MessagePublisher;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.commons.util.StringUtil;
import org.spreadme.component.job.lock.TaskLock;
import org.spreadme.component.job.task.Task;
import org.spreadme.component.job.task.TaskInfo;
import org.spreadme.component.job.task.TaskMessage;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

public class ClusterScheduler implements Scheduler, MessageListener<TaskMessage> {

	private final Logger logger = LoggerFactory.getLogger(ClusterScheduler.class);

	private Map<String, TaskInfo> taskInfos = new ConcurrentHashMap<>();


	private TaskLock lock;
	private ThreadPoolTaskScheduler taskScheduler;
	private CacheClient<String, Set<TaskInfo>> cacheClient;
	private MessagePublisher<TaskMessage> messagePublisher;

	public ClusterScheduler(TaskLock lock, ThreadPoolTaskScheduler taskScheduler,
			CacheClient<String, Set<TaskInfo>> cacheClient,
			MessagePublisher<TaskMessage> messagePublisher) {
		this.lock = lock;
		this.taskScheduler = taskScheduler;
		this.cacheClient = cacheClient;
		this.messagePublisher = messagePublisher;
	}

	@Override
	public void schedule(Collection<Task> tasks) {
		if (CollectionUtil.isNotEmpty(tasks)) {
			this.initScheduler(tasks.size());
			// 过滤出已经激活的task
			List<Task> activeTasks = tasks.stream().filter(Task::isActive).collect(Collectors.toList());
			for (Task task : activeTasks) {
				this.start(task);
			}
		}
	}

	//初始化任务调度器
	private void initScheduler(int size) {
		this.taskScheduler.setPoolSize(size);
		this.taskScheduler.initialize();
	}

	private void start(Task task) {
		final String tasksName = task.getName();
		ScheduledFuture<?> scheduledFuture = this.taskScheduler.schedule(() -> {
			if (!this.lock.lock(tasksName, getId(), 2, TimeUnit.SECONDS)) {
				logger.debug("{} try lock fail.", tasksName);
				return;
			}
			try {
				logger.debug("{} task is running.", tasksName);
				task.execute();
			}
			finally {
				this.lock.unlock(tasksName);
			}

		}, new CronTrigger(task.getCorn()));

		TaskInfo taskInfo = new TaskInfo(tasksName, task.getCorn(), false, scheduledFuture);
		taskInfos.put(tasksName, taskInfo);

		Set<TaskInfo> taskInfoSet = cacheClient.get(getId());
		if (taskInfoSet == null) {
			taskInfoSet = new HashSet<>();
		}
		taskInfoSet.add(taskInfo);
		cacheClient.put(getId(), taskInfoSet);
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Set<TaskInfo> getAllTask() {
		return cacheClient.get(getId());
	}

	@Override
	public void edit(String taskName, boolean isCancel, String cron) {
		TaskMessage message = new TaskMessage();
		message.setInfo(new TaskInfo(taskName, cron, isCancel));
		messagePublisher.publish(message);
	}

	@EventListener
	public void startSchedule(ContextRefreshedEvent event) {
		Map<String, Task> tasks = event.getApplicationContext().getBeansOfType(Task.class);
		if (!tasks.isEmpty()) {
			logger.info("schedule {} start.", this.getClass().getName());
			this.schedule(tasks.values());
		}
	}

	@EventListener
	public void endSchedule(ContextClosedEvent event) {
		this.taskScheduler.shutdown();
		logger.info("schedule {} end.", getId());
	}

	@Override
	public void on(TaskMessage message) {
		TaskInfo taskInfo = message.getInfo();
		TaskInfo rawTaskInfo = taskInfos.get(taskInfo.getName());
		if (rawTaskInfo != null) {
			ScheduledFuture<?> future = rawTaskInfo.getFuture();
			CronTrigger trigger = Reflect.of(future).field("trigger").get();
			String cron = taskInfo.getCron();
			if (Bools.isTure(taskInfo.getCancel())) {
				future.cancel(true);
				logger.info("task {} is canceled.", rawTaskInfo.getName());
				return;
			}
			if (StringUtil.isNotBlank(cron) && trigger != null && !cron.equals(trigger.getExpression())) {
				CronSequenceGenerator sequenceGenerator = new CronSequenceGenerator(cron);
				Reflect.of(trigger).set("sequenceGenerator", sequenceGenerator);
				logger.info("change task cron {} to {}.", trigger.getExpression(), cron);
			}
		}
	}

	@Override
	public boolean isSubscribe() {
		return true;
	}
}
