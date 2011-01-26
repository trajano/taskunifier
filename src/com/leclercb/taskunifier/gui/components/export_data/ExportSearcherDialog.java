package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.searchers.coder.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportSearcherDialog extends DefaultExportDialog {
	
	public ExportSearcherDialog(Frame frame, boolean modal) {
		super(
				new TaskSearcherFactoryXMLCoder(),
				Translations.getString("general.export_searchers"),
				frame,
				modal);
	}
	
}
