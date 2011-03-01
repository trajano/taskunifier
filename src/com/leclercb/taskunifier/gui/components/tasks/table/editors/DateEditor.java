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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Calendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class DateEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JTextFieldDateEditor dateEditor;
	private JPanel panel;
	private JDateChooser dateChooser;
	
	public DateEditor(final String format, final String mask) {
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		
		this.dateEditor = new JTextFieldDateEditor(format, null, '_') {
			
			@Override
			public String createMaskFromDatePattern(String datePattern) {
				return mask;
			}
			
		};
		
		this.dateChooser = new JDateChooser(this.dateEditor);
		
		this.panel.add(this.dateChooser, BorderLayout.CENTER);
	}
	
	@Override
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int col) {
		if (value == null) {
			this.dateChooser.setCalendar(null);
		} else {
			this.dateChooser.setCalendar((Calendar) value);
		}
		
		return this.panel;
	}
	
	@Override
	public Object getCellEditorValue() {
		this.dateEditor.focusLost(null);
		return this.dateChooser.getCalendar();
	}
	
}
