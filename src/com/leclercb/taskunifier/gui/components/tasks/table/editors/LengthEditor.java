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

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.api.settings.Settings;

public class LengthEditor extends AbstractCellEditor implements TableCellEditor {

	private JSpinner timeSpinner;

	public LengthEditor() {
		timeSpinner = new JSpinner();
		timeSpinner.setModel(new SpinnerDateModel());
		timeSpinner.setEditor(new JSpinner.DateEditor(
				timeSpinner, 
				Settings.getStringProperty("date.time_format")));
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {
		int hour = 0;
		int minute = 0;

		if (value != null) {
			hour = ((Integer) value) / 60;
			minute = ((Integer) value) % 60;
		}

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(0, 0, 0, hour, minute, 0);

		timeSpinner.setValue(calendar.getTime());

		return timeSpinner;
	}

	@Override
	public Object getCellEditorValue() {
		Date date = (Date) timeSpinner.getValue();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);

		return (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE);
	}

}
