/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.utils;

import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;

public final class TaskUtils {

	private TaskUtils() {

	}

	public static int getImportance(Task task) {
		CheckUtils.isNotNull(task, "Task cannot be null");

		int importance = 2;

		switch (task.getPriority()) {
		case NEGATIVE:
			importance += -1;
			break;
		case LOW:
			importance += 0;
			break;
		case MEDIUM:
			importance += 1;
			break;
		case HIGH:
			importance += 2;
			break;
		case TOP:
			importance += 3;
			break;
		}

		importance += (task.isStar() ? 1 : 0);

		if (task.getDueDate() != null) {
			long milliSeconds1 = task.getDueDate().getTimeInMillis();
			long milliSeconds2 = Calendar.getInstance().getTimeInMillis();
			long diff = milliSeconds1 - milliSeconds2;
			double diffDays = diff / (24 * 60 * 60 * 1000.0);

			if (diffDays > 14)
				importance += 0;
			else if (diffDays >= 7)
				importance += 1;
			else if (diffDays >= 2)
				importance += 2;
			else if (diffDays == 1)
				importance += 3;
			else if (diffDays == 0)
				importance += 5;
			else
				importance += 6;
		}

		return importance;
	}

	public static boolean showTask(Task task, TaskFilter filter) {
		if (!task.getModelStatus().equals(ModelStatus.LOADED)
				&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}

		// If a filtered parent task has non filtered children, it must be
		// displayed
		if (task.getParent() == null) {
			List<Task> tasks = TaskFactory.getInstance().getList();
			for (Task t : tasks) {
				if (task.equals(t.getParent()))
					if (filter.include(t))
						return true;
			}
		}

		return filter.include(task);
	}

}
