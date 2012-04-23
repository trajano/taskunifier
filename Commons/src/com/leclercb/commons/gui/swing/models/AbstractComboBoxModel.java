package com.leclercb.commons.gui.swing.models;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import com.leclercb.commons.api.utils.EqualsUtils;

public abstract class AbstractComboBoxModel extends AbstractListModel implements ComboBoxModel {
	
	private Object selectedObject;
	
	public AbstractComboBoxModel() {
		this.selectedObject = null;
	}
	
	@Override
	public Object getSelectedItem() {
		return this.selectedObject;
	}
	
	@Override
	public void setSelectedItem(Object anObject) {
		if (!EqualsUtils.equals(this.selectedObject, anObject)) {
			this.selectedObject = anObject;
			this.fireContentsChanged(this, -1, -1);
		}
	}
	
}
