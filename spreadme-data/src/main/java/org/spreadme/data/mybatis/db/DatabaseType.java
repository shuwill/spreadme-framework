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

package org.spreadme.data.mybatis.db;

import java.util.Locale;

/**
 * 支持的数据库类型
 */
public enum DatabaseType {

	// MySQL
	MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://{}:{}/{}?serverTimezone=GMT%2B8&useUnicode=true&useSSL=false&characterEncoding=utf-8"),

	// Oracle
	ORACLE("Oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@//{}:{}/{}"),

	// SQL Server
	SQLSERVER("Microsoft SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://{}:{};DatabaseName={}");

	private final String productName;

	private final String driverClassName;

	private final String jdbcUrl;

	DatabaseType(String productName, String driverClassName, String jdbcUrl) {
		this.productName = productName;
		this.driverClassName = driverClassName;
		this.jdbcUrl = jdbcUrl;
	}

	public String databaseId() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public String productName() {
		return productName;
	}

	public String driverClassName() {
		return driverClassName;
	}

	public String jdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * 根据databaseId 获取对应的Database对象
	 * @param databaseId databaseId
	 * @return 返回对应的Database，如果没有匹配则返回null
	 */
	public static DatabaseType fromDatabaseId(String databaseId) {
		for (DatabaseType db : DatabaseType.values()) {
			if (db.databaseId().equals(databaseId)) {
				return db;
			}
		}
		return null;
	}
}
