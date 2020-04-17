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

package org.spreadme.data.id;

import tk.mybatis.mapper.genid.GenId;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class LongIdGenerator implements GenId<Long> {

	private static IdGenerator<Long> idGeneratorInstance;

	@Override
	public Long genId(String table, String column) {
		return idGeneratorInstance.generate();
	}

	public static Long generate() {
		return idGeneratorInstance.generate();
	}

	protected static void setIdGeneratorInstance(IdGenerator<Long> idGeneratorInstance) {
		LongIdGenerator.idGeneratorInstance = idGeneratorInstance;
	}
}
