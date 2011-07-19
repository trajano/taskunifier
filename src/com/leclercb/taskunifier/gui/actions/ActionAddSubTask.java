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

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.View;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionAddSubTask extends AbstractAction {
	
	private TaskView taskView;
	
	public ActionAddSubTask(TaskView taskView) {
		this(taskView, 32, 32);
	}
	
	public ActionAddSubTask(TaskView taskView, int width, int height) {
		super(
				Translations.getString("action.add_subtask"),
				Images.getResourceImage("subtask.png", width, height));
		
		this.taskView = taskView;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_subtask"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_K,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		taskView.addModelSelectionChangeListener(new ModelSelectionListener() {
			
			@Override
			public void modelSelectionChange(ModelSelectionChangeEvent event) {
				ActionAddSubTask.this.setEnabled(event.getSelectedModels());
			}
			
		});
		
		this.setEnabled(this.taskView.getSelectedTasks());
	}
	
	private void setEnabled(Model[] tasks) {
		if (tasks == null)
			return;
		
		this.setEnabled(tasks.length == 1
				&& ((Task) tasks[0]).getParent() == null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.taskView.getSelectedTasks().length == 1)
			ActionAddSubTask.addSubTask(
					this.taskView.getSelectedTasks()[0],
					true);
	}
	
	public static Task addSubTask(Task parent, boolean edit) {
		return addSubTask(
				TaskTemplateFactory.getInstance().getDefaultTemplate(),
				parent,
				edit);
	}
	
	public static Task addSubTask(
			TaskTemplate template,
			Task parent,
			boolean edit) {
		CheckUtils.isNotNull(parent, "Parent cannot be null");
		
		if (parent.getParent() != null)
			return null;
		
		MainFrame.getInstance().setSelectedView(View.TASKS);
		
		TaskTemplate searcherTemplate = MainFrame.getInstance().getTaskSearcherView().getSelectedTaskSearcher().getTemplate();
		
		if (searcherTemplate == null)
			MainFrame.getInstance().getTaskSearcherView().selectDefaultTaskSearcher();
		
		Task task = TaskFactory.getInstance().create("");
		
		if (template != null)
			template.applyTo(task);
		
		if (searcherTemplate != null)
			searcherTemplate.applyTo(task);
		
		task.setParent(parent);
		task.setContext(parent.getContext());
		task.setFolder(parent.getFolder());
		task.setGoal(parent.getGoal());
		task.setLocation(parent.getLocation());
		
		MainFrame.getInstance().getTaskView().refreshTasks();
		
		if (edit) {
			if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add")) {
				if (!ActionEditTasks.editTasks(new Task[] { task }))
					TaskFactory.getInstance().markDeleted(task);
			} else {
				MainFrame.getInstance().getTaskView().setSelectedTaskAndStartEdit(
						task);
			}
		}
		
		return task;
	}
	
}
