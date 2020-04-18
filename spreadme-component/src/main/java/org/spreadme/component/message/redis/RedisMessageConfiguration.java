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

import org.spreadme.commons.message.Message;
import org.spreadme.commons.message.MessageProducer;
import org.spreadme.commons.message.MessagePublisher;
import org.spreadme.component.annotation.EnableRedis;
import org.spreadme.component.message.MessageListenerRegistrarPostProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * RedisMessageConfiguration
 * @author shuwei.wang
 */
@Configuration
@EnableRedis
public class RedisMessageConfiguration {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedisSerializer<Object> serializer;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Bean
	public RedisMessageListenerRegistar messageListenerRegistar() {
		return new RedisMessageListenerRegistar(redisTemplate, serializer, taskExecutor.getThreadPoolExecutor());
	}

	@Bean
	public <M extends Message> MessageProducer<M> messageProducer(RedisTemplate<String, M> template) {
		return new RedisMessageProducer<>(template);
	}

	@Bean
	public <M extends Message> MessagePublisher<M> messagePublisher(RedisTemplate<String, M> template) {
		return new RedisMessagePublisher<>(template);
	}

	@Bean
	public MessageListenerRegistrarPostProcessor listenerRegistrarPostProcessor() {
		return new MessageListenerRegistrarPostProcessor();
	}
}
