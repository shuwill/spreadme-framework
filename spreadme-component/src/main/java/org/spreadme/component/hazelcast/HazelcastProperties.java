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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * hazelcast properties
 * @author shuwei.wang
 */
@ConfigurationProperties(HazelcastProperties.PREFIX)
public class HazelcastProperties {

	public static final String PREFIX = "spring.hazelcast";

	private String group = "dev";
	private String[] members = new String[] {"127.0.0.1"};
	private Integer connectTimeout = 60;
	private String centerConfigUrl;
	private String loggerType = "slf4j";

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String[] getMembers() {
		return members;
	}

	public void setMembers(String[] members) {
		this.members = members;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public String getCenterConfigUrl() {
		return centerConfigUrl;
	}

	public void setCenterConfigUrl(String centerConfigUrl) {
		this.centerConfigUrl = centerConfigUrl;
	}

	public String getLoggerType() {
		return loggerType;
	}

	public void setLoggerType(String loggerType) {
		this.loggerType = loggerType;
	}
}
