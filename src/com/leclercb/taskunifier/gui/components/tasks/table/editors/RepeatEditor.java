/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class RepeatEditor extends DefaultCellEditor {
	
	public RepeatEditor() {
		super(new JTextField());
		
		final JTextField repeatField = (JTextField) this.getComponent();
		repeatField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (SynchronizerUtils.getPlugin().getSynchronizerApi().isValidRepeatValue(
						repeatField.getText()))
					repeatField.setForeground(Color.BLACK);
				else
					repeatField.setForeground(Color.RED);
			}
			
		});
	}
	
	@Override
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int col) {
		Component component = super.getTableCellEditorComponent(
				table,
				value,
				isSelected,
				row,
				col);
		
		if (SynchronizerUtils.getPlugin().getSynchronizerApi().isValidRepeatValue(
				(this.getCellEditorValue() == null ? null : this.getCellEditorValue().toString())))
			component.setForeground(Color.BLACK);
		else
			component.setForeground(Color.RED);
		
		return component;
	}
}
