/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionEditTask extends AbstractAction {
	
	public ActionEditTask(TaskView taskView) {
		this(taskView, 32, 32);
	}
	
	public ActionEditTask(TaskView taskView, int width, int height) {
		super(
				Translations.getString("action.name.edit_task"),
				Images.getResourceImage("edit.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.edit_task"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_E,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		taskView.addTaskSelectionChangeListener(new TaskSelectionListener() {
			
			@Override
			public void taskSelectionChange(TaskSelectionChangeEvent event) {
				ActionEditTask.this.setEnabled(event.getSelectedTasks().length == 1);
			}
			
		});
		
		this.setEnabled(taskView.getSelectedTasks().length == 1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionEditTask.editTask();
	}
	
	public static void editTask() {
		Task[] tasks = MainFrame.getInstance().getTaskView().getSelectedTasks();
		
		if (tasks.length != 1)
			return;
		
		TaskEditDialog dialog = new TaskEditDialog(
				tasks[0],
				MainFrame.getInstance().getFrame(),
				true);
		dialog.setVisible(true);
	}
	
}
