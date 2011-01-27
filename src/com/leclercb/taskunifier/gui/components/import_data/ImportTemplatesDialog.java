package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.template.TemplateFactory;
import com.leclercb.taskunifier.gui.template.coder.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportTemplatesDialog extends AbstractImportDialog {
	
	public ImportTemplatesDialog(Frame frame, boolean modal) {
		super(
				new TemplateFactoryXMLCoder(true),
				Translations.getString("general.import_templates"),
				frame,
				modal);
	}
	
	@Override
	public void deleteExistingValue() {
		TemplateFactory.getInstance().deleteAll();
	}
	
}
