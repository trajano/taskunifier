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

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionAddSubTask extends AbstractAction {
	
	private TaskView taskView;
	
	public ActionAddSubTask(TaskView taskView) {
		this(taskView, 32, 32);
	}
	
	public ActionAddSubTask(TaskView taskView, int width, int height) {
		super(
				Translations.getString("action.name.add_subtask"),
				Images.getResourceImage("document.png", width, height));
		
		this.taskView = taskView;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.add_subtask"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_K,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		taskView.addTaskSelectionChangeListener(new TaskSelectionListener() {
			
			@Override
			public void taskSelectionChange(TaskSelectionChangeEvent event) {
				ActionAddSubTask.this.setEnabled(event.getSelectedTasks());
			}
			
		});
		
		this.setEnabled(this.taskView.getSelectedTasks());
	}
	
	private void setEnabled(Task[] tasks) {
		if (tasks == null)
			return;
		
		this.setEnabled(tasks.length == 1 && tasks[0].getParent() == null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.taskView.getSelectedTasks().length == 1)
			ActionAddSubTask.addSubTask(
					TemplateFactory.getInstance().getDefaultTemplate(),
					this.taskView.getSelectedTasks()[0]);
	}
	
	public static Task addSubTask(Template template, Task parent) {
		if (parent == null)
			return null;
		
		Task task = TaskFactory.getInstance().create("");
		
		if (template != null)
			template.applyToTask(task);
		
		template = MainFrame.getInstance().getSearcherView().getSelectedTaskSearcher().getTemplate();
		
		if (template != null)
			template.applyToTask(task);
		
		task.setParent(parent);
		task.setContext(parent.getContext());
		task.setFolder(parent.getFolder());
		task.setGoal(parent.getGoal());
		task.setLocation(parent.getLocation());
		
		MainFrame.getInstance().getTaskView().refreshTasks();
		
		if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add") != null
				&& Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add")) {
			TaskEditDialog dialog = new TaskEditDialog(
					task,
					MainFrame.getInstance().getFrame(),
					true);
			dialog.setVisible(true);
			
			if (dialog.isCancelled())
				TaskFactory.getInstance().markDeleted(task);
		} else {
			if (template == null)
				MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
			
			MainFrame.getInstance().getTaskView().setSelectedTaskAndStartEdit(
					task);
		}
		
		return task;
	}
	
}