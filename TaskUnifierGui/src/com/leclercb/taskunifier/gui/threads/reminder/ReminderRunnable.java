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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionTaskReminders;
import com.leclercb.taskunifier.gui.components.reminder.ReminderDialog;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;
import com.leclercb.taskunifier.gui.threads.reminder.progress.ReminderDefaultProgressMessage;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

class ReminderRunnable implements Runnable, PropertyChangeListener, ListChangeListener {
	
	private static final long SLEEP_TIME = 10 * 1000;
	
	private List<Task> notifiedTasks;
	
	public ReminderRunnable() {
		this.notifiedTasks = new ArrayList<Task>();
		TaskFactory.getInstance().addListChangeListener(
				new WeakListChangeListener(TaskFactory.getInstance(), this));
		TaskFactory.getInstance().addPropertyChangeListener(
				new WeakPropertyChangeListener(TaskFactory.getInstance(), this));
	}
	
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(SLEEP_TIME);
				
				if (Synchronizing.getInstance().isSynchronizing())
					continue;
				
				TUSwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						boolean reminders = false;
						List<Task> tasks = TaskFactory.getInstance().getList();
						
						synchronized (this) {
							for (final Task task : tasks) {
								if (ReminderRunnable.this.notifiedTasks.contains(task))
									continue;
								
								if (!task.getModelStatus().isEndUserStatus())
									continue;
								
								if (TaskUtils.isInStartDateReminderZone(task)
										|| TaskUtils.isInDueDateReminderZone(task)) {
									ReminderRunnable.this.notifiedTasks.remove(task);
									ReminderRunnable.this.notifiedTasks.add(task);
									
									ReminderDialog.getInstance().getReminderPanel().getReminderList().addTask(
											task);
									
									Constants.PROGRESS_MONITOR.addMessage(new ReminderDefaultProgressMessage(
											task));
									
									reminders = true;
								}
							}
						}
						
						if (reminders) {
							ActionTaskReminders.taskReminders(true);
						}
					}
					
				});
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	@Override
	public void listChange(ListChangeEvent evt) {
		if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			ReminderDialog.getInstance().getReminderPanel().getReminderList().removeTask(
					(Task) evt.getValue());
			this.notifiedTasks.remove(evt.getValue());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Task.PROP_DUE_DATE)
				|| evt.getPropertyName().equals(Task.PROP_DUE_DATE_REMINDER)
				|| evt.getPropertyName().equals(Task.PROP_START_DATE)
				|| evt.getPropertyName().equals(Task.PROP_START_DATE_REMINDER)
				|| evt.getPropertyName().equals(Task.PROP_COMPLETED)
				|| !((Task) evt.getSource()).getModelStatus().isEndUserStatus()) {
			ReminderDialog.getInstance().getReminderPanel().getReminderList().removeTask(
					(Task) evt.getSource());
			this.notifiedTasks.remove(evt.getSource());
		}
	}
	
}
