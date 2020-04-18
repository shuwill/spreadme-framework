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

package org.spreadme.component.job.lock;

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.util.StringUtil;

/**
 * Hazelcast TaskLock
 * @author shuwei.wang
 */
public class HazelcastTaskLock implements TaskLock {

	private final Logger logger = LoggerFactory.getLogger(HazelcastTaskLock.class);

	private HazelcastInstance instance;

	public HazelcastTaskLock(HazelcastInstance instance) {
		this.instance = instance;
	}

	@Override
	public boolean lock(String key, Object value, long timeout, TimeUnit timeunit) {
		IMap<String, Object> tasks = this.instance.getMap(this.getClass().getName());
		try {
			if (tasks.tryLock(key, 0, timeunit, timeout, timeunit)) {
				return true;
			}
		}
		catch (InterruptedException e) {
			logger.error(StringUtil.stringifyException(e));
		}
		return false;
	}

	@Override
	public void unlock(String key) {

	}
}
