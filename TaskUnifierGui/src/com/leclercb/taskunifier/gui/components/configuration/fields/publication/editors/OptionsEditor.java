package com.leclercb.taskunifier.gui.components.configuration.fields.publication.editors;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.gui.actions.ActionPluginConfiguration;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;

public class OptionsEditor extends AbstractCellEditor implements TableCellEditor {
	
	@Override
	public Object getCellEditorValue() {
		return null;
	}
	
	@Override
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
		final SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) value;
		return new JButton(new ActionPluginConfiguration(24, 24, plugin));
	}
	
}
