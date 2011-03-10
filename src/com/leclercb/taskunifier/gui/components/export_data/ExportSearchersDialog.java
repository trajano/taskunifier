package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportSearchersDialog extends DefaultExportDialog {
	
	public ExportSearchersDialog(Frame frame, boolean modal) {
		super(
				new TaskSearcherFactoryXMLCoder(),
				Translations.getString("general.export_searchers"),
				frame,
				modal);
	}
	
}
