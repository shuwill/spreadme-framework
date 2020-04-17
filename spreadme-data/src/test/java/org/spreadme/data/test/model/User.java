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

package org.spreadme.data.test.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.spreadme.data.base.IdEntity;

@Entity
@Table(name = "USERS")
public class User extends IdEntity {

	private static final long serialVersionUID = -6696369052186272220L;

	private String username;

	private String email;

	private String avatar;

	private Date createTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + super.id + '\'' +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", avatar='" + avatar + '\'' +
				", createTime='" + createTime + '\'' +
				'}';
	}
}
