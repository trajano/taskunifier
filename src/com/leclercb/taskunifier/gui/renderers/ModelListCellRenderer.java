package com.leclercb.taskunifier.gui.renderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.leclercb.taskunifier.api.models.Model;

public class ModelListCellRenderer extends DefaultListCellRenderer {
	
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(
				list,
				value,
				index,
				isSelected,
				cellHasFocus);
		
		if (value == null || !(value instanceof Model)) {
			this.setText(" ");
			return component;
		}
		
		if (((Model) value).getTitle().length() == 0)
			this.setText(" ");
		else
			this.setText(((Model) value).getTitle());
		
		return component;
	}
	
}
