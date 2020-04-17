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

package org.spreadme.data.base;


import org.spreadme.data.mybatis.mapper.CrudMapper;
import org.spreadme.data.mybatis.mapper.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

public interface BaseDao<T extends IdEntity> extends CrudMapper<T, Long>, Mapper {

	default Example.Builder exampleOf(Class<T> clazz) {
		return new Example.Builder(clazz);
	}

	default WeekendSqls<T> criteria() {
		return WeekendSqls.custom();
	}

	/**
	 * update entity by primary key
	 *
	 * @param entity entity
	 * @return update successful
	 * @exception IllegalArgumentException IllegalArgumentException
	 */
	default boolean update(T entity) {
		if (entity == null || entity.getId() == null) {
			throw new IllegalArgumentException("entity is null or id of entity id null.");
		}
		return updateByPrimaryKey(entity) == 1;
	}

	/**
	 * delete entity by primary key
	 *
	 * @param id primary key
	 * @return delete successful
	 */
	default boolean delete(Long id) {
		return deleteByPrimaryKey(id) == 1;
	}

}
