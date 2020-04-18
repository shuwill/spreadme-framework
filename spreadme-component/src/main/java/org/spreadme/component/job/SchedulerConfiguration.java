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
import org.spreadme.component.hazelcast.HazelcastInstanceFactory;
import org.spreadme.component.job.lock.HazelcastTaskLock;
import org.spreadme.component.job.lock.RedisTaskLock;
import org.spreadme.component.job.lock.TaskLock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Scheduler AutoConfiguration
 * @author shuwei.wang
 */
@Configuration
public class SchedulerConfiguration {

	private final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

	@Bean
	@ConditionalOnBean(HazelcastInstanceFactory.class)
	public TaskLock hazelcastTaskLock(HazelcastInstance instance) {
		return new HazelcastTaskLock(instance);
	}

	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	public TaskLock redisTaskLock(RedisTemplate<String, Object> redisTemplate) {
		return new RedisTaskLock(redisTemplate);
	}

	@Bean
	@ConditionalOnBean({TaskLock.class, CacheClient.class, MessagePublisher.class})
	public Scheduler scheduler(TaskLock lock) {
		logger.info("use the tasklock {}", lock.getClass().getName());
		return new ClusterScheduler(lock);
	}

}
