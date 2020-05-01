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

package org.spreadme.website;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.commons.util.StringUtil;
import org.spreadme.web.result.ResultMessage;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * WebExceptionHandler
 * @author shuwei.wang
 */
@ControllerAdvice
public class WebExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(value = Throwable.class)
	public ResultMessage handle(Throwable e) {
		LOGGER.error(StringUtil.stringifyException(e));
		if (e instanceof BindException || e instanceof MethodArgumentNotValidException) {
			return handleValidationException(e);
		}
		return ResultMessage.newErrorMessage(e.getMessage());
	}

	private ResultMessage handleValidationException(Throwable ex) {
		BindingResult bindingResult = getBuildingResult(ex);
		if (bindingResult != null) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			if (CollectionUtil.isNotEmpty(errors)) {
				String message = errors.stream()
						.filter(e -> StringUtil.isNotBlank(e.getDefaultMessage()))
						.map(this::getValidationExceptionMessage)
						.collect(Collectors.joining(","));
				return ResultMessage.newErrorMessage(message);
			}
		}
		return ResultMessage.newErrorMessage(ex.getMessage());
	}

	private BindingResult getBuildingResult(Throwable ex) {
		if (ex instanceof BindingResult) {
			return (BindingResult) ex;
		}
		else if (ex instanceof MethodArgumentNotValidException) {
			return ((MethodArgumentNotValidException) ex).getBindingResult();
		}
		return null;
	}

	private String getValidationExceptionMessage(FieldError error) {
		return error.getField() + ":" + error.getDefaultMessage();
	}
}
