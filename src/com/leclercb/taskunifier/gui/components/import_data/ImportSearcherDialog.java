package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;
import java.util.List;

import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searchers.coder.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportSearcherDialog extends AbstractImportDialog {
	
	public ImportSearcherDialog(Frame frame, boolean modal) {
		super(
				new TaskSearcherFactoryXMLCoder(),
				Translations.getString("general.import_searchers"),
				frame,
				modal);
	}
	
	@Override
	public void deleteExistingValue() {
		List<TaskSearcher> existingSearchers = TaskSearcherFactory.getInstance().getList();
		
		for (TaskSearcher searcher : existingSearchers) {
			TaskSearcherFactory.getInstance().delete(searcher);
		}
	}
	
}
