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
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionAddTask extends AbstractAction {
	
	public ActionAddTask() {
		this(32, 32);
	}
	
	public ActionAddTask(int width, int height) {
		super(
				Translations.getString("action.name.add_task"),
				Images.getResourceImage("document.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.add_task"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Task task = TaskFactory.getInstance().create("");
		Template template = TemplateFactory.getInstance().getDefaultTemplate();
		
		if (template != null)
			template.applyToTask(task);
		
		template = MainFrame.getInstance().getSearcherView().getSelectedTaskSearcher().getTemplate();
		
		if (template != null)
			template.applyToTask(task);
		
		MainFrame.getInstance().getTaskView().refreshTasks();
		
		if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add") != null
				&& Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add")) {
			TaskEditDialog dialog = new TaskEditDialog(
					task,
					MainFrame.getInstance().getFrame(),
					true);
			dialog.setVisible(true);
		} else {
			if (template == null)
				MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
			
			MainFrame.getInstance().getTaskView().setSelectedTaskAndStartEdit(
					task);
		}
	}
	
}
