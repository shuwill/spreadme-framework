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

import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.boot.condition.ConditionalOnBean;
import org.spreadme.commons.cache.CacheClient;
import org.spreadme.commons.message.MessagePublisher;
import org.spreadme.component.job.task.TaskMessage;
import org.spreadme.component.lock.DistributeLock;
import org.spreadme.component.lock.HazelcastLock;
import org.spreadme.component.lock.RedisLock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Scheduler AutoConfiguration
 * @author shuwei.wang
 */
@Configuration
public class SchedulerConfiguration {

	private final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

	@Bean
	@ConditionalOnBean(HazelcastInstance.class)
	public DistributeLock hazelcastTaskLock(HazelcastInstance instance) {
		return new HazelcastLock(instance);
	}

	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	public DistributeLock redisTaskLock(RedisTemplate<String, Long> redisTemplate) {
		return new RedisLock(redisTemplate);
	}

	@Bean
	@ConditionalOnBean({DistributeLock.class, CacheClient.class, MessagePublisher.class})
	public Scheduler scheduler(DistributeLock lock,
			MessagePublisher<TaskMessage> messagePublisher,
			ThreadPoolTaskScheduler taskScheduler) {

		logger.info("use the tasklock {}", lock.getClass().getName());
		return new ClusterScheduler(lock, taskScheduler, messagePublisher);
	}

}
