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

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * task info
 * @author shuwei.wang
 */
public class TaskInfo implements Serializable {

	private static final long serialVersionUID = -4622973679640088858L;

	private String name;
	private String cron;
	private Boolean isCancel;
	private transient ScheduledFuture<?> future;

	public TaskInfo() {
	}

	public TaskInfo(String name, Boolean isCancel) {
		this.name = name;
		this.isCancel = isCancel;
	}

	public TaskInfo(String name, String cron, Boolean isCancel) {
		this.name = name;
		this.cron = cron;
		this.isCancel = isCancel;
	}

	public TaskInfo(String name, String cron, Boolean isCancel, ScheduledFuture<?> future) {
		this.name = name;
		this.cron = cron;
		this.isCancel = isCancel;
		this.future = future;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Boolean getCancel() {
		return isCancel;
	}

	public void setCancel(Boolean cancel) {
		isCancel = cancel;
	}

	public ScheduledFuture<?> getFuture() {
		return future;
	}

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TaskInfo taskInfo = (TaskInfo) o;
		return Objects.equals(name, taskInfo.name) &&
				Objects.equals(cron, taskInfo.cron);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, cron);
	}
}
