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

package org.spreadme.data.mybatis;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.resolve.DefaultEntityResolve;

public class CustomEntityResolve extends DefaultEntityResolve {

	/**
	 * 处理 GeneratedValue 注解
	 */
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected void processGeneratedValue(EntityTable entityTable, EntityColumn entityColumn, GeneratedValue generatedValue) {
		if ("JDBC".equals(generatedValue.generator())) {
			entityColumn.setIdentity(true);
			entityColumn.setGenerator("JDBC");
			entityTable.setKeyProperties(entityColumn.getProperty());
			entityTable.setKeyColumns(entityColumn.getColumn());
		}
		//允许通过generator来设置获取id的sql,例如mysql=CALL IDENTITY(),hsqldb=SELECT SCOPE_IDENTITY()
		//允许通过拦截器参数设置公共的generator
		else if (generatedValue.strategy() == GenerationType.IDENTITY) {
			//mysql的自动增长
			entityColumn.setIdentity(true);
			if (!"".equals(generatedValue.generator())) {
				String generator;
				IdentityDialect identityDialect = IdentityDialect.getDatabaseDialect(generatedValue.generator());
				if (identityDialect != null) {
					generator = identityDialect.getIdentityRetrievalStatement();
				}
				else {
					generator = generatedValue.generator();
				}
				entityColumn.setGenerator(generator);
			}
		}
		else if (generatedValue.strategy() == GenerationType.AUTO) {
			entityColumn.setIdentity(false);
			try {
				Class genIdClass = Class.forName(generatedValue.generator());
				entityColumn.setGenIdClass(genIdClass);
			}
			catch (ClassNotFoundException e) {
				throw new MapperException(entityTable.getEntityClass().getCanonicalName() + e.getMessage());
			}
		}
		else {
			throw new MapperException(entityColumn.getProperty()
					+ "该字段@GeneratedValue配置只允许以下几种形式:" +
					"\n1.useGeneratedKeys的@GeneratedValue(code=\\\"JDBC\\\")" +
					"\n2.类似mysql数据库的@GeneratedValue(strategy=GenerationType.IDENTITY[,code=\"Mysql\"])" +
					"\n3.实现GenId的@GeneratedValue(strategy=GenerationType.AUTO[,code=\"GenId\"])");
		}

	}
}
