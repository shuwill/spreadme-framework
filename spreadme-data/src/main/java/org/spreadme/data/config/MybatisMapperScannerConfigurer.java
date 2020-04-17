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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.data.mybatis.mapper.Mapper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

public class MybatisMapperScannerConfigurer implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {

	private static Logger logger = LoggerFactory.getLogger(MybatisMapperScannerConfigurer.class);

	private ResourceLoader resourceLoader;

	private Environment environment;

	private BeanFactory beanFactory;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		MybatisProperties properties = beanFactory.getBean(MybatisProperties.class);
		MybatisMapperScanner scanner = new MybatisMapperScanner(registry);
		scanner.setEnvironment(environment);
		scanner.setResourceLoader(resourceLoader);
		scanner.setMarkerInterface(Mapper.class);
		scanner.setSqlSessionTemplateBeanName("sqlSessionTemplate");
		scanner.registerFilters();
		String[] basePackages = properties.getBasePackages();
		try {
			logger.info("MyBatis auto-configuration packages {}", CollectionUtil.toList(basePackages));
			scanner.doScan(basePackages);
		}
		catch (IllegalStateException ex) {
			logger.error("Could not determine auto-configuration " + "package, automatic data scanning disabled. {}", ex.getMessage());
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}