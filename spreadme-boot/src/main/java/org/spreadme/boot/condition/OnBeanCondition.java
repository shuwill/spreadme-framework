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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spreadme.boot.autoconfigure.AutoConfigurationPackages;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

/**
 * OnBeanCondition
 * @author shuwei.wang
 */
public class OnBeanCondition implements Condition {

	private static final Log logger = LogFactory.getLog(AutoConfigurationPackages.class);

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

		if (metadata.isAnnotated(ConditionalOnBean.class.getName())) {
			List<String> types = this.collect(metadata, ConditionalOnBean.class);
			for (String type : types) {
				Collection<String> beanNames = this.getBeanNamesForType(beanFactory, type, context.getClassLoader());
				if (beanNames.isEmpty()) {
					logger.info("Bean " + type + " did not find.");
					return false;
				}
			}
		}

		if (metadata.isAnnotated(ConditionalOnMissingBean.class.getName())) {
			List<String> types = this.collect(metadata, ConditionalOnMissingBean.class);
			for (String type : types) {
				Collection<String> beanNames = this.getBeanNamesForType(beanFactory, type, context.getClassLoader());
				if (!beanNames.isEmpty()) {
					logger.info("Bean " + type + " find.");
					return false;
				}
			}
		}
		return true;
	}

	private Collection<String> getBeanNamesForType(ListableBeanFactory beanFactory, String type, ClassLoader classLoader) {
		Set<String> result = new LinkedHashSet<>();
		try {
			collectBeanNamesForType(result, beanFactory, ClassUtils.forName(type, classLoader));
		}
		catch (ClassNotFoundException e) {
			return Collections.emptySet();
		}
		return result;
	}

	private void collectBeanNamesForType(Set<String> result, ListableBeanFactory beanFactory, Class<?> type) {
		String[] beanNames = beanFactory.getBeanNamesForType(type);
		if (beanNames != null && beanNames.length > 0) {
			result.addAll(Arrays.asList(beanNames));
		}
		if (beanFactory instanceof HierarchicalBeanFactory) {
			BeanFactory parent = ((HierarchicalBeanFactory) beanFactory).getParentBeanFactory();
			if (parent instanceof ListableBeanFactory) {
				collectBeanNamesForType(result, (ListableBeanFactory) parent, type);
			}
		}
	}

	private List<String> collect(AnnotatedTypeMetadata metadata, Class<?> annotationType) {
		MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
		List<String> destination = new ArrayList<>();
		if (attributes != null) {
			List<?> values = attributes.get("value");
			if (values != null) {
				for (Object value : values) {
					if (value instanceof String[]) {
						Collections.addAll(destination, (String[]) value);
					}
					else {
						destination.add((String) value);
					}
				}
			}
		}
		return destination;
	}
}
