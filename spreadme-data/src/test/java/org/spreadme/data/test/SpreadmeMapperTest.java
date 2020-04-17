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

package org.spreadme.data.test;


import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.boot.SpringBootApplication;
import org.spreadme.commons.util.StringUtil;
import org.spreadme.data.test.model.User;
import org.spreadme.data.test.server.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpreadmeMapperTest.class)
@SpringBootApplication
@EnableAspectJAutoProxy
public class SpreadmeMapperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpreadmeMapperTest.class);

	@Autowired
	private UserService userService;

	@Test
	public void testStartup() {

	}

	@Test
	public void testDataMoudle() {
		User user = userService.create(getUser());
		Assert.assertNotNull(userService.get(user.getId()));
	}

	private User getUser(){
		User user = new User();
		user.setUsername(StringUtil.randomString(6));
		user.setEmail(StringUtil.randomString(6));
		user.setAvatar(StringUtil.randomString(6));
		user.setCreateTime(new Date());
		return user;
	}
}
