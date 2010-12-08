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
package com.leclercb.taskunifier.gui.components.configuration;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.models.ContextComboxBoxModel;
import com.leclercb.taskunifier.gui.models.FolderComboxBoxModel;
import com.leclercb.taskunifier.gui.models.GoalComboxBoxModel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.RegexFormatter;

public class TaskConfigurationPanel extends ConfigurationPanel {

	public TaskConfigurationPanel() {
		this.initialize();
		this.pack();
	}

	@Override
	public void saveAndApplyConfig() {
		ModelId folder = null;
		if (this.getValue("FOLDER") != null)
			folder = ((Folder) this.getValue("FOLDER")).getModelId();

		ModelId context = null;
		if (this.getValue("CONTEXT") != null)
			context = ((Context) this.getValue("CONTEXT")).getModelId();

		ModelId goal = null;
		if (this.getValue("GOAL") != null)
			goal = ((Goal) this.getValue("GOAL")).getModelId();

		Settings.setStringProperty("task.default.title", (String) this.getValue("TITLE"));
		Settings.setStringProperty("task.default.tags", (String) this.getValue("TAGS"));
		Settings.setModelIdProperty("task.default.folder", folder);
		Settings.setModelIdProperty("task.default.context", context);
		Settings.setModelIdProperty("task.default.goal", goal);
		Settings.setBooleanProperty("task.default.completed", (Boolean) this.getValue("COMPLETED"));
		Settings.setStringProperty("task.default.due_date", (String) this.getValue("DUE_DATE"));
		Settings.setStringProperty("task.default.start_date", (String) this.getValue("START_DATE"));
		Settings.setStringProperty("task.default.reminder", (String) this.getValue("REMINDER"));
		Settings.setStringProperty("task.default.repeat", (String) this.getValue("REPEAT"));
		Settings.setEnumProperty("task.default.status", (TaskStatus) this.getValue("STATUS"));
		Settings.setEnumProperty("task.default.priority", (TaskPriority) this.getValue("PRIORITY"));
		Settings.setBooleanProperty("task.default.star", (Boolean) this.getValue("STAR"));
		Settings.setStringProperty("task.default.note", (String) this.getValue("NOTE"));
	}

