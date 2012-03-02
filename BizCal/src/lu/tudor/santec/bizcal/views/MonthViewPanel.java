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
package lu.tudor.santec.bizcal.views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JToggleButton;

import lu.tudor.santec.bizcal.AbstractCalendarView;
import lu.tudor.santec.bizcal.CalendarIcons;
import lu.tudor.santec.bizcal.EventModel;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.print.PrintUtilities;
import lu.tudor.santec.bizcal.resources.BizCalTranslations;
import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;
import bizcal.swing.CalendarListener;
import bizcal.swing.CalendarView;
import bizcal.swing.MonthView;
import bizcal.util.Interval;

public class MonthViewPanel extends AbstractCalendarView {
	
	private static final long serialVersionUID = 1L;
	private JToggleButton button;
	private MonthView monthView;
	private EventModel monthModel;
	public static final String VIEW_NAME = "MONTH_VIEW";
	
	public MonthViewPanel(EventModel model) {
		this(model, new CalendarViewConfig());
	}
	
	public MonthViewPanel(EventModel model, CalendarViewConfig config) {
		this.button = new JToggleButton(
				CalendarIcons.getMediumIcon(CalendarIcons.MONTHVIEW));
		this.button.setToolTipText(BizCalTranslations.getString("bizcal.MONTH_VIEW"));
		this.setLayout(new BorderLayout());
		
		this.monthModel = model;
		try {
			this.monthView = new MonthView(config);
			this.monthView.setModel(this.monthModel);
			
			this.monthModel.addCalendarView(this.monthView);
			
			this.add(this.monthView.getComponent(), BorderLayout.CENTER);
			this.monthView.refresh0();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void addCalendarListener(CalendarListener listener) {
		/* ================================================== */
		this.monthView.addListener(listener);
		/* ================================================== */
	}
	
	@Override
	public JToggleButton getButton() {
		return this.button;
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
	
	@Override
	public void dateChanged(Date date) {
		this.monthModel.setDate(date);
		try {
			this.monthView.refresh0();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void activeCalendarsChanged(Collection<NamedCalendar> calendars) {
		/* ====================================================== */
		// TODO Auto-generated method stub
		/* ====================================================== */
	}
	
	@Override
	public void selectedCalendarChanged(NamedCalendar selectedCalendar) {
		/* ====================================================== */
		// TODO Auto-generated method stub
		/* ====================================================== */
	}
	
	@Override
	public List getEvents() {
		try {
			Interval interval = this.monthModel.getInterval();
			/* ------------------------------------------------------- */
			Date start = (Date) interval.getStart();
			Date end = (Date) interval.getEnd();
			/* ------------------------------------------------------- */
			List<Event> evs = this.monthModel.getEvents(null);
			List<Event> shownEvents = new ArrayList<Event>();
			/* ------------------------------------------------------- */
			if (evs != null)
				for (Event e : evs) {
					/* ------------------------------------------------------- */
					if (e.getStart().after(start) && e.getStart().before(end))
						shownEvents.add(e);
					/* ------------------------------------------------------- */
				}
			return shownEvents;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void print(boolean showPrinterDialog) {
		PrintUtilities.printComponent(this, showPrinterDialog, true);
	}
	
	@Override
	public CalendarView getView() {
		/* ====================================================== */
		return this.monthView;
		/* ====================================================== */
	}
	
}
