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

		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		label = new JLabel();

		button = new JButton("...");
		button.setActionCommand("BUTTON_CLICK");
		button.addActionListener(this);

		panel.add(label, BorderLayout.CENTER);
		panel.add(button, BorderLayout.EAST);

		dialog = new JDatePicker(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("BUTTON_CLICK")) {
			dialog.setValue(this.value);
			dialog.setLocationRelativeTo(button);
			dialog.setVisible(true);

			if (dialog.getAction() == JDatePicker.Action.OK) {
				value = dialog.getValue();
				this.fireEditingStopped();
			} else {
				this.fireEditingCanceled();
			}
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {
		if (value == null) {
			label.setText("");
		} else {
			label.setText(formatter.format(((Calendar) value).getTime()));
		}

		this.value = (Calendar) value;

		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return value;
	}

}
