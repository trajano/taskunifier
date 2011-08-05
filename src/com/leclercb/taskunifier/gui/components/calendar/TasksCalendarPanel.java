package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import lu.tudor.santec.bizcal.CalendarPanel;
import lu.tudor.santec.bizcal.EventModel;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.listeners.NamedCalendarListener;
import lu.tudor.santec.bizcal.util.ObservableEventList;
import lu.tudor.santec.bizcal.views.DayViewPanel;
import lu.tudor.santec.bizcal.views.MonthViewPanel;
import bizcal.common.Event;

import com.leclercb.taskunifier.gui.main.MainView;

public class TasksCalendarPanel extends JPanel {
	
	private ObservableEventList eventDataList;
	
	private DayViewPanel dayViewPanel;
	private DayViewPanel weekViewPanel;
	private MonthViewPanel monthViewPanel;
	
	private TasksCalendar tasksCalendar;
	
	private CalendarPanel calendarPanel;
	
	public TasksCalendarPanel(MainView mainView) {
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new CalendarPanel();
		this.eventDataList = new ObservableEventList();
		
		EventModel dayModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_DAY);
		EventModel weekModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_WEEK);
		EventModel monthModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_MONTH);
		
		this.dayViewPanel = new DayViewPanel(dayModel);
		this.weekViewPanel = new DayViewPanel(weekModel);
		this.monthViewPanel = new MonthViewPanel(monthModel);
		
		this.calendarPanel.addCalendarView(this.dayViewPanel);
		this.calendarPanel.addCalendarView(this.weekViewPanel);
		this.calendarPanel.addCalendarView(this.monthViewPanel);
		
		this.calendarPanel.showView(this.weekViewPanel.getViewName());
		
		this.tasksCalendar = new TasksCalendar();
		
		this.calendarPanel.addNamedCalendar(this.tasksCalendar);
		
		this.calendarPanel.addNamedCalendarListener(new NamedCalendarListener() {
			
			@Override
			public void activeCalendarsChanged(
					Collection<NamedCalendar> calendars) {
				if (calendars == null || calendars.size() < 1) {
					TasksCalendarPanel.this.eventDataList.clear();
					return;
				}
				
				TasksCalendarPanel.this.updateEventsForActiveCalendars();
			}
			
			@Override
			public void selectedCalendarChanged(NamedCalendar selectedCalendar) {
				TasksCalendarPanel.this.updateEventsForActiveCalendars();
			}
			
		});
		
		this.calendarPanel.setSelectedCalendar(this.tasksCalendar);
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void updateEventsForActiveCalendars() {
		this.tasksCalendar.updateEvents();
		
		List<Event> allActiveEvents = new ArrayList<Event>();
		
		for (NamedCalendar nc : this.calendarPanel.getCalendars()) {
			if (nc.isActive())
				allActiveEvents.addAll(nc.getEvents(null, null));
		}
		
		Collections.sort(allActiveEvents);
		
		this.eventDataList.clear();
		this.eventDataList.addAll(allActiveEvents);
	}
	
}
