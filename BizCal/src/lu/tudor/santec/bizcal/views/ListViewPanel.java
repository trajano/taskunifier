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
import java.awt.Color;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JToggleButton;

import lu.tudor.santec.bizcal.AbstractCalendarView;
import lu.tudor.santec.bizcal.CalendarIcons;
import lu.tudor.santec.bizcal.EventModel;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.print.PrintUtilities;
import lu.tudor.santec.bizcal.resources.BizCalTranslations;
import lu.tudor.santec.bizcal.views.list.ListView;
import bizcal.common.DayViewConfig;
import bizcal.swing.CalendarListener;
import bizcal.swing.CalendarView;
import bizcal.swing.util.GradientArea;

public class ListViewPanel extends AbstractCalendarView {
	
	private DateFormat df = new SimpleDateFormat(
			"dd.MM.yyyy",
			BizCalTranslations.getLocale());
	private static final long serialVersionUID = 1L;
	private JToggleButton button;
	private EventModel eventModel;
	public static final String VIEW_NAME = "LIST_VIEW";
	public ListView listView;
	Color primaryColor = new Color(230, 230, 230);
	Color secondaryColor = Color.WHITE;
	private GradientArea gp;
	
	/**
	 * static logger for this class
	 */
	private static Logger logger = Logger.getLogger(ListViewPanel.class.getName());
	
	public ListViewPanel(EventModel model) {
		
		this.eventModel = model;
		
		this.button = new JToggleButton(
				CalendarIcons.getMediumIcon(CalendarIcons.LISTVIEW));
		this.button.setToolTipText(BizCalTranslations.getString("bizcal.LIST_VIEW"));
		
		this.setLayout(new BorderLayout());
		
		this.gp = new GradientArea(
				GradientArea.TOP_BOTTOM,
				this.secondaryColor,
				this.primaryColor);
		this.gp.setPreferredSize(new Dimension(30, 30));
		
		this.add(this.gp, BorderLayout.NORTH);
		
		try {
			this.listView = new ListView(new DayViewConfig(), this);
			this.listView.setModel(this.eventModel);
			this.eventModel.addCalendarView(this.listView);
			
			this.listView.refresh();
			this.listView.refresh0();
			this.add(this.listView.getComponent(), BorderLayout.CENTER);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "listView creation failed", e);
		}
		
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
		this.eventModel.setDate(date);
		this.listView.setDate(date);
		try {
			this.eventModel.refresh();
		} catch (Exception e) {
			logger.log(Level.WARNING, "updating listView failed", e);
		}
	}
	
	public void setTitle(Date start, Date end) {
		try {
			this.gp.setText(
					"<html><center>"
							+ BizCalTranslations.getString("bizcal.LIST_VIEW")
							+ "<br>"
							+ this.df.format(start)
							+ " - "
							+ this.df.format(end),
					true);
		} catch (Exception e) {
			logger.log(Level.WARNING, "setTitle failed", e);
		}
	}
	
	@Override
	public void activeCalendarsChanged(Collection<NamedCalendar> calendars) {
		/* ====================================================== */
		/* ====================================================== */
	}
	
	@Override
	public void selectedCalendarChanged(NamedCalendar selectedCalendar) {
		/* ====================================================== */
		/* ====================================================== */
	}
	
	public void addCalendarListener(CalendarListener listener) {
		/* ================================================== */
		this.listView.addListener(listener);
		/* ================================================== */
	}
	
	public void setShowDays(int showDays) {
		this.listView.setShowDays(showDays);
	}
	
	@Override
	public List getEvents() {
		try {
			return this.listView.getEvents();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void print(boolean showPrinterDialog) {
		PrintUtilities.printComponent(this, showPrinterDialog, false);
	}
	
	@Override
	public CalendarView getView() {
		/* ====================================================== */
		return this.listView;
		/* ====================================================== */
	}
	
}
