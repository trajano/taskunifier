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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.swing.JTextAreaDialog;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionBatchAddTasks extends AbstractAction {
	
	public ActionBatchAddTasks() {
		this(32, 32);
	}
	
	public ActionBatchAddTasks(int width, int height) {
		super(
				Translations.getString("action.name.batch_add_tasks"),
				Images.getResourceImage("document.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.batch_add_tasks"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Task[] selectedTasks = MainFrame.getInstance().getTaskView().getSelectedTasks();
		
		if (selectedTasks.length != 1) {
			ErrorDialog errorDialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("error.select_one_task"));
			errorDialog.setVisible(true);
			return;
		}
		
		MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
		
		JTextAreaDialog dialog = new JTextAreaDialog(
				MainFrame.getInstance().getFrame(),
				Translations.getString("action.name.batch_add_tasks"),
				Translations.getString("action.batch_add_tasks.insert_task_titles"));
		
		dialog.setVisible(true);
		
		if (dialog.getAnswer() == null)
			return;
		
		String titles = dialog.getAnswer();
		
		List<Task> tasks = new ArrayList<Task>();
		for (String title : titles.split("\n")) {
			title = title.trim();
			
			if (title.length() == 0)
				continue;
			
			Task task = TaskFactory.getInstance().create(selectedTasks[0]);
			task.setTitle(title);
			
			tasks.add(task);
		}
		
		MainFrame.getInstance().getTaskView().setSelectedTasks(
				tasks.toArray(new Task[0]));
	}
	
}
