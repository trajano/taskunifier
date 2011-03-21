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
package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;
import java.io.FileOutputStream;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportSettingsDialog extends AbstractExportDialog {
	
	public ExportSettingsDialog(Frame frame, boolean modal) {
		super(
				Translations.getString("general.export_settings"),
				frame,
				modal,
				"properties",
				Translations.getString("general.properties_files"));
	}
	
	@Override
	protected void exportToFile(String file) throws Exception {
		Main.SETTINGS.store(new FileOutputStream(file), "Settings");
	}
	
}
