/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.import_data;

import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportSettingsDialog extends AbstractImportDialog {
	
	private static ImportSettingsDialog INSTANCE;
	
	public static ImportSettingsDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ImportSettingsDialog();
		
		return INSTANCE;
	}
	
	private ImportSettingsDialog() {
		super(
				Translations.getString("action.import_settings"),
				false,
				"properties",
				Translations.getString("general.properties_files"),
				"import.settings.file_name");
	}
	
	@Override
	protected void deleteExistingValue() {
		
	}
	
	@Override
	protected void importFromFile(String file) throws Exception {
		String[] toImport = new String[] {
				"backup",
				"date",
				"export",
				"general.locale",
				"general.toolbar",
				"import",
				"note",
				"notesearcher",
				"proxy",
				"synchronizer",
				"task",
				"tasksearcher",
				"theme",
				"view" };
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			
			for (int i = 0; i < toImport.length; i++) {
				if (key.startsWith(toImport[i])) {
					Main.getSettings().setStringProperty(key, value);
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
