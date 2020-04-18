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

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis Task Lock
 * @author shuwei.wang
 */
public class RedisTaskLock implements TaskLock {

	private RedisTemplate<String, Object> redisTemplate;

	public RedisTaskLock(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean lock(String key, Object value, long timeout, TimeUnit timeunit) {
		if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
			redisTemplate.expire(key, timeout, timeunit);
			return true;
		}
		long expireTime = redisTemplate.getExpire(key);
		if (expireTime < 0 || expireTime > timeout) {
			redisTemplate.delete(key);
			lock(key, value, timeout, timeunit);
		}
		return false;
	}

	@Override
	public void unlock(String key) {
	}
}
