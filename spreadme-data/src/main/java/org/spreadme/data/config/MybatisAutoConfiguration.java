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

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.spreadme.data.base.TypeAliases;
import org.spreadme.data.id.DefaultIdGenerator;
import org.spreadme.data.id.IdGenerator;
import org.spreadme.data.mybatis.db.DefaultDatabaseIdProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * {@link org.spreadme.boot.autoconfigure.EnableAutoConfiguration Auto-Configuration} for Mybatis. Contributes a
 * {@link SqlSessionFactory} and a {@link SqlSessionTemplate}.
 *
 * If {@link org.mybatis.spring.annotation.MapperScan} is used, or a configuration file is
 * specified as a property, those will be considered, otherwise this auto-configuration
 * will attempt to register mappers based on the interface definitions in or under the
 * root auto-configuration package.
 *
 * @author Eddú Meléndez
 * @author Josh Long
 */
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 40)
@Import(DataSourceAutoConfiguration.class)
@EnableTransactionManagement
public class MybatisAutoConfiguration {

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	public IdGenerator<Long> idGenerator() {
		return new DefaultIdGenerator();
	}

	@Bean
	public DatabaseIdProvider databaseIdProvider() {
		return new DefaultDatabaseIdProvider();
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, DatabaseIdProvider databaseIdProvider) {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		String configLocation = properties.getConfigLocation();
		factory.setConfigLocation(resourceLoader.getResource(configLocation));
		factory.setDatabaseIdProvider(databaseIdProvider);
		factory.setTypeAliasesSuperType(TypeAliases.class);
		factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
		factory.setTypeAliases(properties.getTypeAliases());
		return factory;
	}

	@Bean("sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		ExecutorType executorType = properties.getExecutorType();
		if (executorType != null) {
			return new SqlSessionTemplate(sqlSessionFactory, executorType);
		}
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up creating
	 * instances of {@link MapperFactoryBean}. If
	 * {@link org.mybatis.spring.annotation.MapperScan} is used then this
	 * auto-configuration is not needed. If it is _not_ used, however, then this will
	 * bring in a bean registrar and automatically register components based on the same
	 * component-scanning path as Spring Boot itself.
	 */
	@Configuration
	@EnableConfigurationProperties(MybatisProperties.class)
	@Import({MybatisMapperScannerConfigurer.class})
	public static class MapperScannerRegistrarNotFoundConfiguration {

	}

}