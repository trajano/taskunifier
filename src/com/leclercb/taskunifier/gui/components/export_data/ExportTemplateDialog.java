package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.template.coder.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportTemplateDialog extends DefaultExportDialog {
	
	public ExportTemplateDialog(Frame frame, boolean modal) {
		super(
				new TemplateFactoryXMLCoder(),
				Translations.getString("general.export_templates"),
				frame,
				modal);
	}
	
}
