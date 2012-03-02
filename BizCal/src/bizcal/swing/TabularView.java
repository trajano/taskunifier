/*******************************************************************************
 * Bizcal is a component library for calendar widgets written in java using swing.
 * Copyright (C) 2007  Frederik Bertilsson 
 * Contributors:       Martin Heinemann martin.heinemann(at)tudor.lu
 * 
 * http://sourceforge.net/projects/bizcal/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 *******************************************************************************/
package bizcal.swing;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;
import bizcal.util.DateUtil;
import bizcal.util.LocaleBroker;

/**
 * @author Fredrik Bertilsson
 */
public class TabularView extends CalendarView {
	
	// private JPanel panel;
	
	// private int dayCount = 14;
	private JTable table;
	private JScrollPane scroll;
	
	public TabularView(CalendarViewConfig desc) throws Exception {
		super(desc);
		this.table = new JTable();
		this.scroll = new JScrollPane(this.table);
	}
	
	@Override
	public JComponent getComponent() {
		return this.scroll;
	}
	
	public long getTimeInterval() throws Exception {
		return 0;
	}
	
	@Override
	public void refresh0() throws Exception {
		DateFormat dateFormat = DateFormat.getDateInstance(
				DateFormat.SHORT,
				LocaleBroker.getLocale());
		DateFormat timeFormat = DateFormat.getTimeInstance(
				DateFormat.SHORT,
				LocaleBroker.getLocale());
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(this.tr("Date"));
		Map eventMap = new HashMap();
		Iterator i = this.getSelectedCalendars().iterator();
		while (i.hasNext()) {
			bizcal.common.Calendar cal = (bizcal.common.Calendar) i.next();
			model.addColumn(cal.getSummary());
			eventMap.put(cal.getId(), this.createEventsPerDay(cal.getId()));
		}
		Date date = this.getInterval().getStartDate();
		while (date.before(this.getInterval().getEndDate())) {
			Vector row = new Vector();
			row.add(dateFormat.format(date));
			i = this.getSelectedCalendars().iterator();
			while (i.hasNext()) {
				bizcal.common.Calendar cal = (bizcal.common.Calendar) i.next();
				Map eventsPerDay = (Map) eventMap.get(cal.getId());
				List events = (List) eventsPerDay.get(date);
				StringBuffer str = new StringBuffer();
				if (events != null) {
					Iterator j = events.iterator();
					while (j.hasNext()) {
						Event event = (Event) j.next();
						str.append(timeFormat.format(event.getStart()) + "-");
						str.append(timeFormat.format(event.getEnd()));
						if (j.hasNext())
							str.append(", ");
					}
				}
				row.add(str);
			}
			model.addRow(row);
			System.err.println("TabularView: " + row);
			date = DateUtil.getDiffDay(date, +1);
		}
		this.table.setModel(model);
		model.fireTableDataChanged();
		this.setColumnWidths();
	}
	
	private void setColumnWidths() {
		TableColumnModel model = this.table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			TableColumn col = model.getColumn(i);
			if (i == 0)
				col.setWidth(50);
			else
				col.setWidth(100);
		}
	}
	
	@Override
	public Date getDate(int x, int y) {
		return null;
	}
	
	private String tr(String str) {
		return str;
	}
	
}
