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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.lang.Bools;
import org.spreadme.commons.lang.Reflect;
import org.spreadme.commons.message.MessageListener;
import org.spreadme.commons.message.MessagePublisher;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.commons.util.StringUtil;
import org.spreadme.component.job.task.Task;
import org.spreadme.component.job.task.TaskInfo;
import org.spreadme.component.job.task.TaskMessage;
import org.spreadme.component.lock.DistributeLock;
import org.spreadme.component.lock.RedisLock;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

public class ClusterScheduler implements Scheduler, MessageListener<TaskMessage> {

	private final Logger logger = LoggerFactory.getLogger(ClusterScheduler.class);

	private final static String ID = UUID.randomUUID().toString();

	private Map<String, TaskInfo> taskInfos = new ConcurrentHashMap<>();

	private DistributeLock lock;
	private ThreadPoolTaskScheduler taskScheduler;
	private MessagePublisher<TaskMessage> messagePublisher;

	public ClusterScheduler(DistributeLock lock,
			ThreadPoolTaskScheduler taskScheduler,
			MessagePublisher<TaskMessage> messagePublisher) {
		this.lock = lock;
		this.taskScheduler = taskScheduler;
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
			if (!this.lock.tryLock(tasksName)) {
				logger.debug("{} try lock fail.", tasksName);
				return;
			}
			try {
				logger.info("{}-{} is running.", tasksName, Thread.currentThread().getName());
				task.execute();
			}
			finally {
				this.lock.unlock(tasksName);
			}

		}, new CronTrigger(task.getCorn()));

		taskInfos.put(tasksName, new TaskInfo(task.getName(), task.getCorn(), false, scheduledFuture));

	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void cancel(String taskName) {
		TaskMessage message = new TaskMessage();
		message.setCancel(true);
		message.setTaskName(taskName);
		messagePublisher.publish(message);
	}

	@Override
	public void cron(String taskName, String cron) {
		TaskMessage message = new TaskMessage();
		message.setTaskName(taskName);
		message.setCron(cron);
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
		if(lock instanceof RedisLock){
			taskInfos.keySet().forEach(s -> lock.unlock(s));
		}
		logger.info("schedule {} end.", getId());
	}

	@Override
	public void on(TaskMessage message) {
		TaskInfo taskInfo = taskInfos.get(message.getTaskName());
		if (taskInfo != null) {
			ScheduledFuture<?> future = taskInfo.getFuture();
			CronTrigger trigger = Reflect.of(future).field("trigger").get();
			String cron = message.getCron();
			if (Bools.isTure(taskInfo.getCancel())) {
				future.cancel(true);
				logger.info("task {} is canceled.", taskInfo.getName());
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
