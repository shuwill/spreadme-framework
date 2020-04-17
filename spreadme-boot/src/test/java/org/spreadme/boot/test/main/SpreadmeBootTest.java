/*
 * Copyright [2020] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.boot.test.main;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.boot.SpringBootApplication;
import org.spreadme.boot.autoconfigure.AutoConfigurationPackages;
import org.spreadme.boot.test.config.HelloService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * spreadme boot test
 * @author shuwei.wang
 */
@SpringBootApplication
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpreadmeBootTest.class)
public class SpreadmeBootTest implements BeanFactoryAware {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private BeanFactory beanFactory;

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Before
	public void before() {
		List<String> autoPackages = AutoConfigurationPackages.get(this.beanFactory);
		logger.info("auto configuration packages: {}", autoPackages);
	}

	@Test
	public void testOnConditionBean() throws InterruptedException {
		HelloService helloService = applicationContext.getBean(HelloService.class);
		Assert.assertNotNull(helloService);
		final int size = taskExecutor.getCorePoolSize();
		CountDownLatch downLatch = new CountDownLatch(size);
		for(int i =0;i<size;i++){
			taskExecutor.execute(() ->{
				logger.info("{} is running.", Thread.currentThread().getName());
				try {
					TimeUnit.SECONDS.sleep(2);
				}
				catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
				downLatch.countDown();
			});
		}
		downLatch.await();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
