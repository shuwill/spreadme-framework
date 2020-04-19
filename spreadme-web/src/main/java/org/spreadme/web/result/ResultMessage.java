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

package org.spreadme.web.result;

import java.util.Date;
import java.util.HashMap;

import org.spreadme.commons.lang.Dates;
import org.spreadme.commons.util.NumberUtil;

public class ResultMessage extends HashMap<String, Object> {

	private static final long serialVersionUID = -7538477481940718978L;

	private static final String TIMESTAMP_KEY = "timestamp";

	private static final String CODE_KEY = "code";

	private static final String MESSAGE_KEY = "message";

	private static final String RESULT_KEY = "result";

	private static final Integer SUCCESS_CODE = 200;

	private static final String SUCCESS_MESSAGE = "SUCCESS";

	private static final Integer ERROR_CODE = 100;

	private static final String ERROR_MESSAGE = "ERROR";

	public static final ResultMessage SUCCESS = new ResultMessage(SUCCESS_CODE, SUCCESS_MESSAGE);

	public static final ResultMessage ERROR = new ResultMessage(ERROR_CODE, ERROR_MESSAGE);

	public ResultMessage(int code) {
		this(code, null);
	}

	public ResultMessage(Integer code, String message) {
		if (code != null) {
			super.put(CODE_KEY, code);
		}
		if (message != null) {
			super.put(MESSAGE_KEY, message);
		}
		super.put(TIMESTAMP_KEY, Dates.format(new Date(), Dates.DateFormatType.NORM_DATETIME_PATTERN));
	}

	/**
	 * 返回成功消息(使用默认的对象名称result)
	 * @param data 返回对象value
	 * @return ResultMessage
	 */
	public static ResultMessage newSuccessMessage(Object data) {
		ResultMessage resultMessage = new ResultMessage(SUCCESS_CODE, SUCCESS_MESSAGE);
		if (data != null) {
			resultMessage.put(RESULT_KEY, data);
		}
		return resultMessage;
	}

	/**
	 * 返回失败消息
	 * @param message 失败消息
	 * @return ResultMessage
	 */
	public static ResultMessage newErrorMessage(String message) {
		return new ResultMessage(ERROR_CODE, message);
	}

	public static ResultMessage newErrorMessage(Object data) {
		ResultMessage resultMessage = new ResultMessage(ERROR_CODE, ERROR_MESSAGE);
		if (data != null) {
			resultMessage.put(RESULT_KEY, data);
		}
		return resultMessage;
	}

	public Integer getCode() {
		Object code = this.get(MESSAGE_KEY);
		return code == null ? null : NumberUtil.toNumber(code).intValue();
	}

	public String getMessage() {
		Object message = this.get(MESSAGE_KEY);
		return message == null ? null : message.toString();
	}

	@Override
	public Object put(String key, Object value) {
		if (CODE_KEY.equals(key)) {
			throw new RuntimeException("ResultMessage is not allow to use special key '" + CODE_KEY + "'.");
		}
		if (MESSAGE_KEY.equals(key)) {
			throw new RuntimeException("ResultMessage is not allow to use special key '" + MESSAGE_KEY + "'.");
		}
		return super.put(key, value);
	}
}
