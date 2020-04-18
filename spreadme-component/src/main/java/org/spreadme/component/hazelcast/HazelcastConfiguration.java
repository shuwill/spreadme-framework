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

package org.spreadme.component.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import org.spreadme.boot.condition.ConditionalOnMissingBean;
import org.spreadme.commons.util.StringUtil;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hazelcast Configuration
 * @author shuwei.wang
 */
@Configuration
@ConditionalOnMissingBean(HazelcastInstanceFactory.class)
@EnableConfigurationProperties(HazelcastProperties.class)
public class HazelcastConfiguration {

	@Bean
	public Config config(HazelcastProperties properties) {
		Config config = new Config(properties.getInstanceName());
		NetworkConfig networkConfig = new NetworkConfig();
		if (properties.getPort() != null) {
			networkConfig.setPort(properties.getPort());
		}
		if (StringUtil.isNotBlank(properties.getIp())) {
			networkConfig.setPublicAddress(properties.getIp());
		}
		config.setNetworkConfig(networkConfig);
		return config;
	}

	@Bean
	public HazelcastInstanceFactory hazelcastInstanceFactory(Config config) {
		return new HazelcastInstanceFactory(config);
	}
}
