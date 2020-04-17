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

package org.spreadme.data.test.server;


import org.spreadme.data.service.BaseCrudService;
import org.spreadme.data.test.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
@Service
@Transactional
public class UserServiceImpl extends BaseCrudService<User> implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User get(Long id) {
		return super.findById(id).orElse(null);
	}

	@Override
	public String getWaterMarkKey(Long id) {
		return userDao.getWaterMarkKey(id);
	}

}
