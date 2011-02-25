package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;
import java.io.FileInputStream;

import com.leclercb.commons.api.coder.FactoryCoder;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class DefaultImportDialog extends AbstractImportDialog {
	
	private FactoryCoder coder;
	
	public DefaultImportDialog(
			FactoryCoder coder,
			String title,
			Frame frame,
			boolean modal) {
		super(
				title,
				frame,
				modal,
				"xml",
				Translations.getString("general.xml_files"));
		
		CheckUtils.isNotNull(coder, "Coder cannot be null");
		
		this.coder = coder;
	}
	
	@Override
	protected abstract void deleteExistingValue();
	
	@Override
	protected void importFromFile(String file) throws Exception {
		FileInputStream input = new FileInputStream(file);
		this.coder.decode(input);
	}
	
}
