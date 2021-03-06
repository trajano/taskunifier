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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import lu.tudor.santec.bizcal.AbstractCalendarView;
import lu.tudor.santec.bizcal.CalendarIcons;
import lu.tudor.santec.bizcal.EventModel;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.print.PrintUtilities;
import lu.tudor.santec.bizcal.resources.BizCalTranslations;
import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.CalendarListener;
import bizcal.swing.CalendarView;
import bizcal.swing.DayView;
import bizcal.util.Interval;

/**
 * @author martin.heinemann@tudor.lu
 *         19.06.2008
 *         14:49:51
 * 
 * 
 * @version
 * <br>
 *          $Log: DayViewPanel.java,v $ <br>
 *          Revision 1.4 2008/08/12 12:47:28 heine_ <br>
 *          fixed some bugs and made code improvements <br>
 * <br>
 *          Revision 1.3 2008/06/24 12:53:25 heine_ <br>
 *          *** empty log message *** <br>
 * 
 */
public class DayViewPanel extends AbstractCalendarView {
	
	private static final long serialVersionUID = 1L;
	
	private final static int STATE_FULL = 1;
	private final static int STATE_MORNING = 2;
	private final static int STATE_AFTERNOON = 3;
	
	private int state = STATE_FULL;
	
	private JToggleButton button;
	private EventModel dayModel;
	private DayView dayView;
	public static final String VIEW_NAME_DAY = "DAY_VIEW";
	public static final String VIEW_NAME_WEEK = "WEEK_VIEW";
	public String VIEW_NAME;
	private JButton switcherButton;
	
	private ImageIcon fullDayIcon;
	
	private ImageIcon morningDayIcon;
	
	private ImageIcon afternoonDayIcon;
	
	private DayViewConfig dayViewConfig;
	
	/**
	 * @param model
	 */
	public DayViewPanel(EventModel model) {
		/* ================================================== */
		this(model, new DayViewConfig());
		/* ================================================== */
	}
	
	/**
	 * @param model
	 * @param config
	 */
	public DayViewPanel(EventModel model, DayViewConfig config) {
		/* ================================================== */
		
		this.dayModel = model;
		this.dayViewConfig = config;
		
		this.setLayout(new BorderLayout());
		
		if (this.dayModel.getType() == EventModel.TYPE_DAY) {
			/* ------------------------------------------------------- */
			this.VIEW_NAME = VIEW_NAME_DAY;
			this.button = new JToggleButton(
					CalendarIcons.getMediumIcon(CalendarIcons.DAYVIEW));
			this.button.setToolTipText(BizCalTranslations.getString("bizcal.DAY_VIEW"));
			/* ------------------------------------------------------- */
		} else {
			/* ------------------------------------------------------- */
			this.VIEW_NAME = VIEW_NAME_WEEK;
			this.button = new JToggleButton(
					CalendarIcons.getMediumIcon(CalendarIcons.WEEKVIEW));
			this.button.setToolTipText(BizCalTranslations.getString("bizcal.WEEK_VIEW"));
			
			// set the weekday start/stop to the model
			this.dayModel.setWeekdayStartEnd(
					this.dayViewConfig.getWeekStart(),
					this.dayViewConfig.getWeekEnd());
			/* ------------------------------------------------------- */
		}
		
		try {
			/* ------------------------------------------------------- */
			
			this.initDayViewSwitcherButton();
			
			this.dayView = new DayView(this.dayViewConfig, this.switcherButton);
			this.dayView.setModel(this.dayModel);
			this.dayModel.addCalendarView(this.dayView);
			
			this.dayView.refresh();
			// dayView.refresh0();
			this.add(this.dayView.getComponent());
			
			this.updateSwitcherButton();
			/* ------------------------------------------------------- */
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ================================================== */
	}
	
	/**
	 * Updates the settings from the config file
	 */
	public void refresh() {
		/* ================================================== */
		if (VIEW_NAME_WEEK.equals(this.VIEW_NAME)) {
			/* ------------------------------------------------------- */
			// set the weekday start/stop to the model
			this.dayModel.setWeekdayStartEnd(
					this.dayViewConfig.getWeekStart(),
					this.dayViewConfig.getWeekEnd());
			/* ------------------------------------------------------- */
		}
		
		try {
			// dayView.refresh();
			this.updateSwitcherButton();
			this.refreshGrid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ================================================== */
	}
	
	/**
	 * 
	 */
	private void refreshGrid() {
		/* ================================================== */
		try {
			this.dayView.resetHorizontalLines();
			this.dayView.refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* ================================================== */
	}
	
	/**
	 *
	 */
	private void initDayViewSwitcherButton() {
		/* ====================================================== */
		this.switcherButton = new JButton(
				CalendarIcons.getMediumIcon(CalendarIcons.DELETE));
		this.switcherButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/* ====================================================== */
				DayViewPanel.this.switchState();
				/* ------------------------------------------------------- */
				// update buttons
				DayViewPanel.this.updateSwitcherButton();
				/* ====================================================== */
			}
			
		});
		
		this.switcherButton.setBorderPainted(false);
		this.switcherButton.setContentAreaFilled(false);
		this.switcherButton.setFocusPainted(false);
		
		this.fullDayIcon = CalendarIcons.getMediumIcon(CalendarIcons.DAY_FULL);
		this.morningDayIcon = CalendarIcons.getMediumIcon(CalendarIcons.DAY_MORNING);
		this.afternoonDayIcon = CalendarIcons.getMediumIcon(CalendarIcons.DAY_AFTERNOON);
		
		/* ====================================================== */
	}
	
