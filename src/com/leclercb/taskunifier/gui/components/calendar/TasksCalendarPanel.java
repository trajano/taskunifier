package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
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
		
		this.dayViewPanel = new DayViewPanel(
				dayModel,
				Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.weekViewPanel = new DayViewPanel(
				weekModel,
				Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.monthViewPanel = new MonthViewPanel(monthModel);
		
		TasksCalendarListener calListener = new TasksCalendarListener();
		
		this.dayViewPanel.addCalendarListener(calListener);
		this.weekViewPanel.addCalendarListener(calListener);
		this.monthViewPanel.addCalendarListener(calListener);
		
		this.calendarPanel.addCalendarView(this.dayViewPanel);
		this.calendarPanel.addCalendarView(this.weekViewPanel);
		this.calendarPanel.addCalendarView(this.monthViewPanel);
		
		this.calendarPanel.addNamedCalendar(new TestNamedCalendar(
				"Peter",
				"dem Peter seiner",
				Color.RED));
		this.calendarPanel.addNamedCalendar(new TestNamedCalendar(
				"Max",
				"dem Max seiner",
				Color.BLUE));
		this.calendarPanel.addNamedCalendar(new TestNamedCalendar(
				"Office",
				"allen ihrer",
				Color.GRAY));
		
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
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
	}
	
	@SuppressWarnings("unchecked")
	private synchronized void updateEventsForActiveCalendars() {
		List<Event> allActiveEvents = new ArrayList<Event>();
		
		for (NamedCalendar nc : this.calendarPanel.getCalendars()) {
			if (nc.isActive() || nc.isSelected())
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
			if (TasksCalendarPanel.this.calendarPanel.getCalendars() == null)
				return;
			
			for (NamedCalendar nc : TasksCalendarPanel.this.calendarPanel.getCalendars()) {
				if (nc.getId().equals(event.get(Event.CALENDAR_ID))) {
					TasksCalendarPanel.this.calendarPanel.setSelectedCalendar(nc);
					return;
				}
			}
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
			event.move(newDate);
			TasksCalendarPanel.this.eventDataList.trigger();
		}
		
		@Override
		public void newCalendar() throws Exception {

		}
		
		@Override
		public void newEvent(Object id, Date date) throws Exception {
			DateInterval interval = new DateInterval(date, new Date(
					date.getTime() + 900000));
			this.newEvent(id, interval);
		}
		
		@Override
		public void newEvent(Object id, DateInterval interval) throws Exception {
			NamedCalendar nc = TasksCalendarPanel.this.calendarPanel.getSelectedCalendar();
			
			if (nc == null)
				return;
			
			Event event = new Event();
			event.setStart(interval.getStartDate());
			event.setEnd(interval.getEndDate());
			event.setId(id);
			
			nc.addEvent("clientXXX", event);
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
			NamedCalendar nc = TasksCalendarPanel.this.calendarPanel.getSelectedCalendar();
			
			if (nc == null)
				return;
			
			event.setEnd(newEndDate);
			nc.saveEvent("clientXXX", event, false);
		}
		
		@Override
		public void selectionReset() throws Exception {

		}
		
		@Override
		public void showEvent(Object id, Event event) throws Exception {

		}
		
	}
	
	class TestNamedCalendar extends NamedCalendar {
		
		private List<Event> calendarEvents = new ArrayList<Event>();
		
		public TestNamedCalendar(String name, String description, Color color) {
			super(name, description, color);
			
			this.setId(this.hashCode());
		}
		
		@Override
		public List<Event> getEvents(Date from, Date to) {
			return this.calendarEvents;
		}
		
		@Override
		public void deleteEvent(String clientId, Event event) {
			this.calendarEvents.remove(event);
			TasksCalendarPanel.this.eventDataList.remove(event);
		}
		
		@Override
		public List<Event> addEvent(String clientId, Event event) {
			event.set(Event.CALENDAR_ID, this.getId());
			event.setColor(this.getColor());
			
			TasksCalendarPanel.this.eventDataList.add(event);
			this.calendarEvents.add(event);
			return null;
		}
		
		@Override
		public List<Event> saveEvent(
				String clientId,
				Event event,
				boolean userInteraction) {
			return null;
		}
		
	}
	
}
