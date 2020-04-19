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

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.message.Message;
import org.spreadme.commons.message.MessageProducer;
import org.spreadme.commons.util.StringUtil;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * hazelcast message producer
 * @author shuwei.wang
 */
public class HazelcastMessageProducer<M extends Message> implements MessageProducer<M> {

	private final Logger logger = LoggerFactory.getLogger(HazelcastMessageProducer.class);

	private HazelcastInstance instance;
	private ThreadPoolTaskScheduler taskScheduler;

	public HazelcastMessageProducer(HazelcastInstance instance, ThreadPoolTaskScheduler taskScheduler) {
		this.instance = instance;
		this.taskScheduler = taskScheduler;
	}

	@Override
	public void produce(M message) {
		final String name = message.getClass().getName();
		IQueue<M> queue = instance.getQueue(name);
		try {
			queue.put(message);
		}
		catch (InterruptedException e) {
			logger.error(StringUtil.stringifyException(e));
		}
	}

	@Override
	public void produce(M message, long delaytime, TimeUnit timeUnit) {
		taskScheduler.scheduleWithFixedDelay(() -> this.produce(message),
				timeUnit.toMillis(delaytime));
	}
}
