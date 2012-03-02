/*******************************************************************************
 * Copyright (c) 2007 by CRP Henri TUDOR - SANTEC LUXEMBOURG 
 * check http://www.santec.tudor.lu for more information
 *  
 * Contributor(s):
 * Johannes Hermen  johannes.hermen(at)tudor.lu                            
 * Martin Heinemann martin.heinemann(at)tudor.lu  
 *  
 * This library is free software; you can redistribute it and/or modify it  
 * under the terms of the GNU Lesser General Public License (version 2.1)
 * as published by the Free Software Foundation.
 * 
 * This software is distributed in the hope that it will be useful, but     
 * WITHOUT ANY WARRANTY; without even the implied warranty of               
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU        
 * Lesser General Public License for more details.                          
 * 
 * You should have received a copy of the GNU Lesser General Public         
 * License along with this library; if not, write to the Free Software      
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *******************************************************************************/
package lu.tudor.santec.bizcal.views.list;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.resources.BizCalTranslations;
import bizcal.common.Event;
import bizcal.util.DateUtil;

public class ListModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Event> events = new ArrayList<Event>();
	
	private DateFormat dateTime = new SimpleDateFormat(
			"dd.MM.yy HH:mm",
			BizCalTranslations.getLocale());
	private DateFormat date = new SimpleDateFormat(
			"EEE dd.MM.yy",
			BizCalTranslations.getLocale());
	private DateFormat time = new SimpleDateFormat(
			"HH:mm",
			BizCalTranslations.getLocale());
	/**
	 * static logger for this class
	 */
	private static Logger logger = Logger.getLogger(ListModel.class.getName());
	
	private static String[] columnNames = {
			BizCalTranslations.getString("bizcal.date"),
			BizCalTranslations.getString("bizcal.type"),
			BizCalTranslations.getString("bizcal.calendar"),
			BizCalTranslations.getString("bizcal.desc") };
	
	private static Class[] columnClasses = {
			String.class,
			ImageIcon.class,
			String.class,
			String.class };
	
	public ListModel() {}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}
	
	@Override
	public int getRowCount() {
		return this.events.size();
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Event e = this.events.get(rowIndex);
		if (e == null)
			return null;
		
		switch (columnIndex) {
			case 0:
				String str = "";
				try {
					if (DateUtil.isSameDay(e.getStart(), e.getEnd())) {
						String d = this.date.format(e.getStart());
						d = d.substring(0, 1).toUpperCase() + d.substring(1);
						str = "<html><b>"
								+ d
								+ "</b><br>"
								+ this.time.format(e.getStart())
								+ " - "
								+ this.time.format(e.getEnd());
					} else {
						str = "<html><b>"
								+ this.dateTime.format(e.getStart())
								+ "</b><br>"
								+ this.dateTime.format(e.getEnd());
					}
					
				} catch (Exception e1) {
					logger.log(
							Level.WARNING,
							"listmodel dateformating creation failed",
							e);
				}
				return str;
			case 1:
				return e.getIcon();
			case 2:
				return e.get(NamedCalendar.CALENDAR_NAME);
			case 3:
				return "<html><b>"
						+ (e.getSummary() != null ? e.getSummary() : "")
						+ "</b><br>"
						+ (e.getDescription() != null ? e.getDescription() : "");
			default:
				break;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void setEvents(List events) {
		this.events = new ArrayList<Event>(events);
		this.fireTableDataChanged();
	}
	
	public Event getEvent(int row) {
		return this.events.get(row);
	}
	
}
