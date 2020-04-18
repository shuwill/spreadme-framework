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

package org.spreadme.component.cache.hazelcast;

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.spreadme.component.cache.AbstractCacheClient;

/**
 * hazelcast cache client
 * @author shuwei.wang
 */
public class HazelcastCacheClient<K,V> extends AbstractCacheClient<K,V> {

	private static final String CACHE_NAME = HazelcastCacheClient.class.getName();

	private IMap<K,V> cache;

	public HazelcastCacheClient(HazelcastInstance instance) {
		this.cache = instance.getMap(CACHE_NAME);
	}

	@Override
	public void put(K key, V value) {
		this.cache.put(key, value);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		return this.cache.putIfAbsent(key, value);
	}

	@Override
	public void put(K key, V value, long timeout, TimeUnit timeUnit) {
		this.cache.put(key, value, timeout, timeUnit);
	}

	@Override
	public V get(K key) {
		return this.cache.get(key);
	}

	@Override
	public boolean remove(K key) {
		V value = get(key);
		return this.cache.remove(key,value);
	}

	@Override
	public void clear() {
		this.cache.clear();
	}
}
