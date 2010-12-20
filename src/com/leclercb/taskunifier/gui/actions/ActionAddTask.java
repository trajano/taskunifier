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
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;

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
import com.leclercb.taskunifier.api.settings.Settings;
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
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_T);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Task task = TaskFactory.getInstance().create(
				Translations.getString("task.default.title"));
		
		if (Settings.getStringProperty("task.default.title") != null)
			task.setTitle(Settings.getStringProperty("task.default.title"));
		
		if (Settings.getStringProperty("task.default.tags") != null) {
			String[] tags = (Settings.getStringProperty("task.default.tags")).split(",");
			for (int i = 0; i < tags.length; i++)
				if (tags[i].trim().length() != 0)
					task.addTag(tags[i].trim());
		}
		
		if (Settings.getModelIdProperty("task.default.folder") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.folder");
			task.setFolder(FolderFactory.getInstance().get(modelId));
		}
		
		if (Settings.getModelIdProperty("task.default.context") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.context");
			task.setContext(ContextFactory.getInstance().get(modelId));
		}
		
		if (Settings.getModelIdProperty("task.default.goal") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.goal");
			task.setGoal(GoalFactory.getInstance().get(modelId));
		}
		
		if (Settings.getModelIdProperty("task.default.location") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.location");
			task.setLocation(LocationFactory.getInstance().get(modelId));
		}
		
		if (Settings.getBooleanProperty("task.default.completed") != null)
			task.setCompleted(Settings.getBooleanProperty("task.default.completed"));
		
		if (Settings.getIntegerProperty("task.default.due_date") != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(
					Calendar.DAY_OF_MONTH,
					Settings.getIntegerProperty("task.default.due_date"));
			task.setDueDate(calendar);
		}
		
		if (Settings.getIntegerProperty("task.default.start_date") != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(
					Calendar.DAY_OF_MONTH,
					Settings.getIntegerProperty("task.default.start_date"));
			task.setStartDate(calendar);
		}
		
		if (Settings.getIntegerProperty("task.default.reminder") != null)
			task.setReminder(Settings.getIntegerProperty("task.default.reminder"));
		
		if (Settings.getStringProperty("task.default.repeat") != null)
			task.setRepeat(Settings.getStringProperty("task.default.repeat"));
		
		if (Settings.getStringProperty("task.default.repeat_from") != null)
			task.setRepeatFrom((TaskRepeatFrom) Settings.getEnumProperty(
					"task.default.repeat_from",
					TaskRepeatFrom.class));
		
		if (Settings.getEnumProperty("task.default.status", TaskStatus.class) != null)
			task.setStatus((TaskStatus) Settings.getEnumProperty(
					"task.default.status",
					TaskStatus.class));
		
		if (Settings.getIntegerProperty("task.default.length") != null)
			task.setLength(Settings.getIntegerProperty("task.default.length"));
		
		if (Settings.getEnumProperty(
				"task.default.priority",
				TaskPriority.class) != null)
			task.setPriority((TaskPriority) Settings.getEnumProperty(
					"task.default.priority",
					TaskPriority.class));
		
		if (Settings.getBooleanProperty("task.default.star") != null)
			task.setStar(Settings.getBooleanProperty("task.default.star"));
		
		if (Settings.getStringProperty("task.default.note") != null)
			task.setNote(Settings.getStringProperty("task.default.note"));
	}
	
}
