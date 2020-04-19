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

package org.spreadme.security.session.support;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spreadme.cache.CacheClient;
import org.spreadme.commons.id.IdentifierGenerator;
import org.spreadme.commons.id.support.UUIDGenerator;
import org.spreadme.commons.util.StringUtil;
import org.spreadme.security.auth.AuthenticatedToken;
import org.spreadme.security.cookie.CookieUtil;
import org.spreadme.security.session.SessionManager;

public class CookieSessionManager implements SessionManager {

	private static final IdentifierGenerator<UUID> UUID_GENERATOR = new UUIDGenerator();

	private final CacheClient<String, AuthenticatedToken<?>> cacheClient;

	private final String sessionKey;

	private final int timeout;

	public CookieSessionManager(CacheClient<String, AuthenticatedToken<?>> cacheClient, String sessionKey, int timeout) {
		this.cacheClient = cacheClient;
		this.sessionKey = sessionKey;
		this.timeout = timeout;
	}

	@Override
	public void add(HttpServletRequest request, HttpServletResponse response, AuthenticatedToken<?> token) {
		String sessionId = UUID_GENERATOR.nextIdentifier().toString();
		token.setToken(sessionId);
		if (request.isSecure()) {
			CookieUtil.addCookie(response, null, this.sessionKey, sessionId, true);
		}
		else {
			CookieUtil.addCookie(response, null, this.sessionKey, sessionId, false);
		}
		this.cacheClient.put(sessionId, token, this.timeout, TimeUnit.SECONDS);
	}

	@Override
	public AuthenticatedToken<?> load(HttpServletRequest request) {
		Cookie cookie = CookieUtil.getCookie(request, this.sessionKey);
		if (cookie == null || StringUtil.isBlank(cookie.getValue())) {
			return null;
		}
		return this.cacheClient.get(cookie.getValue());
	}

	@Override
	public void remove(HttpServletRequest request, HttpServletResponse response, AuthenticatedToken<?> token) {
		CookieUtil.deleteCookie(request, response, this.sessionKey);
	}

}
