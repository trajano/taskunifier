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
