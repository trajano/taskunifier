package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;
import java.util.List;

import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.template.TemplateFactory;
import com.leclercb.taskunifier.gui.template.coder.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportTemplateDialog extends AbstractImportDialog {
	
	public ImportTemplateDialog(Frame frame, boolean modal) {
		super(
				new TemplateFactoryXMLCoder(),
				Translations.getString("general.import_templates"),
				frame,
				modal);
	}
	
	@Override
	public void deleteExistingValue() {
		List<Template> existingTemplates = TemplateFactory.getInstance().getList();
		
		for (Template template : existingTemplates) {
			TemplateFactory.getInstance().delete(template);
		}
	}
	
}
