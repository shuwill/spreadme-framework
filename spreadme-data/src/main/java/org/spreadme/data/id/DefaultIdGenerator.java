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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.id.IdentifierGenerator;
import org.spreadme.commons.id.support.SnowflakeLongGenerator;
import org.spreadme.commons.lang.Randoms;

import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public class DefaultIdGenerator implements IdGenerator<Long>, SmartInitializingSingleton {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIdGenerator.class);

	private IdentifierGenerator<Long> idGenerator;

	@Override
	public Long generate() {
		return idGenerator.nextIdentifier();
	}

	@Override
	public void afterSingletonsInstantiated() {
		idGenerator = new SnowflakeLongGenerator(Randoms.nextInt(30), Randoms.nextInt(30));
		LOGGER.info("Set IdGeneratorInstance : {}", this);
		LongIdGenerator.setIdGeneratorInstance(this);
	}
}
