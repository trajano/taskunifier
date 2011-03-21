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
package com.leclercb.taskunifier.gui.components.plugins.exc;

import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginException extends Exception {
	
	public static enum PluginExceptionType {
		
		ERROR_LOADING_PLUGIN_DB(Translations.getString("errors.cannot_load_plugin_database")),
		ERROR_LOADING_PLUGIN(Translations.getString("error.cannot_install_plugin")),
		NO_VALID_PLUGIN(Translations.getString("error.no_valid_plugin")),
		MORE_THAN_ONE_PLUGIN(Translations.getString("error.more_than_one_plugin")),
		OUTDATED_PLUGIN(Translations.getString("error.plugin_not_up_to_date")),
		PLUGIN_FOUND(Translations.getString("error.plugin_already_installed"));
		
		private String label;
		
		private PluginExceptionType(String label) {
			this.label = label;
		}
		
		@Override
		public String toString() {
			return this.label;
		}
		
	}
	
	private PluginExceptionType type;
	
	public PluginException(PluginExceptionType type) {
		super(type.toString());
		
		this.type = type;
	}
	
	public PluginExceptionType getType() {
		return this.type;
	}
	
}
