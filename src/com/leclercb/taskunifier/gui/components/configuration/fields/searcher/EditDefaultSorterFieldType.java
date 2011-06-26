package com.leclercb.taskunifier.gui.components.configuration.fields.searcher;

import java.io.ByteArrayOutputStream;

import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSorterXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;

public class EditDefaultSorterFieldType extends ConfigurationFieldTypeExt.Panel {
	
	private TaskSorter sorter;
	private TaskSorterXMLCoder coder;
	
	public EditDefaultSorterFieldType() {
		super(new TaskSorterPanel(Constants.getDefaultSorter()));
		
		this.sorter = ((TaskSorterPanel) this.getFieldComponent()).getSorter();
		this.coder = new TaskSorterXMLCoder();
	}
	
	@Override
	public void saveAndApplyConfig() {
		String value = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		try {
			this.coder.encode(output, this.sorter);
			value = new String(output.toByteArray());
		} catch (FactoryCoderException e) {
			e.printStackTrace();
		}
		
		Main.SETTINGS.setStringProperty("searcher.default_sorter", value);
	}
	
}
