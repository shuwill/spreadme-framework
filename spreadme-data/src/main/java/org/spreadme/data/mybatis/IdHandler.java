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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.data.base.IdEntity;
import org.spreadme.data.base.TypeAliases;

/**
 * 实体类型默认类型转换器
 * @param <Entity>
 */
@MappedTypes(value = {TypeAliases.class})
@MappedJdbcTypes(value = {JdbcType.BIGINT})
public class IdHandler<Entity extends IdEntity> extends BaseTypeHandler<Entity> implements TypeAliases {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private Class<Entity> type;

	public IdHandler(Class<Entity> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Entity parameter, JdbcType jdbcType) throws SQLException {
		ps.setLong(i, parameter.getId());
	}

	@Override
	public Entity getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Long id = rs.getLong(columnName);
		return getEntityInstance(id);
	}

	@Override
	public Entity getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Long id = rs.getLong(columnIndex);
		return getEntityInstance(id);
	}

	@Override
	public Entity getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Long id = cs.getLong(columnIndex);
		return getEntityInstance(id);
	}

	protected Entity getEntityInstance(Long id) {
		try {
			Entity entity = type.newInstance();
			entity.setId(id);
			return entity;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
