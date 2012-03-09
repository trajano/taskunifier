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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.components.batchaddtask.BatchAddTaskDialog;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionBatchAddTasks extends AbstractViewAction {
	
	public ActionBatchAddTasks() {
		this(32, 32);
	}
	
	public ActionBatchAddTasks(int width, int height) {
		super(
				Translations.getString("action.batch_add_tasks"),
				ImageUtils.getResourceImage("batch.png", width, height),
				ViewType.TASKS,
				ViewType.CALENDAR);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.batch_add_tasks"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_B,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionBatchAddTasks.batchAddTasks();
	}
	
	public static void batchAddTasks() {
		BatchAddTaskDialog.getInstance().setVisible(true);
	}
	
	public static void batchAddTasks(TaskTemplate template, String[] titles) {
		Synchronizing.setSynchronizing(true);
		
		try {
			List<Task> previousParentTasks = new ArrayList<Task>();
			List<Task> tasks = new ArrayList<Task>();
			
			for (String title : titles) {
				int subTaskLevel = 0;
				while (title.startsWith("\t")) {
					subTaskLevel++;
					title = title.substring(1);
				}
				
				title = title.trim();
				
				if (title.length() == 0)
					continue;
				
				Task task = null;
				
				if (subTaskLevel == 0 || previousParentTasks.size() == 0) {
					task = ActionAddTask.addTask(template, title, false);
					previousParentTasks.clear();
					previousParentTasks.add(task);
				} else {
					if (subTaskLevel > previousParentTasks.size())
						subTaskLevel = previousParentTasks.size();
					
					task = ActionAddSubTask.addSubTask(
							template,
							previousParentTasks.get(subTaskLevel - 1),
							false);
					
					for (int i = previousParentTasks.size() - 1; i >= subTaskLevel; i--)
						previousParentTasks.remove(i);
					
					previousParentTasks.add(task);
				}
				
				task.setTitle(title);
				
				tasks.add(task);
			}
			
			ViewUtils.setSelectedTasks(tasks.toArray(new Task[0]));
		} finally {
			Synchronizing.setSynchronizing(false);
		}
	}
	
}
