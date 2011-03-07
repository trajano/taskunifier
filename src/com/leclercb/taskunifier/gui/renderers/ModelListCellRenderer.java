package com.leclercb.taskunifier.gui.renderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.GuiModel;
import com.leclercb.taskunifier.gui.swing.ColorBadgeIcon;

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
			this.setIcon(null);
			return component;
		}
		
		if (((Model) value).getTitle().length() == 0)
			this.setText(" ");
		else
			this.setText(((Model) value).getTitle());
		
		if (value instanceof GuiModel)
			this.setIcon(new ColorBadgeIcon(
					((GuiModel) value).getColor(),
					12,
					12));
		else
			this.setIcon(new ColorBadgeIcon(null, 12, 12));
		
		return component;
	}
	
}
