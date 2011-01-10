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

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ColumnsConfigurationPanel extends ConfigurationPanel {
	
	public ColumnsConfigurationPanel() {
		this.initialize();
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		for (ConfigurationField field : this.getFields()) {
			if (!(field.getType() instanceof ConfigurationFieldType.CheckBox))
				continue;
			
			TaskColumn.valueOf(field.getId()).setVisible(
					(Boolean) this.getValue(field.getId()));
		}
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"LABEL",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("configuration.columns.right_click_column_title"))));
		
		for (TaskColumn taskColumn : TaskColumn.values()) {
			this.addField(new ConfigurationField(
					taskColumn.name(),
					taskColumn.getLabel(),
					new ConfigurationFieldType.CheckBox(taskColumn.isVisible())));
		}
	}
	
}
