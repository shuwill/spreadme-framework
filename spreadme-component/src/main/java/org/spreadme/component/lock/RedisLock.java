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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis distribute lock, only support a single redis
 * @author shuwei.wang
 */
public class RedisLock implements DistributeLock {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final long LOCK_EXPIRATION_INTERVAL_SECONDS = 30;
	private static final Integer SCHEDULER_CORE_SIZE = Runtime.getRuntime().availableProcessors() + 2;
	private static final Map<String, ScheduledFuture<?>> FUTURE_MAP = new ConcurrentHashMap<>();

	private RedisTemplate<String, Long> redisTemplate;
	// the shceduler task to use add expire time of key
	private ScheduledExecutorService taskScheduler;

	public RedisLock(RedisTemplate<String, Long> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.taskScheduler = Executors.newScheduledThreadPool(SCHEDULER_CORE_SIZE);
	}

	@Override
	public boolean tryLock(String key) {
		boolean successful = tryLock(key, LOCK_EXPIRATION_INTERVAL_SECONDS, TimeUnit.SECONDS);
		if (successful) {
			scheduleExpiration(key);
		}
		return successful;
	}

	private void scheduleExpiration(String key) {
		if (FUTURE_MAP.containsKey(key)) {
			logger.info("{} is already in schedule expiration", key);
			return;
		}

		ScheduledFuture<?> future = this.taskScheduler.scheduleAtFixedRate(() -> {

			logger.debug("Change {} expire time to {} senconds", key, LOCK_EXPIRATION_INTERVAL_SECONDS);
			redisTemplate.expire(key, LOCK_EXPIRATION_INTERVAL_SECONDS, TimeUnit.SECONDS);

		}, 0, LOCK_EXPIRATION_INTERVAL_SECONDS / 3, TimeUnit.SECONDS);

		if (FUTURE_MAP.putIfAbsent(key, future) != null) {
			future.cancel(true);
		}
	}

	@Override
	public boolean tryLock(String key, long timeout, TimeUnit timeunit) {
		final long threadId = getId();
		final long expire = timeunit.toSeconds(timeout);
		String result = redisTemplate.execute((RedisCallback<String>) connection -> {
			JedisCommands commands = (JedisCommands) connection.getNativeConnection();
			return commands.set(key, String.valueOf(threadId), "NX", "EX", expire);
		});
		return "OK".equals(result);
	}

	@Override
	public void unlock(String key) {
		ScheduledFuture<?> future = FUTURE_MAP.remove(key);
		if (future != null) {
			future.cancel(true);
		}
		final String script =
				"if redis.call('get',KEYS[1]) == ARGV[1] then " +
						"return redis.call('del',KEYS[1]) else return 0 end";
		final long threadId = getId();
		redisTemplate.execute((RedisCallback<Object>) connection -> {
			Jedis jedis = (Jedis) connection.getNativeConnection();
			return jedis.eval(script,
					Collections.singletonList(key),
					Collections.singletonList(String.valueOf(threadId)));
		});
	}

	private long getId() {
		return Thread.currentThread().getId();
	}

	@EventListener
	public void endLock(ContextClosedEvent event) {
		this.taskScheduler.shutdown();
		logger.info("shutdown expire scheduler task.");
	}
}
