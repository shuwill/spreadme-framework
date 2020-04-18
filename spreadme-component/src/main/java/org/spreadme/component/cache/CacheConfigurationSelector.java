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

package org.spreadme.component.cache;

import java.util.Map;

import org.spreadme.component.annotation.EnableCache;
import org.spreadme.component.cache.hazelcast.HazelcastCacheConfiguration;
import org.spreadme.component.cache.redis.RedisCacheConfiguration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * cache configuration selector
 * @author shuwei.wang
 */
public class CacheConfigurationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		if (metadata.hasAnnotation(EnableCache.class.getName())) {
			Map<String, Object> value = metadata.getAnnotationAttributes(EnableCache.class.getName());
			if (value != null) {
				CacheType type = (CacheType) value.get("type");
				switch (type) {
					case hazelcast:
						return new String[] {HazelcastCacheConfiguration.class.getName()};
					case redis:
						return new String[] {RedisCacheConfiguration.class.getName()};
				}
			}
		}
		return new String[] {};
	}
}