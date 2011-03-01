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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.gui.images.Images;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class DateEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JTextFieldDateEditor dateEditor;
	private JPanel panel;
	private JDateChooser dateChooser;
	private JButton buttonRemove;
	
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
		
		this.buttonRemove = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.buttonRemove.setActionCommand("REMOVE");
		this.buttonRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				DateEditor.this.dateChooser.setCalendar(null);
				DateEditor.this.fireEditingStopped();
			}
			
		});
		
		FocusListener focusListener = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				buttonRemove.setVisible(buttonRemove.hasFocus() || dateEditor.hasFocus());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				buttonRemove.setVisible(buttonRemove.hasFocus() || dateEditor.hasFocus());
			}
			
		};
		
		this.buttonRemove.addFocusListener(focusListener);
		this.dateEditor.addFocusListener(focusListener);
		
		this.panel.add(this.dateChooser, BorderLayout.CENTER);
		this.panel.add(this.buttonRemove, BorderLayout.EAST);
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
		
		this.buttonRemove.setVisible(false);
		
		return this.panel;
	}
	
	@Override
	public Object getCellEditorValue() {
		this.dateEditor.focusLost(null);
		return this.dateChooser.getCalendar();
	}
	
}
