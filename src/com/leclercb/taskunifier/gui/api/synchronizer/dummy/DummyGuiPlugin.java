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

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;

public class DummyGuiPlugin extends DummyPlugin implements SynchronizerGuiPlugin {
	
	private static DummyGuiPlugin INSTANCE;
	
	public static DummyGuiPlugin getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DummyGuiPlugin();
		
		return INSTANCE;
	}
	
	private static String VERSION = "1.0";
	
	private DummyGuiPlugin() {

	}
	
	@Override
	public int getPluginApiVersion() {
		return Constants.PLUGIN_API_VERSION;
	}
	
	@Override
	public String getVersion() {
		return VERSION;
	}
	
	@Override
	public ConfigurationPanel getConfigurationPanel(boolean welcome) {
		return new DummyConfigurationPanel(welcome);
	}
	
	@Override
	public boolean needsLicense() {
		return false;
	}
	
	@Override
	public boolean checkLicense() throws SynchronizerLicenseException {
		return true;
	}
	
	@Override
	public Connection getConnection() throws SynchronizerException {
		throw new SynchronizerException(
				true,
				Translations.getString("synchronizer.select_an_api"));
	}
	
}
