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

package org.spreadme.security.util;

import javax.servlet.http.HttpServletRequest;

public abstract class WebUtil extends org.spreadme.commons.util.WebUtil {

	/**
	 * 判断是否是AJAX请求
	 *
	 * @param request HttpServletRequest
	 * @return is ajax request
	 */
	public static boolean isXMLHttpRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}

}
