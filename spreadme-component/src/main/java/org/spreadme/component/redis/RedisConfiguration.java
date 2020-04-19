/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.component.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.spreadme.boot.condition.ConditionalOnMissingBean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis configuration
 * @author shuwei.wang
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@Import(JedisConnectionConfiguration.class)
@ConditionalOnMissingBean(RedisTemplate.class)
public class RedisConfiguration {

	@Bean
	public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer serializer) {
		RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setDefaultSerializer(serializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public RedisSerializer redisSerializer(ObjectMapper objectMapper) {
		Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		jsonRedisSerializer.setObjectMapper(objectMapper);
		return jsonRedisSerializer;
	}
}
