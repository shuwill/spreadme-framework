/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.security.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spreadme.commons.lang.Charsets;
import org.spreadme.security.util.WebUtil;

/**
 * RedirectStrategy
 * @author shuwei.wang
 */
public interface RedirectStrategy {

	/**
	 * Redirect 跳转
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param message 消息
	 * @param url 跳转URL
	 * @throws IOException IOException
	 */
	void redirect(HttpServletRequest request, HttpServletResponse response, String message, String url) throws IOException;

	/**
	 * 返回JSON数据
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param message 消息
	 * @throws IOException IOException
	 */
	default void jsonResponse(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		if (message != null) {
			WebUtil.flush(message.getBytes(Charsets.UTF_8), response);
		}
	}
}
