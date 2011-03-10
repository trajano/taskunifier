package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportSearchersDialog extends DefaultImportDialog {
	
	public ImportSearchersDialog(Frame frame, boolean modal) {
		super(
				new TaskSearcherFactoryXMLCoder(),
				Translations.getString("general.import_searchers"),
				frame,
				modal);
	}
	
	@Override
	public void deleteExistingValue() {
		TaskSearcherFactory.getInstance().deleteAll();
	}
	
}
