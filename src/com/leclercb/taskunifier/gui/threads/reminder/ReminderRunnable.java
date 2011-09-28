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
package com.leclercb.taskunifier.gui.threads.reminder;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionTaskReminders;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.components.reminder.ReminderDialog;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

class ReminderRunnable implements Runnable, PropertyChangeListener {
	
	private static final long SLEEP_TIME = 10000;
	
	public ReminderRunnable() {
		TaskFactory.getInstance().addPropertyChangeListener(this);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				
			}
			
			if (Synchronizing.isSynchronizing())
				continue;
			
			boolean reminders = false;
			List<Task> list = TaskFactory.getInstance().getList();
			for (Task task : list) {
				GuiTask gTask = (GuiTask) task;
				
				if (!task.getModelStatus().isEndUserStatus())
					continue;
				
				boolean inSD = (!gTask.isStartDateReminded() && TaskUtils.isInStartDateReminderZone(task));
				boolean inDD = (!gTask.isDueDateReminded() && TaskUtils.isInDueDateReminderZone(task));
				
				if (inSD || inDD) {
					if (inSD)
						gTask.setStartDateReminded(true);
					
					if (inDD)
						gTask.setDueDateReminded(true);
					
					reminders = true;
				}
			}
			
			if (reminders) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						ActionTaskReminders.taskReminders(true);
					}
					
				});
			}
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Task task = (Task) evt.getSource();
		
		if (!task.getModelStatus().isEndUserStatus()) {
			ReminderDialog.getInstance().getReminderPanel().getReminderList().removeTask(
					task);
		}
		
		if (evt.getPropertyName().equals(GuiTask.PROP_START_DATE_REMINDED)
				|| evt.getPropertyName().equals(GuiTask.PROP_DUE_DATE_REMINDED)) {
			if (EqualsUtils.equals(evt.getNewValue(), Boolean.TRUE))
				ReminderDialog.getInstance().getReminderPanel().getReminderList().addTask(
						task);
			else
				ReminderDialog.getInstance().getReminderPanel().getReminderList().removeTask(
						(Task) evt.getSource());
		}
	}
	
}
