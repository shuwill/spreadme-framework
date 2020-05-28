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

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.lang.Reflect;
import org.spreadme.commons.message.Message;
import org.spreadme.commons.message.MessageListener;
import org.spreadme.commons.message.MessageListenerRegistrar;
import org.spreadme.commons.util.StringUtil;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis message listener registrar
 * @author shuwei.wang
 */
public class RedisMessageListenerRegistar implements MessageListenerRegistrar {

	private final Logger logger = LoggerFactory.getLogger(RedisMessageListenerRegistar.class);

	private final Map<String, RedisMessageListenerContainer> containers = new ConcurrentHashMap<>();

	private RedisTemplate<String, Object> template;
	private RedisSerializer<Object> serializer;
	private ExecutorService executor;

	public RedisMessageListenerRegistar(RedisTemplate<String, Object> template, RedisSerializer<Object> serializer, ExecutorService executor) {
		this.template = template;
		this.serializer = serializer;
		this.executor = executor;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <M extends Message> void register(MessageListener<M> listener) {
		final String name = Reflect.getInterfaceGenericType(listener.getClass(), 0).getName();
		if (listener.isSubscribe()) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(Objects.requireNonNull(this.template.getConnectionFactory()));
			MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(listener, "on");
			listenerAdapter.setSerializer(serializer);
			listenerAdapter.afterPropertiesSet();
			container.addMessageListener(listenerAdapter, new PatternTopic(name));
			container.setTaskExecutor(executor);
			container.setSubscriptionExecutor(executor);
			container.start();

			containers.put(UUID.randomUUID().toString(), container);
		}
		else {
			new Thread(() -> {
				while (true){
					M message = (M) template.opsForList().rightPop(name);
					if(message != null){
						executor.execute(() -> listener.on(message));
					}
				}
			}, "redis message queue listener").start();
		}
	}

	@Override
	public void destroy() {
		for (RedisMessageListenerContainer container : this.containers.values()) {
			try {
				container.destroy();
			}
			catch (Exception e) {
				logger.error(StringUtil.stringifyException(e));
			}
		}
	}
}
