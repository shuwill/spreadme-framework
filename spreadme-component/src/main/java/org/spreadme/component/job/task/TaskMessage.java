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

package org.spreadme.component.job.task;

import org.spreadme.commons.message.Message;

/**
 * job message
 * @author shuwei.wang
 */
public class TaskMessage implements Message {

	private static final long serialVersionUID = -459709068017433665L;

	private String taskName;
	private Boolean cancel = false;
	private String cron;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Boolean getCancel() {
		return cancel;
	}

	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
}
