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
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.data.mybatis.mapper.CrudMapper;

public abstract class AbstractCrudService<T, ID extends Serializable> implements CrudService<T, ID> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected CrudMapper<T, ID> curdMapper;

	@Override
	public T create(T entity) {
		curdMapper.insertSelective(entity);
		return entity;
	}

	@Override
	public boolean update(T entity) {
		curdMapper.updateByPrimaryKey(entity);
		return true;
	}

	@Override
	public Optional<T> findById(ID id) {
		T entity = curdMapper.selectByPrimaryKey(id);
		return Optional.ofNullable(entity);
	}

	@Override
	public Optional<T> findOne(T entity) {
		T value = curdMapper.selectOne(entity);
		return Optional.ofNullable(value);
	}

	@Override
	public List<T> find(T entity) {
		return curdMapper.select(entity);
	}

	@Override
	public boolean exists(ID id) {
		return curdMapper.existsWithPrimaryKey(id);
	}

	@Override
	public boolean delete(ID id) {
		curdMapper.deleteByPrimaryKey(id);
		return true;
	}

	@Override
	public void delete(T entity) {
		curdMapper.delete(entity);
	}
}
