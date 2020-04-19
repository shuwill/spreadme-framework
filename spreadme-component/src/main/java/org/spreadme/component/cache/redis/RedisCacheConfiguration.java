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

import org.spreadme.cache.CacheClient;
import org.spreadme.component.annotation.EnableRedis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis cache cleint configuration
 * @author shuwei.wang
 */
@Configuration
@EnableRedis
public class RedisCacheConfiguration {

	@Bean
	public <K, V> CacheClient<K, V> redisCacheClient(RedisTemplate<K, V> redisTemplate) {
		return new RedisCacheClient<>(redisTemplate);
	}

}
