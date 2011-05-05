package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class LocationEditor extends ComboBoxCellEditor {

	public LocationEditor() {
		super(ComponentFactory.createModelComboBox(new LocationModel(true)));
	}

}
