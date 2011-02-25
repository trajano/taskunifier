package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportModelsDialog extends AbstractImportDialog {
	
	public ImportModelsDialog(Frame frame, boolean modal) {
		super(
				Translations.getString("general.import_searchers"),
				frame,
				modal,
				"zip",
				Translations.getString("general.zip_files"));
	}
	
	@Override
	protected void deleteExistingValue() {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void importFromFile(String file) throws Exception {
		// TODO Auto-generated method stub
	}
	
}
