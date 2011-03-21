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
package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;
import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportSettingsDialog extends AbstractImportDialog {
	
	public ImportSettingsDialog(Frame frame, boolean modal) {
		super(
				Translations.getString("general.import_settings"),
				frame,
				modal,
				false,
				"properties",
				Translations.getString("general.properties_files"));
	}
	
	@Override
	protected void deleteExistingValue() {

	}
	
	@Override
	protected void importFromFile(String file) throws Exception {
		// TODO: keep up to date
		String[] toImport = new String[] {
				"date",
				"general.locale",
				"proxy",
				"searcher",
				"synchronizer",
				"taskcolumn",
				"theme" };
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			
			for (int i = 0; i < toImport.length; i++) {
				if (key.startsWith(toImport[i])) {
					Main.SETTINGS.setStringProperty(key, value);
					break;
				}
			}
		}
		
		JOptionPane.showMessageDialog(
				MainFrame.getInstance().getFrame(),
				Translations.getString("configuration.general.settings_changed_after_restart"),
				Translations.getString("general.information"),
				JOptionPane.INFORMATION_MESSAGE);
	}
	
}
