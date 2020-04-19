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

package org.spreadme.web.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * {@link org.spreadme.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for {@link HttpMessageConverter}s.
 *
 * @author Dave Syer
 * @author Christian Dupuis
 * @author Piotr Maj
 * @author Oliver Gierke
 * @author David Liu
 * @author Andy Wilkinson
 * @author Sebastien Deleuze
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 * @since 2.0.0
 */
@Configuration
@Import(HttpMessageConvertersConfiguration.class)
public class HttpMessageConvertersAutoConfiguration {

	static final String PREFERRED_MAPPER_PROPERTY = "spring.http.converters.preferred-json-mapper";

	private final List<HttpMessageConverter<?>> converters;

	public HttpMessageConvertersAutoConfiguration(ObjectProvider<HttpMessageConverter<?>> convertersProvider) {
		this.converters = convertersProvider.orderedStream().collect(Collectors.toList());
	}

	@Bean
	public HttpMessageConverters messageConverters() {
		return new HttpMessageConverters(this.converters);
	}

	@Configuration
	@EnableConfigurationProperties(HttpProperties.class)
	protected static class StringHttpMessageConverterConfiguration {

		private final HttpProperties.Encoding properties;

		protected StringHttpMessageConverterConfiguration(HttpProperties httpProperties) {
			this.properties = httpProperties.getEncoding();
		}

		@Bean
		public StringHttpMessageConverter stringHttpMessageConverter() {
			StringHttpMessageConverter converter = new StringHttpMessageConverter(this.properties.getCharset());
			converter.setWriteAcceptCharset(false);
			return converter;
		}

	}

}
