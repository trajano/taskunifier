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
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellEditor;

public class LengthEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JSpinner timeSpinner;
	
	public LengthEditor() {
		this.timeSpinner = new JSpinner();
		this.timeSpinner.setModel(new SpinnerDateModel());
		this.timeSpinner.setEditor(new JSpinner.DateEditor(
				this.timeSpinner,
				"HH:mm"));
	}
	
	@Override
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int col) {
		int hour = 0;
		int minute = 0;
		
		if (value != null) {
			hour = ((Integer) value) / 60;
			minute = ((Integer) value) % 60;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(0, 0, 0, hour, minute, 0);
		
		this.timeSpinner.setValue(calendar.getTime());
		
		return this.timeSpinner;
	}
	
	@Override
	public Object getCellEditorValue() {
		try {
			this.timeSpinner.commitEdit();
		} catch (ParseException e) {}
		
		Date date = (Date) this.timeSpinner.getValue();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return (calendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ calendar.get(Calendar.MINUTE);
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
