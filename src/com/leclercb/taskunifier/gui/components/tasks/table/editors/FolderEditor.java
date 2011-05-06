package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class FolderEditor extends ComboBoxCellEditor {
	
	public FolderEditor() {
		super(ComponentFactory.createModelComboBox(new FolderModel(true)));
	}
	
}
