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
package com.leclercb.taskunifier.gui.components.tasknote;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskNotePanel extends JTextArea implements TaskSelectionListener {
	
	private Task previousSelectedTask;
	
	public TaskNotePanel() {
		this.previousSelectedTask = null;
		this.initialize();
	}
	
	public String getTaskNote() {
		return this.getText();
	}
	
	private void initialize() {
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setText(Translations.getString("error.select_one_task"));
		this.setEnabled(false);
		this.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (TaskNotePanel.this.previousSelectedTask != null) {
					if (!EqualsUtils.equals(
							TaskNotePanel.this.previousSelectedTask.getNote(),
							TaskNotePanel.this.getText()))
						TaskNotePanel.this.previousSelectedTask.setNote(TaskNotePanel.this.getText());
				}
			}
			
		});
	}
	
	@Override
	public void taskSelectionChange(TaskSelectionChangeEvent event) {
		if (this.previousSelectedTask != null) {
			if (!EqualsUtils.equals(
					this.previousSelectedTask.getNote(),
					this.getText()))
				this.previousSelectedTask.setNote(this.getText());
		}
		
		Task[] tasks = event.getSelectedTasks();
		
		if (tasks.length != 1) {
			this.previousSelectedTask = null;
			
			this.setText(Translations.getString("error.select_one_task"));
			this.setEnabled(false);
		} else {
			this.previousSelectedTask = tasks[0];
			
			this.setText((tasks[0].getNote() == null ? "" : tasks[0].getNote()));
			this.setEnabled(true);
		}
	}
	
}
