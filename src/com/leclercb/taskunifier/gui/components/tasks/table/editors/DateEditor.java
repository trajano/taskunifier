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
import java.text.DateFormat;
import java.util.Calendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.gui.swing.JDatePicker;

public class DateEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	
	private DateFormat formatter;
	
	private Calendar value;
	
	private JPanel panel;
	private JLabel label;
	private JButton button;
	private JDatePicker dialog;
	
	public DateEditor(DateFormat formatter) {
		this.formatter = formatter;
		
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		
		this.label = new JLabel();
		
		this.button = new JButton("...");
		this.button.setActionCommand("BUTTON_CLICK");
		this.button.addActionListener(this);
		
		this.panel.add(this.label, BorderLayout.CENTER);
		this.panel.add(this.button, BorderLayout.EAST);
		
		this.dialog = new JDatePicker(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("BUTTON_CLICK")) {
			this.dialog.setValue(this.value);
			this.dialog.setLocationRelativeTo(this.button);
			this.dialog.setVisible(true);
			
			if (this.dialog.getAction() == JDatePicker.Action.OK) {
				this.value = this.dialog.getValue();
				this.fireEditingStopped();
			} else {
				this.fireEditingCanceled();
			}
		}
	}
	
	@Override
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int col) {
		if (value == null) {
			this.label.setText("");
		} else {
			this.label.setText(this.formatter.format(((Calendar) value).getTime()));
		}
		
		this.value = (Calendar) value;
		
		return this.panel;
	}
	
	@Override
	public Object getCellEditorValue() {
		return this.value;
	}
	
}
