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

package org.spreadme.component.test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spreadme.boot.SpringBootApplication;
import org.spreadme.commons.cache.CacheClient;
import org.spreadme.commons.message.MessagePublisher;
import org.spreadme.commons.system.sampler.ProcessorSampler;
import org.spreadme.commons.util.StringUtil;
import org.spreadme.component.annotation.EnableCache;
import org.spreadme.component.annotation.EnableMessage;
import org.spreadme.component.cache.CacheType;
import org.spreadme.component.message.MessageType;
import org.spreadme.component.test.entity.User;
import org.spreadme.component.test.message.UserMessage;
import org.spreadme.component.test.task.TestTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shuwei.wang
 */
@SpringBootApplication
@EnableCache(type = CacheType.redis)
@EnableMessage(type = MessageType.redis)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisTest.class)
public class RedisTest {


	@Autowired
	private CacheClient<String, User> cacheClient;
	@Autowired
	private MessagePublisher<UserMessage> publisher;
	@Autowired
	private TestTask task;

	@Test
	public void testCacheClient() throws InterruptedException {
		doTest();
	}

	private void doTest() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(5);

		User user = new User(StringUtil.randomString(4), 20, new Date());
		user.setSampler(new ProcessorSampler());
		cacheClient.put(user.getName(), user, 100, TimeUnit.SECONDS);
		Assert.assertNotNull(cacheClient.get(user.getName()));

		UserMessage message = new UserMessage();
		message.setUser(cacheClient.get(user.getName()));
		publisher.publish(message);
		task.setDownLatch(countDownLatch);

		countDownLatch.await();
	}
}
