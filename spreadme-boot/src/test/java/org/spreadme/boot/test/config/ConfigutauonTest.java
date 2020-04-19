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

package org.spreadme.boot.test.config;

import org.spreadme.boot.condition.ConditionalOnMissingBean;
import org.spreadme.boot.condition.ConditionalOnProperty;
import org.spreadme.boot.config.PropertySourceLocator;
import org.spreadme.boot.test.main.CustomPropertySourceLocator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuwei.wang
 */
@Configuration
public class ConfigutauonTest {

	@Bean
	@ConditionalOnMissingBean(ConditionService.class)
	@ConditionalOnProperty(name = {"spreadme.test", "spreadme.test1"}, havingValue = "test", matchIfMissing = true)
	public HelloService helloService() {
		return new HelloServiceImpl();
	}

	@Bean
	public PropertySourceLocator propertySourceLocator(){
		return new CustomPropertySourceLocator();
	}
}
