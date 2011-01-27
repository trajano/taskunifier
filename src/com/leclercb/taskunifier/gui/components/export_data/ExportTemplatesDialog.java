package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.template.coder.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportTemplatesDialog extends DefaultExportDialog {
	
	public ExportTemplatesDialog(Frame frame, boolean modal) {
		super(
				new TemplateFactoryXMLCoder(true),
				Translations.getString("general.export_templates"),
				frame,
				modal);
	}
	
}
