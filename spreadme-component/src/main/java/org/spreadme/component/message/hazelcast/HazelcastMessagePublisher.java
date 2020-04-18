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

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.spreadme.commons.message.Message;
import org.spreadme.commons.message.MessagePublisher;

/**
 * hazelcast message publisher
 * @author shuwei.wang
 */
public class HazelcastMessagePublisher<M extends Message> implements MessagePublisher<M> {

	private HazelcastInstance instance;

	public HazelcastMessagePublisher(HazelcastInstance instance) {
		this.instance = instance;
	}

	@Override
	public void publish(M message) {
		final String name = message.getClass().getName();
		ITopic<M> topic = instance.getTopic(name);
		topic.publish(message);
	}
}
