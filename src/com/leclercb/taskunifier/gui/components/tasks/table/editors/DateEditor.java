/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.leclercb.taskunifier.gui.utils.Images;
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
				10,
				10));
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
