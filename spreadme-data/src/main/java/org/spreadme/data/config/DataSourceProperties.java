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

package org.spreadme.data.config;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {

	public static final String PREFIX = "spring.datasource";

	//JDBC url of the database.
	private String url;

	//Login user of the database.
	private String username;

	//Login password of the database.
	private String password;

	//the maximum number of connections in the pool
	private int maxPoolSize = 2;

	//the minimum number of idle connections in the pool to maintain
	private int minIdle = 1;

	private long connectionTimeout = SECONDS.toMillis(60);

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public String getDatabaseId() {
		return url.split(":")[1].toUpperCase(Locale.ENGLISH);
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

}