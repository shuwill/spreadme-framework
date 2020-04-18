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

package org.spreadme.component.message;

import java.util.Map;

import org.spreadme.component.annotation.EnableMessage;
import org.spreadme.component.message.hazelcast.HazelcastMessageConfiguration;
import org.spreadme.component.message.redis.RedisMessageConfiguration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * MessageConfigurationSelector
 * @author shuwei.wang
 */
public class MessageConfigurationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		if (metadata.hasAnnotation(EnableMessage.class.getName())) {
			Map<String, Object> value = metadata.getAnnotationAttributes(EnableMessage.class.getName());
			if (value != null) {
				MessageType type = (MessageType) value.get("type");
				switch (type) {
					case hazelcast:
						return new String[] {HazelcastMessageConfiguration.class.getName()};
					case redis:
						return new String[] {RedisMessageConfiguration.class.getName()};
				}
			}
		}
		return new String[] {};
	}
}
