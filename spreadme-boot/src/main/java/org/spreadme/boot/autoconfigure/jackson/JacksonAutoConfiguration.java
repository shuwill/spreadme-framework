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

package org.spreadme.boot.autoconfigure.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson AutoConfiguration
 * @author shuwei.wang
 */
@Configuration
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonAutoConfiguration {

	@Bean
	public JsonComponentModule jsonComponentModule() {
		return new JsonComponentModule();
	}

	@Configuration
	static class JacksonObjectMapperBuilderConfiguration {

		@Bean
		public ObjectMapper objectMapper(JacksonProperties properties) {
			ObjectMapper objectMapper = new ObjectMapper();
			// 忽略transient字段
			objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
			objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			objectMapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
			objectMapper.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

			properties.getVisibility().forEach(objectMapper::setVisibility);
			properties.getSerialization().forEach(objectMapper::configure);
			properties.getDeserialization().forEach(objectMapper::configure);
			properties.getMapper().forEach(objectMapper::configure);
			properties.getParser().forEach(objectMapper::configure);
			properties.getGenerator().forEach(objectMapper::configure);
			if(properties.getDefaultPropertyInclusion() != null){
				objectMapper.setDefaultPropertyInclusion(properties.getDefaultPropertyInclusion());
			}
			if(properties.getTimeZone() != null){
				objectMapper.setTimeZone(properties.getTimeZone());
			}
			if(properties.getLocale() != null){
				objectMapper.setLocale(properties.getLocale());
			}
			return objectMapper;
		}
	}
}
