package com.leclercb.taskunifier.gui.commons.renderers;

import java.awt.Component;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class LocaleListCellRenderer extends DefaultListCellRenderer {
	
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
		
		if (value == null || !(value instanceof Locale)) {
			this.setText("");
			return component;
		}
		
		this.setText(((Locale) value).getDisplayName());
		return component;
	}
	
}
