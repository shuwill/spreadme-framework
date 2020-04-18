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

package org.spreadme.component.cache.redis;

import java.util.concurrent.TimeUnit;

import org.spreadme.component.cache.AbstractCacheClient;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis cache client
 * @author shuwei.wang
 */
public class RedisCacheClient<K,V> extends AbstractCacheClient<K,V> {

	private RedisTemplate<K, V> redisTemplate;

	public RedisCacheClient(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void put(K key, V value) {
		this.redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		this.redisTemplate.opsForValue().setIfAbsent(key, value);
		return value;
	}

	@Override
	public void put(K key, V value, long timeout, TimeUnit timeUnit) {
		this.redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
	}

	@Override
	public V get(K key) {
		return this.redisTemplate.opsForValue().get(key);
	}

	@Override
	public boolean remove(K key) {
		Boolean success = this.redisTemplate.delete(key);
		return success != null && success;
	}

	@Override
	public void clear() {

	}
}
