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
package com.leclercb.taskunifier.gui.components.configuration.api;

import com.leclercb.taskunifier.api.utils.CheckUtils;

public class ConfigurationField {

	private String id;
	private String label;
	private ConfigurationFieldType<?, ?> type;

	public ConfigurationField(String id, String label, ConfigurationFieldType<?, ?> type) {
		this.setId(id);
		this.setLabel(label);
		this.setType(type);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	private void setLabel(String label) {
		this.label = label;
	}

	public ConfigurationFieldType<?, ?> getType() {
		return type;
	}

	private void setType(ConfigurationFieldType<?, ?> type) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		this.type = type;
	}

}
