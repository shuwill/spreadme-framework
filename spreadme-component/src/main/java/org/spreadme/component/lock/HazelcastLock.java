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

package org.spreadme.component.lock;

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.util.StringUtil;

/**
 * hazelcast distribute lock
 * @author shuwei.wang
 */
public class HazelcastLock implements DistributeLock {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private HazelcastInstance instance;
	private IMap<String, Object> locker;

	public HazelcastLock(HazelcastInstance instance) {
		this.instance = instance;
		this.locker = instance.getMap(getClass().getName());
	}

	@Override
	public boolean tryLock(String key) {
		return this.locker.tryLock(key);
	}

	@Override
	public boolean tryLock(String key, long timeout, TimeUnit timeunit) {
		try {
			return this.locker.tryLock(key, timeout, timeunit);
		}
		catch (InterruptedException e) {
			logger.error(StringUtil.stringifyException(e));
			return false;
		}
	}

	@Override
	public void unlock(String key) {
		if(instance.getLifecycleService().isRunning()){
			this.locker.unlock(key);
		}
	}
}
