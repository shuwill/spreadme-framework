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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.spreadme.data.mybatis.util.FieldReflectUtil;

@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class ResultTypePlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof DefaultResultSetHandler) {
			DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();

			Object[] args = invocation.getArgs();
			Statement stmt = (Statement) args[0];

			MappedStatement mappedStatement = (MappedStatement) FieldReflectUtil.getFieldValue(resultSetHandler, "mappedStatement");

			List<ResultMap> resultMaps = mappedStatement.getResultMaps();

			if (resultMaps != null && !resultMaps.isEmpty()) {
				ResultMap resultMap = resultMaps.get(0);

				if (resultMap.getResultMappings() == null || resultMap.getResultMappings().isEmpty()) {
					if (resultMap.getType().isAnnotationPresent(Entity.class) || resultMap.getType().isAnnotationPresent(Table.class)) {

						ResultMapSwapper swapper = ResultMapSwapperHolder.getSwapper(mappedStatement.getConfiguration());
						ResultMap newResultMap = swapper.reloadResultMap(mappedStatement.getResource(), resultMap.getId(), resultMap.getType());

						List<ResultMap> newResultMaps = new ArrayList<>();
						newResultMaps.add(newResultMap);

						// modify the resultMaps
						FieldReflectUtil.setFieldValue(mappedStatement, "resultMaps", newResultMaps);
						FieldReflectUtil.setFieldValue(resultSetHandler, "mappedStatement", mappedStatement);
					}
				}
			}

			return resultSetHandler.handleResultSets(stmt);

		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * Use static inner classes ensure thread safety
	 */
	private static class ResultMapSwapperHolder {

		private static Map<String, ResultMapSwapper> swappers = new HashMap<>();

		static ResultMapSwapper getSwapper(Configuration configuration) {
			String id = configuration.getEnvironment().getId();
			if (!swappers.containsKey(id)) {
				swappers.put(id, new ResultMapSwapper(configuration));
			}
			return swappers.get(id);
		}
	}

}
