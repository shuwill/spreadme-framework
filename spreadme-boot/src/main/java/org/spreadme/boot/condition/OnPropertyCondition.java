/*
 * Copyright [2020] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.boot.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @author shuwei.wang
 */
public class OnPropertyCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		PropertyResolver resolver = context.getEnvironment();
		List<AnnotationAttributes> allAnnotationAttributes = getAnnotationAttributeses(metadata, ConditionalOnProperty.class);
		for (AnnotationAttributes annotationAttributes : allAnnotationAttributes) {
			String[] names = (String[]) annotationAttributes.get("name");
			Boolean matchIfMissing = (Boolean) annotationAttributes.get("matchIfMissing");
			for (String key : names) {
				if (!resolver.containsProperty(key)) {
					if (matchIfMissing != null && matchIfMissing) {
						continue;
					}
					return false;
				}
				String havingValue = (String) annotationAttributes.get("havingValue");
				if (StringUtils.hasText(havingValue)) {
					if (!havingValue.equals(resolver.getProperty(key))) {
						return false;
					}
				}
			}

		}
		return true;
	}

	private List<AnnotationAttributes> getAnnotationAttributeses(AnnotatedTypeMetadata metadata, Class<?> annotationType) {
		MultiValueMap<String, Object> multiValueMap = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
		List<Map<String, Object>> maps = new ArrayList<>();
		if (multiValueMap != null) {
			multiValueMap.forEach((key, value) -> {
				for (int i = 0; i < value.size(); i++) {
					Map<String, Object> map;
					if (i < maps.size()) {
						map = maps.get(i);
					}
					else {
						map = new HashMap<>();
						maps.add(map);
					}
					map.put(key, value.get(i));
				}
			});
		}
		List<AnnotationAttributes> annotationAttributes = new ArrayList<>(maps.size());
		for (Map<String, Object> map : maps) {
			annotationAttributes.add(AnnotationAttributes.fromMap(map));
		}
		return annotationAttributes;
	}
}
