package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import lu.tudor.santec.bizcal.CalendarPanel;
import lu.tudor.santec.bizcal.EventModel;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.listeners.NamedCalendarListener;
import lu.tudor.santec.bizcal.util.ObservableEventList;
import lu.tudor.santec.bizcal.views.DayViewPanel;
import lu.tudor.santec.bizcal.views.ListViewPanel;
import lu.tudor.santec.bizcal.views.MonthViewPanel;
import bizcal.common.Event;
import bizcal.swing.CalendarListener;
import bizcal.swing.DayView.Layout;
import bizcal.swing.util.FrameArea;
import bizcal.util.DateInterval;

import com.leclercb.taskunifier.gui.main.MainView;

public class TasksCalendarPanel extends JPanel {
	
	private ObservableEventList eventDataList;
	
	private DayViewPanel dayViewPanel;
	private DayViewPanel weekViewPanel;
	private MonthViewPanel monthViewPanel;
	private ListViewPanel listViewPanel;
	
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
		EventModel listModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_MONTH);
		
		this.dayViewPanel = new DayViewPanel(
				dayModel,
				Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.weekViewPanel = new DayViewPanel(
				weekModel,
				Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.monthViewPanel = new MonthViewPanel(monthModel);
		this.listViewPanel = new ListViewPanel(listModel);
		
		TasksCalendarListener calListener = new TasksCalendarListener();
		
		this.dayViewPanel.addCalendarListener(calListener);
		this.weekViewPanel.addCalendarListener(calListener);
		this.monthViewPanel.addCalendarListener(calListener);
		this.listViewPanel.addCalendarListener(calListener);
		
		this.calendarPanel.addCalendarView(this.dayViewPanel);
		this.calendarPanel.addCalendarView(this.weekViewPanel);
		this.calendarPanel.addCalendarView(this.monthViewPanel);
		this.calendarPanel.addCalendarView(this.listViewPanel);
		
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
		
		// this.calendarPanel.getFunctionsButtonPanel().add();
		
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
	
	class TasksCalendarListener implements CalendarListener {
		
		@Override
		public void closeCalendar(Object calId) throws Exception {

		}
		
		@Override
		public void copy(List<Event> list) throws Exception {

		}
		
		@Override
		public void dateChanged(Date date) throws Exception {

		}
		
		@Override
		public void dateSelected(Date date) throws Exception {

		}
		
		@Override
		public void deleteEvent(Event event) throws Exception {

		}
		
		@Override
		public void deleteEvents(List<Event> events) {

		}
		
		@Override
		public void eventClicked(
				Object id,
				Event _event,
				FrameArea area,
				MouseEvent e) {

		}
		
		@Override
		public void eventDoubleClick(
				Object id,
				Event event,
				MouseEvent mouseEvent) {

		}
		
		@Override
		public void eventSelected(Object id, Event event) throws Exception {

		}
		
		@Override
		public void eventsSelected(List<Event> list) throws Exception {

		}
		
		@Override
		public void moved(
				Event event,
				Object orgCalId,
				Date orgDate,
				Object newCalId,
				Date newDate) throws Exception {

		}
		
		@Override
		public void newCalendar() throws Exception {

		}
		
		@Override
		public void newEvent(Object id, Date date) throws Exception {

		}
		
		@Override
		public void newEvent(Object id, DateInterval interval) throws Exception {

		}
		
		@Override
		public void paste(Object calId, Date date) throws Exception {

		}
		
		@Override
		public void resized(
				Event event,
				Object orgCalId,
				Date orgEndDate,
				Date newEndDate) throws Exception {

		}
		
		@Override
		public void selectionReset() throws Exception {

		}
		
		@Override
		public void showEvent(Object id, Event event) throws Exception {

		}
		
	}
	
}
