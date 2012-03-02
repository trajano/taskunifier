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
package com.leclercb.taskunifier.gui.components.reminder;

import java.util.Comparator;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

class ReminderComparator implements Comparator<Task> {
	
	public static final ReminderComparator INSTANCE = new ReminderComparator();
	
	private ReminderComparator() {
		
	}
	
	@Override
	public int compare(Task t1, Task t2) {
		boolean inZone1 = TaskUtils.isInDueDateReminderZone(t1);
		boolean inZone2 = TaskUtils.isInDueDateReminderZone(t2);
		
		if (inZone1 && !inZone2)
			return -1;
		
		if (!inZone1 && inZone2)
			return 1;
		
		inZone1 = TaskUtils.isInStartDateReminderZone(t1);
		inZone2 = TaskUtils.isInStartDateReminderZone(t2);
		
		if (inZone1 && !inZone2)
			return -1;
		
		if (!inZone1 && inZone2)
			return 1;
		
		int importance1 = TaskUtils.getImportance(t1);
		int importance2 = TaskUtils.getImportance(t2);
		
		// Sort descending !
		if (importance1 != importance2)
			return new Integer(importance2).compareTo(importance1);
		
		return ModelComparator.INSTANCE.compare(t1, t2);
	}
	
}
