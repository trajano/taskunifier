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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizingException;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionDuplicateTasks extends AbstractViewAction {
	
	public ActionDuplicateTasks() {
		this(32, 32);
	}
	
	public ActionDuplicateTasks(int width, int height) {
		super(
				Translations.getString("action.duplicate_tasks"),
				ImageUtils.getResourceImage("duplicate.png", width, height),
				ViewType.TASKS,
				ViewType.CALENDAR);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.duplicate_tasks"));
		
		this.viewLoaded();
		
		ViewType.TASKS.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ActionDuplicateTasks.this.viewLoaded();
			}
			
		});
		
		ViewType.CALENDAR.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ActionDuplicateTasks.this.viewLoaded();
			}
			
		});
		
		this.setEnabled(false);
	}
	
	private void viewLoaded() {
		if (ViewType.TASKS.isLoaded() && ViewType.CALENDAR.isLoaded()) {
			ViewType.getTaskView().getTaskTableView().addModelSelectionChangeListener(
					new ModelSelectionListener() {
						
						@Override
						public void modelSelectionChange(
								ModelSelectionChangeEvent event) {
							ActionDuplicateTasks.this.setEnabled(ActionDuplicateTasks.this.shouldBeEnabled());
						}
						
					});
			
			ViewType.getCalendarView().getTaskCalendarView().addModelSelectionChangeListener(
					new ModelSelectionListener() {
						
						@Override
						public void modelSelectionChange(
								ModelSelectionChangeEvent event) {
							ActionDuplicateTasks.this.setEnabled(ActionDuplicateTasks.this.shouldBeEnabled());
						}
						
					});
			
			this.setEnabled(this.shouldBeEnabled());
		}
	}
	
	@Override
	protected boolean shouldBeEnabled() {
		if (!super.shouldBeEnabled())
			return false;
		
		Task[] tasks = ViewType.getSelectedTasks();
		
		if (tasks == null)
			return false;
		
		return tasks.length != 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionDuplicateTasks.duplicateTasks(ViewType.getSelectedTasks());
	}
	
	public static void duplicateTasks(Task[] tasks) {
		Map<Task, Task> newTasks = new HashMap<Task, Task>();
		
		boolean set = false;
		
		try {
			set = Synchronizing.setSynchronizing(true);
		} catch (SynchronizingException e) {
			
		}
		
		for (Task task : tasks) {
			Task newTask = TaskFactory.getInstance().create(task);
			newTasks.put(task, newTask);
		}
		
		for (Task newTask : newTasks.values()) {
			if (newTask.getParent() != null) {
				if (newTasks.containsKey(newTask.getParent()))
					newTask.setParent(newTasks.get(newTask.getParent()));
			}
		}
		
		if (set) {
			try {
				Synchronizing.setSynchronizing(false);
			} catch (SynchronizingException e) {
				
			}
		}
		
		ViewType.refreshTasks();
		ViewType.setSelectedTasks(newTasks.values().toArray(new Task[0]));
	}
	
}
