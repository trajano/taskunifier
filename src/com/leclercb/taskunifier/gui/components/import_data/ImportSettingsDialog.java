package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;
import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Properties;

import com.leclercb.taskunifier.gui.Main;
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
	}
	
}
