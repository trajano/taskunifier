package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportModelsDialog extends AbstractExportDialog {
	
	public ExportModelsDialog(Frame frame, boolean modal) {
		super(
				Translations.getString("general.export_templates"),
				frame,
				modal,
				"zip",
				Translations.getString("general.zip_files"));
	}
	
	@Override
	protected void exportToFile(String file) throws Exception {
		// TODO Auto-generated method stub
	}
	
}
