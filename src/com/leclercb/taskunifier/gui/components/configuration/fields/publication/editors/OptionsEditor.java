package com.leclercb.taskunifier.gui.components.configuration.fields.publication.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

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
		SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) value;
		
		JButton button = new JButton("Options");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.out.println("test");
			}
			
		});
		
		return button;
	}
	
}
