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
import org.spreadme.commons.message.MessagePublisher;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis message publisher
 * @author shuwei.wang
 */
public class RedisMessagePublisher<M extends Message> implements MessagePublisher<M> {

	private RedisTemplate redisTemplate;

	public RedisMessagePublisher(RedisTemplate<String, M> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void publish(M message) {
		final String name = message.getClass().getName();
		redisTemplate.convertAndSend(name, message);
	}
}
