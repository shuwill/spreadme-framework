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

package org.spreadme.component.message.redis;

import java.util.concurrent.TimeUnit;

import org.spreadme.commons.message.Message;
import org.spreadme.commons.message.MessageProducer;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * redis message producer
 * @author shuwei.wang
 */
public class RedisMessageProducer<M extends Message> implements MessageProducer<M> {


	private RedisTemplate<String, M> redisTemplate;
	private ThreadPoolTaskScheduler taskScheduler;

	public RedisMessageProducer(RedisTemplate<String, M> redisTemplate, ThreadPoolTaskScheduler taskScheduler) {
		this.redisTemplate = redisTemplate;
		this.taskScheduler = taskScheduler;
	}

	@Override
	public void produce(M message) {
		redisTemplate.opsForList().leftPush(message.getClass().getName(), message);
	}

	@Override
	public void produce(M message, long delaytime, TimeUnit timeUnit) {
		taskScheduler.scheduleWithFixedDelay(() -> this.produce(message), timeUnit.toMillis(delaytime));
	}
}