	private void initialize() {
		String taskTitleValue = "Untitled";
		String taskTagsValue = "";
		Folder taskFolderValue = null;
		Context taskContextValue = null;
		Goal taskGoalValue = null;
		Boolean taskCompletedValue = false;
		String taskDueDateValue = "";
		String taskStartDateValue = "";
		String taskReminderValue = "";
		String taskRepeatValue = "";
		TaskStatus taskStatusValue = TaskStatus.NONE;
		TaskPriority taskPriorityValue = TaskPriority.LOW;
		Boolean taskStarValue = false;
		String taskNoteValue = null;

		if (Settings.getStringProperty("task.default.title") != null)
			taskTitleValue = Settings.getStringProperty("task.default.title");

		if (Settings.getStringProperty("task.default.tags") != null)
			taskTagsValue = Settings.getStringProperty("task.default.tags");

		if (Settings.getModelIdProperty("task.default.folder") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.folder");
			taskFolderValue = FolderFactory.getInstance().get(modelId);
		}

		if (Settings.getModelIdProperty("task.default.context") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.context");
			taskContextValue = ContextFactory.getInstance().get(modelId);
		}

		if (Settings.getModelIdProperty("task.default.goal") != null) {
			ModelId modelId = Settings.getModelIdProperty("task.default.goal");
			taskGoalValue = GoalFactory.getInstance().get(modelId);
		}

		if (Settings.getBooleanProperty("task.default.completed") != null)
			taskCompletedValue = Settings.getBooleanProperty("task.default.completed");

		if (Settings.getIntegerProperty("task.default.due_date") != null)
			taskDueDateValue = Settings.getStringProperty("task.default.due_date");

		if (Settings.getIntegerProperty("task.default.start_date") != null)
			taskStartDateValue = Settings.getStringProperty("task.default.start_date");

		if (Settings.getIntegerProperty("task.default.reminder") != null)
			taskReminderValue = Settings.getStringProperty("task.default.reminder");

		if (Settings.getStringProperty("task.default.repeat") != null)
			taskRepeatValue = Settings.getStringProperty("task.default.repeat");

		if (Settings.getEnumProperty("task.default.status", TaskStatus.class) != null)
			taskStatusValue = (TaskStatus) Settings.getEnumProperty("task.default.status", TaskStatus.class);

		if (Settings.getEnumProperty("task.default.priority", TaskPriority.class) != null)
			taskPriorityValue = (TaskPriority) Settings.getEnumProperty("task.default.priority", TaskPriority.class);

		if (Settings.getBooleanProperty("task.default.star") != null)
			taskStarValue = Settings.getBooleanProperty("task.default.star");

		if (Settings.getStringProperty("task.default.note") != null)
			taskNoteValue = Settings.getStringProperty("task.default.note");

		this.addField(new ConfigurationField(
				"DEFAULT_VALUE",
				null,
				new ConfigurationFieldType.Label(Translations.getString("configuration.task.default_value"))));

		this.addField(new ConfigurationField(
				"TITLE", 
				Translations.getString("general.task.title"), 
				new ConfigurationFieldType.TextField(taskTitleValue)));

		this.addField(new ConfigurationField(
				"TAGS", 
				Translations.getString("general.task.tags"), 
				new ConfigurationFieldType.TextField(taskTagsValue)));

		this.addField(new ConfigurationField(
				"FOLDER", 
				Translations.getString("general.task.folder"), 
				new ConfigurationFieldType.ComboBox(new FolderComboxBoxModel(), taskFolderValue)));

		this.addField(new ConfigurationField(
				"CONTEXT", 
				Translations.getString("general.task.context"), 
				new ConfigurationFieldType.ComboBox(new ContextComboxBoxModel(), taskContextValue)));

		this.addField(new ConfigurationField(
				"GOAL", 
				Translations.getString("general.task.goal"), 
				new ConfigurationFieldType.ComboBox(new GoalComboxBoxModel(), taskGoalValue)));

		this.addField(new ConfigurationField(
				"COMPLETED", 
				Translations.getString("general.task.completed"), 
				new ConfigurationFieldType.CheckBox(taskCompletedValue)));

		this.addField(new ConfigurationField(
				"DUE_DATE", 
				Translations.getString("general.task.due_date"), 
				new ConfigurationFieldType.FormattedTextField(new RegexFormatter("^[0-9]{0,3}$"), taskDueDateValue)));

		this.addField(new ConfigurationField(
				"START_DATE", 
				Translations.getString("general.task.start_date"), 
				new ConfigurationFieldType.FormattedTextField(new RegexFormatter("^[0-9]{0,3}$"), taskStartDateValue)));

		this.addField(new ConfigurationField(
				"REMINDER", 
				Translations.getString("general.task.reminder"), 
				new ConfigurationFieldType.FormattedTextField(new RegexFormatter("^[0-9]{0,3}$"), taskReminderValue)));

		this.addField(new ConfigurationField(
				"REPEAT", 
				Translations.getString("general.task.repeat"), 
				new ConfigurationFieldType.TextField(taskRepeatValue)));

		this.addField(new ConfigurationField(
				"STATUS", 
				Translations.getString("general.task.status"), 
				new ConfigurationFieldType.ComboBox(TaskStatus.values(), taskStatusValue)));

		this.addField(new ConfigurationField(
				"PRIORITY", 
				Translations.getString("general.task.priority"), 
				new ConfigurationFieldType.ComboBox(TaskPriority.values(), taskPriorityValue)));

		this.addField(new ConfigurationField(
				"STAR", 
				Translations.getString("general.task.star"), 
				new ConfigurationFieldType.StarCheckBox(taskStarValue)));

		this.addField(new ConfigurationField(
				"NOTE", 
				Translations.getString("general.task.note"), 
				new ConfigurationFieldType.TextArea(taskNoteValue)));
	}

}
