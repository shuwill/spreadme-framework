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

package org.spreadme.data.service;

import java.io.Serializable;
import java.util.Optional;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
 */
public interface CrudService<T, ID extends Serializable> {

	/**
	 * Insert a new entity
	 *
	 * @return entity
	 */
	T create(T entity);

	/**
	 * update a entity
	 *
	 * @param entity entity
	 */
	boolean update(T entity);

	/**
	 * find an entity by its id
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	Optional<T> findById(ID id);

	/**
	 * 根据实体类条件查询一条记录
	 * @param entity entity
	 * @return Optional
	 */
	Optional<T> findOne(T entity);

	/**
	 * 根据实体类条件查询集合
	 * @param entity entity
	 * @return Iterable
	 */
	Iterable<T> find(T entity);

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	boolean exists(ID id);

	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	boolean delete(ID id);

	/**
	 * Deletes a given entity.
	 *
	 * @param entity entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	void delete(T entity);

}
