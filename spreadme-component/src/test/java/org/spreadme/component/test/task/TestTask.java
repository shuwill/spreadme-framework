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

package org.spreadme.component.test.task;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.component.job.task.Task;

import org.springframework.stereotype.Component;

/**
 * @author shuwei.wang
 */
@Component
public class TestTask implements Task {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private CountDownLatch countDownLatch;

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public String getCorn() {
		return "*/2 * * * * ?";
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void execute() {
		countDownLatch.countDown();
		int i = 1/0;
	}

	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
}
