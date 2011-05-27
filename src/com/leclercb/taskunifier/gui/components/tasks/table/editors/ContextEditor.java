package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ContextEditor extends ComboBoxCellEditor {
	
	public ContextEditor() {
		super(ComponentFactory.createModelComboBox(new ContextModel(true)));
	}
	
}
