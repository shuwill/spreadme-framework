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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.spreadme.data.mybatis.util.FieldReflectUtil;
import org.spreadme.data.mybatis.util.PersistentUtil;

/**
 * ResultMap替换处理类
 */
public class ResultMapSwapper {

	private Configuration configuration;

	/**
	 * Result Maps collection,key : id
	 */
	private ConcurrentHashMap<String, ResultMap> resultMaps = new ConcurrentHashMap<>();

	public ResultMapSwapper(Configuration configuration) {
		this.configuration = configuration;
	}

	public ResultMap reloadResultMap(String resource, String id, Class<?> type) {
		if (!resultMaps.containsKey(id)) {
			resultMaps.put(id, resolveResultMap(resource, id, type));
		}

		return resultMaps.get(id);
	}

	public void registerResultMap(ResultMap resultMap) {
		configuration.addResultMap(resultMap);
	}

	public ResultMap resolveResultMap(String resource, String id, Class<?> type) {
		List<ResultMapping> resultMappings = resolveResultMappings(resource, id, type);
		return new ResultMap.Builder(configuration, id, type, resultMappings).build();
	}

	/**
	 * 解析ResultMapping
	 */
	private List<ResultMapping> resolveResultMappings(String resource, String id, Class<?> type) {
		List<ResultMapping> resultMappings = new ArrayList<>();

		MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, resource);

		List<Field> fields = PersistentUtil.getPersistentFields(type);

		for (Field field : fields) {
			// java field name
			String property = field.getName();
			// sql column name
			String column = PersistentUtil.getColumnName(field, configuration.isMapUnderscoreToCamelCase());
			Class<?> javaType = field.getType();

			// resultMap is not need jdbcType
			String nestedResultMap = null;

			// OneToOne or OneToMany
			if (PersistentUtil.isAssociationField(field)) {
				// mappedBy
				column = PersistentUtil.getMappedName(field);

				if (field.isAnnotationPresent(OneToOne.class)) {
					nestedResultMap = id + "_association[" + javaType.getSimpleName() + "]";
					registerResultMap(resolveResultMap(resource, nestedResultMap, javaType));
				}

				if (field.isAnnotationPresent(OneToMany.class)) {
					// create resultMap with GenericType
					Class<?> actualType = FieldReflectUtil.getGenericType(field);
					nestedResultMap = id + "collection[" + actualType.getSimpleName() + "]";
					registerResultMap(resolveResultMap(resource, nestedResultMap, actualType));
				}
			}

			// if primaryKey,then flags.add(ResultFlag.ID);
			List<ResultFlag> flags = new ArrayList<>();
			if (field.isAnnotationPresent(Id.class)) {
				flags.add(ResultFlag.ID);
			}
			// typeHandler
			// 不处理TypeHandler，由Mybatis默认处理
			// Class<? extends TypeHandler<?>> typeHandlerClass = ColumnMetaResolver.resolveTypeHandler(field);

			ResultMapping resultMapping = assistant.buildResultMapping(type, property, column, javaType, null,
					null, nestedResultMap, null, null, null, flags, null,
					null, false);
			resultMappings.add(resultMapping);
		}
		return resultMappings;
	}

}
