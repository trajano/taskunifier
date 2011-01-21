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
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

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
		MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
		
		Task task = TaskFactory.getInstance().create(
				Translations.getString("task.default.title"));
		
		if (Main.SETTINGS.getStringProperty("task.default.title") != null)
			task.setTitle(Main.SETTINGS.getStringProperty("task.default.title"));
		
		if (Main.SETTINGS.getStringProperty("task.default.tags") != null) {
			task.setTags(Main.SETTINGS.getStringProperty("task.default.tags").split(
					","));
		}
		
		if (Main.SETTINGS.getObjectProperty(
				"task.default.folder",
				ModelId.class) != null) {
			ModelId modelId = Main.SETTINGS.getObjectProperty(
					"task.default.folder",
					ModelId.class);
			task.setFolder(FolderFactory.getInstance().get(modelId));
		}
		
		if (Main.SETTINGS.getObjectProperty(
				"task.default.context",
				ModelId.class) != null) {
			ModelId modelId = Main.SETTINGS.getObjectProperty(
					"task.default.context",
					ModelId.class);
			task.setContext(ContextFactory.getInstance().get(modelId));
		}
		
		if (Main.SETTINGS.getObjectProperty("task.default.goal", ModelId.class) != null) {
			ModelId modelId = Main.SETTINGS.getObjectProperty(
					"task.default.goal",
					ModelId.class);
			task.setGoal(GoalFactory.getInstance().get(modelId));
		}
		
		if (Main.SETTINGS.getObjectProperty(
				"task.default.location",
				ModelId.class) != null) {
			ModelId modelId = Main.SETTINGS.getObjectProperty(
					"task.default.location",
					ModelId.class);
			task.setLocation(LocationFactory.getInstance().get(modelId));
		}
		
		if (Main.SETTINGS.getBooleanProperty("task.default.completed") != null)
			task.setCompleted(Main.SETTINGS.getBooleanProperty("task.default.completed"));
		
		if (Main.SETTINGS.getIntegerProperty("task.default.due_date") != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(
					Calendar.DAY_OF_MONTH,
					Main.SETTINGS.getIntegerProperty("task.default.due_date"));
			task.setDueDate(calendar);
		}
		
		if (Main.SETTINGS.getIntegerProperty("task.default.start_date") != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(
					Calendar.DAY_OF_MONTH,
					Main.SETTINGS.getIntegerProperty("task.default.start_date"));
			task.setStartDate(calendar);
		}
		
		if (Main.SETTINGS.getIntegerProperty("task.default.reminder") != null)
			task.setReminder(Main.SETTINGS.getIntegerProperty("task.default.reminder"));
		
		if (Main.SETTINGS.getStringProperty("task.default.repeat") != null)
			task.setRepeat(Main.SETTINGS.getStringProperty("task.default.repeat"));
		
		if (Main.SETTINGS.getStringProperty("task.default.repeat_from") != null)
			task.setRepeatFrom((TaskRepeatFrom) Main.SETTINGS.getEnumProperty(
					"task.default.repeat_from",
					TaskRepeatFrom.class));
		
		if (Main.SETTINGS.getEnumProperty(
				"task.default.status",
				TaskStatus.class) != null)
			task.setStatus((TaskStatus) Main.SETTINGS.getEnumProperty(
					"task.default.status",
					TaskStatus.class));
		
		if (Main.SETTINGS.getIntegerProperty("task.default.length") != null)
			task.setLength(Main.SETTINGS.getIntegerProperty("task.default.length"));
		
		if (Main.SETTINGS.getEnumProperty(
				"task.default.priority",
				TaskPriority.class) != null)
			task.setPriority((TaskPriority) Main.SETTINGS.getEnumProperty(
					"task.default.priority",
					TaskPriority.class));
		
		if (Main.SETTINGS.getBooleanProperty("task.default.star") != null)
			task.setStar(Main.SETTINGS.getBooleanProperty("task.default.star"));
		
		if (Main.SETTINGS.getStringProperty("task.default.note") != null)
			task.setNote(Main.SETTINGS.getStringProperty("task.default.note"));
		
		MainFrame.getInstance().getTaskView().setSelectedTasks(
				new Task[] { task });
	}
}
