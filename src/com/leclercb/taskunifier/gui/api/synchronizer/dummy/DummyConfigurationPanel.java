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
package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class DummyConfigurationPanel extends DefaultConfigurationPanel {
	
	public DummyConfigurationPanel(boolean welcome) {
		super();
		this.initialize(welcome);
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {

	}
	
	private void initialize(boolean welcome) {
		this.addField(new ConfigurationField(
				"NO_CONFIG",
				null,
				new ConfigurationFieldType.Label(
						Translations.getString("general.no_configuration_values"))));
	}
	
}
