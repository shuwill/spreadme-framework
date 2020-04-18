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

package org.spreadme.security.auth.support;


import org.spreadme.security.auth.AuthenticatedToken;
import org.spreadme.security.auth.AuthenticationProvider;
import org.spreadme.security.auth.AuthorizingRealm;
import org.spreadme.security.auth.CredentialsMatcher;
import org.spreadme.security.auth.CredentialsToken;
import org.spreadme.security.auth.exception.AuthenticationException;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	private AuthorizingRealm<?> authorizingRealm;

	private CredentialsMatcher credentialsMatcher;

	@Override
	public AuthenticatedToken<?> auth(CredentialsToken<?> credentials) throws AuthenticationException {
		AuthenticatedToken<?> token = authorizingRealm.getAuthenticatedToken(credentials);
		if (!credentialsMatcher.match(credentials, token)) {
			throw new AuthenticationException("Auth Fail");
		}
		return token;
	}

	@Override
	public boolean supports(Class<? extends CredentialsToken> credentialsClass) {
		return UsernamePasswordToken.class.isAssignableFrom(credentialsClass);
	}

	public void setAuthorizingRealm(AuthorizingRealm<?> authorizingRealm) {
		this.authorizingRealm = authorizingRealm;
	}

	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		this.credentialsMatcher = credentialsMatcher;
	}
}
