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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUPostponeCalendar;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class DateEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JTextFieldDateEditor dateEditor;
	private JDateChooser dateChooser;
	
	public DateEditor(boolean showTime) {
		String dateFormat = Main.getSettings().getStringProperty(
				"date.date_format");
		String timeFormat = Main.getSettings().getStringProperty(
				"date.time_format");
		String format = null;
		String mask = null;
		
		if (showTime) {
			format = dateFormat + " " + timeFormat;
			mask = DateTimeFormatUtils.getMask(dateFormat)
					+ " "
					+ DateTimeFormatUtils.getMask(timeFormat);
		} else {
			format = dateFormat;
			mask = DateTimeFormatUtils.getMask(dateFormat);
		}
		
		final String finalMask = mask;
		
		this.dateEditor = new JTextFieldDateEditor(format, null, '_') {
			
			@Override
			public String createMaskFromDatePattern(String datePattern) {
				return finalMask;
			}
			
		};
		
		TUPostponeCalendar calendar = new TUPostponeCalendar(true);
		calendar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (EqualsUtils.equals(
						TUPostponeCalendar.NO_DATE_COMMAND,
						evt.getActionCommand())) {
					DateEditor.this.dateChooser.setCalendar(null);
					DateEditor.this.fireEditingStopped();
				}
			}
			
		});
		
		this.dateChooser = new JDateChooser(
				calendar,
				null,
				null,
				this.dateEditor);
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
		
		return this.dateChooser;
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
