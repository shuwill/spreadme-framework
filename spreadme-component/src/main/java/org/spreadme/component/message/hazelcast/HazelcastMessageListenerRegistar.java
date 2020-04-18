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

package org.spreadme.component.message.hazelcast;

import java.util.concurrent.ExecutorService;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import org.spreadme.commons.lang.Reflect;
import org.spreadme.commons.message.Message;
import org.spreadme.commons.message.MessageListener;
import org.spreadme.commons.message.MessageListenerRegistrar;

/**
 * hazelcast message listener registrar
 * @author shuwei.wang
 */
public class HazelcastMessageListenerRegistar implements MessageListenerRegistrar {

	private HazelcastInstance instance;

	private ExecutorService executorService;

	public HazelcastMessageListenerRegistar(HazelcastInstance instance, ExecutorService executorService) {
		this.instance = instance;
		this.executorService = executorService;
	}

	@Override
	public <M extends Message> void register(MessageListener<M> listener) {
		final String name = Reflect.getInterfaceGenericType(listener.getClass(), 0).getName();
		if (listener.isSubscribe()) {
			ITopic<M> topic = instance.getTopic(name);
			topic.addMessageListener(message -> listener.on(message.getMessageObject()));
		}
		else {
			IQueue<M> queue = instance.getQueue(name);
			queue.addItemListener(new ItemListener<M>() {
				@Override
				public void itemAdded(ItemEvent<M> item) {
					M message = queue.poll();
					if (message == null) {
						return;
					}
					executorService.execute(() -> listener.on(message));
				}

				@Override
				public void itemRemoved(ItemEvent<M> item) {

				}
			}, true);
		}
	}

	@Override
	public void destroy() {
		this.executorService.shutdown();
	}
}
