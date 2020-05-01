/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.spreadme.component.cache.hazelcast.HazelcastCacheConfiguration;
import org.spreadme.component.hazelcast.HazelcastConfiguration;
import org.spreadme.component.message.hazelcast.HazelcastMessageConfiguration;

import org.springframework.context.annotation.Import;

/**
 * enable hazelcast
 * @author shuwei.wang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Import({HazelcastConfiguration.class, HazelcastCacheConfiguration.class, HazelcastMessageConfiguration.class})
public @interface EnableHazelcast {

	String name() default "";
}
