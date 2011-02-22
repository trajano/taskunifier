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

import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.swing.JDatePicker;

public class DateEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	
	private DateFormat formatter;
	
	private Calendar value;
	
	private JPanel panel;
	private JLabel label;
	private JButton buttonSet;
	private JButton buttonRemove;
	private JDatePicker dialog;
	
	public DateEditor(DateFormat formatter) {
		this.formatter = formatter;
		
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		
		this.label = new JLabel();
		
		this.buttonSet = new JButton(Images.getResourceImage(
				"calendar.png",
				16,
				16));
		this.buttonSet.setActionCommand("SET");
		this.buttonSet.addActionListener(this);
		
		this.buttonRemove = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.buttonRemove.setActionCommand("REMOVE");
		this.buttonRemove.addActionListener(this);
		
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.add(this.buttonSet, BorderLayout.WEST);
		buttonsPanel.add(this.buttonRemove, BorderLayout.EAST);
		
		this.panel.add(this.label, BorderLayout.CENTER);
		this.panel.add(buttonsPanel, BorderLayout.EAST);
		
		this.dialog = new JDatePicker(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("SET")) {
			this.dialog.setValue(this.value);
			this.dialog.setLocationRelativeTo(this.buttonSet);
			this.dialog.setVisible(true);
			
			if (this.dialog.getAction() == JDatePicker.Action.OK) {
				this.value = this.dialog.getValue();
				this.fireEditingStopped();
			} else {
				this.fireEditingCanceled();
			}
		} else if (event.getActionCommand().equals("REMOVE")) {
			this.value = null;
			this.fireEditingStopped();
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
		
		this.buttonRemove.setVisible(this.value != null);
		
		return this.panel;
	}
	
	@Override
	public Object getCellEditorValue() {
		return this.value;
	}
	
}
