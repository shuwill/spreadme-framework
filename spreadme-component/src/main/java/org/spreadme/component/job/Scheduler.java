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

package org.spreadme.component.job;

import java.util.Collection;
import java.util.Map;

import org.spreadme.component.job.task.Task;
import org.spreadme.component.job.task.TaskInfo;


public interface Scheduler {

	void schedule(Collection<Task> tasks);

	String getId();

	void cancel(String taskName);

	void cron(String taskName, String cron);

	Map<String, TaskInfo> getTasks();
}
