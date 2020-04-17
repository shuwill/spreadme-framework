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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.data.base.BaseDao;
import org.spreadme.data.base.IdEntity;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseCrudService<T extends IdEntity> extends AbstractCrudService<T, Long> {

	protected static Logger logger = LoggerFactory.getLogger(BaseCrudService.class);

	protected BaseDao<T> dao;

	public boolean updateSelective(T entity) {
		return dao.updateByPrimaryKeySelective(entity) > 0;
	}

	@Autowired
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
		super.curdMapper = dao;
	}
}
