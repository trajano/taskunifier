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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.KeyStroke;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionAddSubTask extends AbstractViewAction {
	
	public ActionAddSubTask() {
		this(32, 32);
	}
	
	public ActionAddSubTask(int width, int height) {
		super(
				Translations.getString("action.add_subtask"),
				ImageUtils.getResourceImage("subtask.png", width, height),
				ViewType.TASKS,
				ViewType.CALENDAR);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_subtask"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_K,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		this.viewLoaded();
		
		ViewType.TASKS.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ActionAddSubTask.this.viewLoaded();
			}
			
		});
		
		ViewType.CALENDAR.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ActionAddSubTask.this.viewLoaded();
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
							ActionAddSubTask.this.setEnabled(ActionAddSubTask.this.shouldBeEnabled());
						}
						
					});
			
			ViewType.getCalendarView().getTaskCalendarView().addModelSelectionChangeListener(
					new ModelSelectionListener() {
						
						@Override
						public void modelSelectionChange(
								ModelSelectionChangeEvent event) {
							ActionAddSubTask.this.setEnabled(ActionAddSubTask.this.shouldBeEnabled());
						}
						
					});
			
			MainFrame.getInstance().addPropertyChangeListener(
					MainView.PROP_SELECTED_VIEW,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							ActionAddSubTask.this.setEnabled(ActionAddSubTask.this.shouldBeEnabled());
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
		
		return tasks.length == 1;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ViewType.getSelectedTasks().length == 1)
			ActionAddSubTask.addSubTask(ViewType.getSelectedTasks()[0], true);
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
		
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		TaskTemplate searcherTemplate = null;
		
		if (viewType == ViewType.CALENDAR || viewType == ViewType.TASKS) {
			searcherTemplate = ViewType.getSelectedTaskSearcher().getTemplate();
			
			if (searcherTemplate == null)
				ViewType.selectDefaultTaskSearcher();
		}
		
		Task task = TaskFactory.getInstance().create(
				Translations.getString("task.default.title"));
		
		if (template != null)
			template.applyTo(task);
		
		if (searcherTemplate != null)
			searcherTemplate.applyTo(task);
		
		task.setParent(parent);
		task.setContext(parent.getContext());
		task.setFolder(parent.getFolder());
		task.setGoal(parent.getGoal());
		task.setLocation(parent.getLocation());
		
		ViewType.refreshTasks();
		
		if (edit) {
			if (viewType == ViewType.CALENDAR
					|| Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add")) {
				if (!ActionEditTasks.editTasks(new Task[] { task }))
					TaskFactory.getInstance().markDeleted(task);
			} else {
				ViewType.getTaskView().getTaskTableView().setSelectedTaskAndStartEdit(
						task);
			}
		}
		
		return task;
	}
	
}
