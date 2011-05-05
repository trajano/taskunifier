package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class GoalEditor extends ComboBoxCellEditor {

	public GoalEditor() {
		super(ComponentFactory.createModelComboBox(new GoalModel(true)));
	}

}