	/**
	 * Update the button in the upper left corner to switch the day view
	 */
	private void updateSwitcherButton() {
		/* ================================================== */
		if (STATE_FULL == this.state) {
			/* ------------------------------------------------------- */
			this.switcherButton.setIcon(this.fullDayIcon);
			this.switcherButton.setToolTipText(BizCalTranslations.getString("bizcal.switcher.morning"));
			
			this.dayViewConfig.setDayStartHour(0);
			this.dayViewConfig.setDayEndHour(24);
			/* ------------------------------------------------------- */
		} else if (STATE_MORNING == this.state) {
			/* ------------------------------------------------------- */
			this.switcherButton.setIcon(this.morningDayIcon);
			this.switcherButton.setToolTipText(BizCalTranslations.getString("bizcal.switcher.afternoon"));
			
			this.dayViewConfig.setDayStartHour(this.dayViewConfig.getDefaultDayStartHour());
			this.dayViewConfig.setDayEndHour(this.dayViewConfig.getDayBreak());
			/* ------------------------------------------------------- */
		} else if (STATE_AFTERNOON == this.state) {
			/* ------------------------------------------------------- */
			this.switcherButton.setIcon(this.afternoonDayIcon);
			this.switcherButton.setToolTipText(BizCalTranslations.getString("bizcal.switcher.full"));
			
			this.dayViewConfig.setDayStartHour(this.dayViewConfig.getDayBreak());
			this.dayViewConfig.setDayEndHour(this.dayViewConfig.getDefaultDayEndHour());
			/* ------------------------------------------------------- */
		}
		try {
			this.dayView.refresh();
			this.refreshGrid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ================================================== */
	}
	
	public void addCalendarListener(CalendarListener listener) {
		/* ================================================== */
		this.dayView.addListener(listener);
		/* ================================================== */
	}
	
	@Override
	public JToggleButton getButton() {
		return this.button;
	}
	
	@Override
	public String getViewName() {
		return this.VIEW_NAME;
	}
	
	@Override
	public void dateChanged(Date date) {
		this.dayModel.setDate(date);
		try {
			this.dayView.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moved(
			Event event,
			Object orgCalId,
			Date orgDate,
			Object newCalId,
			Date newDate) throws Exception {
		/* ====================================================== */
		event.move(newDate);
		this.dayModel.triggerUpdate();
		/* ====================================================== */
	}
	
	public void resized(
			Event event,
			Object orgCalId,
			Date orgEndDate,
			Date newEndDate) throws Exception {
		/* ====================================================== */
		event.setEnd(newEndDate);
		// notify all models that uses the same ObservableEventList
		this.dayModel.triggerUpdate();
		/* ====================================================== */
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
		/* ================================================== */
		try {
			Interval interval = this.dayModel.getInterval();
			/* ------------------------------------------------------- */
			Date start = (Date) interval.getStart();
			Date end = (Date) interval.getEnd();
			/* ------------------------------------------------------- */
			List<Event> evs = this.dayModel.getEvents(null);
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
		/* ================================================== */
	}
	
	public void setZoomFactor(int zoom) {
		/* ================================================== */
		DayView.PIXELS_PER_HOUR = zoom;
		this.refresh();
		/* ================================================== */
	}
	
	/**
	 * Defines the order of the day view switching.
	 * 1. Full
	 * 2. Morning
	 * 3. Afternoon
	 * 
	 * @param state
	 */
	private void switchState() {
		/* ================================================== */
		if (STATE_FULL == this.state)
			this.state = STATE_MORNING;
		else if (STATE_MORNING == this.state)
			this.state = STATE_AFTERNOON;
		else if (STATE_AFTERNOON == this.state)
			this.state = STATE_FULL;
		else
			this.state = STATE_AFTERNOON;
		/* ================================================== */
	}
	
	@Override
	public void print(boolean showPrinterDialog) {
		int curZoom = DayView.PIXELS_PER_HOUR;
		this.setZoomFactor(30);
		if (this.dayModel.getType() == EventModel.TYPE_DAY)
			PrintUtilities.printComponent(this, showPrinterDialog, false);
		else
			PrintUtilities.printComponent(this, showPrinterDialog, true);
		this.setZoomFactor(curZoom);
	}
	
	@Override
	public CalendarView getView() {
		/* ====================================================== */
		return this.dayView;
		/* ====================================================== */
	}
	
}
