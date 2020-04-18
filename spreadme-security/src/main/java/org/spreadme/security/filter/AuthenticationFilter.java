package org.spreadme.security.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spreadme.security.auth.CredentialsToken;

public interface AuthenticationFilter {

	default void authFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws Exception {

		if (!requiresAuthentication(request)) {
			filterChain.doFilter(request, response);
		}
		else if (executeAuth(request, response)) {
			filterChain.doFilter(request, response);
		}
	}

	default boolean executeAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CredentialsToken<?> token = createToken(request, response);
		return doAuth(request, response, token);
	}

	boolean requiresAuthentication(HttpServletRequest request);

	CredentialsToken<?> createToken(HttpServletRequest request, HttpServletResponse response);

	boolean doAuth(HttpServletRequest request, HttpServletResponse response, CredentialsToken<?> token) throws Exception ;
}
