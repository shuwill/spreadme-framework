/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.spreadme.security.auth;

import java.security.Principal;

import org.spreadme.commons.cache.Cacheable;


public class AuthenticatedToken<P extends Principal> extends CredentialsToken<Principal> implements Cacheable {

	private static final long serialVersionUID = -332945055477079257L;

	private String token;

	private P principal;

	public AuthenticatedToken() {
		super(null);
	}

	public AuthenticatedToken(P principal, Object credentials) {
		super(credentials);
		this.principal = principal;
	}

	@Override
	public P getPrincipal() {
		return this.principal;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	public void setPrincipal(P principal) {
		this.principal = principal;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toCacheKey() {
		return this.token;
	}
}
