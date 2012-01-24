package com.leclercb.taskunifier.gui.components.configuration.fields.publication.renderers;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class OptionsRenderer extends JButton implements TableCellRenderer {
	
	public OptionsRenderer() {
		super("Options");
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		return this;
	}
	
}
