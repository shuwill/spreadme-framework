/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.data.mybatis.util;

/**
 * 下划线、驼峰命名处理工具类
 */
public class NamingUtil {

	/**
	 * 驼峰转下划线
	 */
	public static String camelToUnderline(String text) {
		if (text == null || "".equals(text.trim())) {
			return "";
		}
		StringBuilder result = new StringBuilder(text.length() + 1);
		result.append(text, 0, 1);
		for (int i = 1; i < text.length(); i++) {
			if (!Character.isLowerCase(text.charAt(i))) {
				result.append('_');
			}
			result.append(text, i, i + 1);
		}
		return result.toString().toLowerCase();
	}

	/**
	 * 下划线转驼峰
	 */
	public static String underlineToCamel(String text) {
		if (text == null || "".equals(text.trim())) {
			return "";
		}
		int length = text.length();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = text.charAt(i);
			if (c == '_') {
				if (++i < length) {
					result.append(Character.toUpperCase(text.charAt(i)));
				}
			}
			else {
				result.append(c);
			}
		}
		return result.toString();
	}

}
