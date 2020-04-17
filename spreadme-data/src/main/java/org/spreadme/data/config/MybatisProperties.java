/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.data.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.boot.autoconfigure.AutoConfigurationPackages;
import org.spreadme.commons.util.StringUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Data.
 */
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties implements BeanFactoryAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisProperties.class);

	public static final String PREFIX = "mybatis";

	public static final String TYPE_ALIASES = "spreadme.mybatis.typeAliases";

	private static final String CONFIG_LOCATION = "classpath:mybatis-config.xml";

	private BeanFactory beanFactory;

	private String[] basePackages;

	// Location of MyBatis xml config file.
	private String configLocation = CONFIG_LOCATION;

	// Locations of MyBatis data files.
	private String[] mapperLocations;

	// Packages to search type aliases. (Package delimiters are ",; \t\n")
	private String typeAliasesPackage;

	private Class<?>[] typeAliases = {};

	// Packages to search for type handlers. (Package delimiters are ",; \t\n")
	private String typeHandlersPackage;

	// Execution mode for {@link org.mybatis.spring.SqlSessionTemplate}.
	private ExecutorType executorType = ExecutorType.SIMPLE;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public String[] getBasePackages() {
		if (basePackages != null) {
			return basePackages;
		}
		try {
			List<String> autoPackages = AutoConfigurationPackages.get(beanFactory);
			return StringUtil.toStringArray(autoPackages);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	public void setBasePackages(String[] basePackages) {
		this.basePackages = basePackages;
	}

	public String getTypeAliasesPackage() {
		return typeAliasesPackage == null ? joinedPackages() : typeAliasesPackage;
	}

	public String getTypeHandlersPackage() {
		return typeHandlersPackage == null ? joinedPackages() : typeHandlersPackage;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String[] getMapperLocations() {
		return mapperLocations;
	}

	public void setMapperLocations(String[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public void setTypeAliasesPackage(String typeAliasesPackage) {
		this.typeAliasesPackage = typeAliasesPackage;
	}

	public void setTypeHandlersPackage(String typeHandlersPackage) {
		this.typeHandlersPackage = typeHandlersPackage;
	}

	public ExecutorType getExecutorType() {
		return executorType;
	}

	public void setExecutorType(ExecutorType executorType) {
		this.executorType = executorType;
	}

	public Class<?>[] getTypeAliases() {
		String settingAliases = System.getProperty(TYPE_ALIASES);
		if (settingAliases != null) {
			List<Class<?>> aliasesList = new ArrayList<>(Arrays.asList(typeAliases));
			String[] aliases = settingAliases.split(",");
			for (String aliase : aliases) {
				try {
					aliasesList.add(Class.forName(aliase));
				}
				catch (ClassNotFoundException ignored) {
				}
			}
			return aliasesList.toArray(new Class<?>[0]);
		}
		return typeAliases;
	}

	public void setTypeAliases(Class<?>[] typeAliases) {
		this.typeAliases = typeAliases;
	}

	private String joinedPackages() {
		StringBuilder builder = new StringBuilder();
		for (String pkg : getBasePackages()) {
			builder.append(",").append(pkg);
		}
		return builder.substring(1);
	}
}