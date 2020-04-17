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

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class IdEntity implements Entity, TypeAliases, Serializable {

	private static final long serialVersionUID = -3477746452232479339L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "org.spreadme.data.id.LongIdGenerator")
	protected Long id;

	public IdEntity() {
	}

	public IdEntity(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		final int prime = 31;
		int result = 1;
		result = result * prime + id.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IdEntity other = (IdEntity) obj;
		if (id == null && other.id == null) {
			return false;
		}
		return id == null || id.equals(other.id);
	}
}
