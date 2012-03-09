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
import java.util.HashMap;
import java.util.Map;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionDuplicateTasks extends AbstractViewTaskSelectionAction {
	
	public ActionDuplicateTasks() {
		this(32, 32);
	}
	
	public ActionDuplicateTasks(int width, int height) {
		super(
				Translations.getString("action.duplicate_tasks"),
				ImageUtils.getResourceImage("duplicate.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.duplicate_tasks"));
	}
	
	@Override
	public boolean shouldBeEnabled() {
		if (!super.shouldBeEnabled())
			return false;
		
		Task[] tasks = ViewUtils.getSelectedTasks();
		
		if (tasks == null)
			return false;
		
		return tasks.length != 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionDuplicateTasks.duplicateTasks(ViewUtils.getSelectedTasks());
	}
	
	public static void duplicateTasks(Task[] tasks) {
		Map<Task, Task> newTasks = new HashMap<Task, Task>();
		
		Synchronizing.setSynchronizing(true);
		
		try {
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
		} finally {
			Synchronizing.setSynchronizing(false);
		}
		
		ViewUtils.refreshTasks();
		ViewUtils.setSelectedTasks(newTasks.values().toArray(new Task[0]));
	}
	
}
