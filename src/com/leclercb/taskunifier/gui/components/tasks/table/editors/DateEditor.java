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
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class DateEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JTextFieldDateEditor dateEditor;
	private JPanel panel;
	private JDateChooser dateChooser;
	private JButton buttonRemove;
	
	public DateEditor() {
		final String dateFormat = Main.SETTINGS.getStringProperty("date.date_format");
		final String timeFormat = Main.SETTINGS.getStringProperty("date.time_format");
		final String format = dateFormat + " " + timeFormat;
		final String mask = DateTimeFormatUtils.getMask(dateFormat)
				+ " "
				+ DateTimeFormatUtils.getMask(timeFormat);
		
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
				DateEditor.this.buttonRemove.setVisible(DateEditor.this.buttonRemove.hasFocus()
						|| DateEditor.this.dateEditor.hasFocus());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				DateEditor.this.buttonRemove.setVisible(DateEditor.this.buttonRemove.hasFocus()
						|| DateEditor.this.dateEditor.hasFocus());
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
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			MouseEvent event = (MouseEvent) anEvent;
			
			if (event.getClickCount() != 1)
				return false;
		}
		
		return super.isCellEditable(anEvent);
	}
	
}
