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


import java.util.List;

import org.spreadme.security.auth.exception.AuthenticationException;

public interface AuthenticationManager {

	default AuthenticatedToken<?> auth(CredentialsToken<?> credentialsToken) throws AuthenticationException {

		AuthenticationException lastException = new AuthenticationException("Authentication Fail");

		for (AuthenticationProvider provider : getAuthenticationProviders()) {
			if (!provider.supports(credentialsToken.getClass()))
				continue;
			return provider.auth(credentialsToken);
		}

		throw lastException;
	}

	List<AuthenticationProvider> getAuthenticationProviders();

}
