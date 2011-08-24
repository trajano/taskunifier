package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
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
import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.CalendarListener.CalendarAdapter;
import bizcal.util.DateInterval;
import bizcal.util.TimeOfDay;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainView;

public class TasksCalendarPanel extends JPanel implements TaskCalendarView, SavePropertiesListener {
	
	private ObservableEventList eventDataList;
	
	private boolean showCompletedTasks;
	
	private DayViewPanel dayViewPanel;
	private DayViewPanel weekViewPanel;
	
	private TasksCalendar[] tasksCalendars = new TasksCalendar[] {
			new TasksStartDateCalendar(),
			new TasksDueDateCalendar() };
	
	private CalendarPanel calendarPanel;
	
	public TasksCalendarPanel(MainView mainView) {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new CalendarPanel();
		this.eventDataList = new ObservableEventList();
		
		DayViewConfig config = new DayViewConfig();
		config.setStartView(new TimeOfDay(0, 0));
		config.setEndView(new TimeOfDay(24, 0));
		config.setDefaultDayStartHour(0);
		config.setDefaultDayEndHour(24);
		
		EventModel dayModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_DAY);
		EventModel weekModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_WEEK);
		
		this.dayViewPanel = new DayViewPanel(dayModel, config);
		this.weekViewPanel = new DayViewPanel(weekModel, config);
		
		TasksCalendarListener calListener = new TasksCalendarListener();
		
		this.dayViewPanel.addCalendarListener(calListener);
		this.weekViewPanel.addCalendarListener(calListener);
		
		this.calendarPanel.addCalendarView(this.dayViewPanel);
		this.calendarPanel.addCalendarView(this.weekViewPanel);
		
		this.calendarPanel.showView(this.weekViewPanel.getViewName());
		
		boolean foundSelected = false;
		String selectedCalendar = Main.SETTINGS.getStringProperty("calendar.selected");
		
		for (TasksCalendar calendar : this.tasksCalendars) {
			try {
				boolean active = Main.SETTINGS.getBooleanProperty("calendar."
						+ calendar.getId()
						+ ".active");
				
				calendar.setActive(active);
			} catch (Throwable t) {

			}
			
			this.calendarPanel.addNamedCalendar(calendar);
			
			if (EqualsUtils.equals(
					selectedCalendar,
					calendar.getId().toString())) {
				foundSelected = true;
				this.calendarPanel.setSelectedCalendar(calendar);
			}
		}
		
		if (!foundSelected)
			this.calendarPanel.setSelectedCalendar(this.tasksCalendars[1]);
		
		this.calendarPanel.addNamedCalendarListener(new NamedCalendarListener() {
			
			@Override
			public void activeCalendarsChanged(
					Collection<NamedCalendar> calendars) {
				if (calendars == null || calendars.size() < 1) {
					TasksCalendarPanel.this.eventDataList.clear();
					return;
				}
				
				TasksCalendarPanel.this.refreshTasks();
			}
			
			@Override
			public void selectedCalendarChanged(NamedCalendar selectedCalendar) {
				TasksCalendarPanel.this.refreshTasks();
			}
			
		});
		
		// this.calendarPanel.getFunctionsButtonPanel().add();
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
	}
	
	public boolean isShowCompletedTasks() {
		return this.showCompletedTasks;
	}
	
	public void setShowCompletedTasks(boolean showCompletedTasks) {
		this.showCompletedTasks = showCompletedTasks;
		this.refreshTasks();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized void refreshTasks() {
		for (TasksCalendar calendar : this.tasksCalendars)
			calendar.updateEvents(this.showCompletedTasks);
		
		List<Event> allActiveEvents = new ArrayList<Event>();
		
		for (NamedCalendar nc : this.calendarPanel.getCalendars()) {
			if (nc.isActive())
				allActiveEvents.addAll(nc.getEvents(null, null));
		}
		
		Collections.sort(allActiveEvents);
		
		this.eventDataList.clear();
		this.eventDataList.addAll(allActiveEvents);
	}
	
	private class TasksCalendarListener extends CalendarAdapter {
		
		@Override
		public void eventDoubleClick(
				Object id,
				Event event,
				MouseEvent mouseEvent) {
			Task[] tasks = new Task[] { this.getTask(event) };
			ActionEditTasks.editTasks(tasks);
			
			TasksCalendarPanel.this.refreshTasks();
		}
		
		@Override
		public void newEvent(Object id, DateInterval interval) throws Exception {
			interval.setStartDate(this.roundMinutes(interval.getStartDate()));
			interval.setEndDate(this.roundMinutes(interval.getEndDate()));
			
			for (TasksCalendar calendar : TasksCalendarPanel.this.tasksCalendars)
				if (calendar.isSelected())
					calendar.newEvent(interval);
			
			TasksCalendarPanel.this.refreshTasks();
		}
		
		@Override
		public void deleteEvents(List<Event> events) {
			for (Event event : events) {
				Task task = this.getTask(event);
				TaskFactory.getInstance().markToDelete(task);
			}
			
			TasksCalendarPanel.this.refreshTasks();
		}
		
		@Override
		public void moved(
				Event event,
				Object orgCalId,
				Date orgDate,
				Object newCalId,
				Date newDate) throws Exception {
			newDate = this.roundMinutes(newDate);
			
			for (TasksCalendar calendar : TasksCalendarPanel.this.tasksCalendars)
				if (calendar.getId().equals(
						event.get(NamedCalendar.CALENDAR_ID)))
					calendar.moved(event, orgDate, newDate);
			
			TasksCalendarPanel.this.refreshTasks();
		}
		
		@Override
		public void resized(
				Event event,
				Object orgCalId,
				Date orgEndDate,
				Date newEndDate) throws Exception {
			newEndDate = this.roundMinutes(newEndDate);
			
			for (TasksCalendar calendar : TasksCalendarPanel.this.tasksCalendars)
				if (calendar.getId().equals(
						event.get(NamedCalendar.CALENDAR_ID)))
					calendar.resized(event, orgEndDate, newEndDate);
			
			TasksCalendarPanel.this.refreshTasks();
		}
		
		public Task getTask(Event event) {
			return TaskFactory.getInstance().get((ModelId) event.getId());
		}
		
		public Date roundMinutes(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			int minutes = calendar.get(Calendar.MINUTE);
			
			int mod = minutes % 5;
			if (mod < 2.5)
				minutes -= mod;
			else
				minutes += 5 - mod;
			
			calendar.set(Calendar.MINUTE, minutes);
			return calendar.getTime();
		}
		
	}
	
	@Override
	public void saveProperties() {
		Main.SETTINGS.remove("calendar.selected");
		
		for (TasksCalendar calendar : this.tasksCalendars) {
			Main.SETTINGS.setBooleanProperty("calendar."
					+ calendar.getId()
					+ ".active", calendar.isActive());
			
			if (calendar.isSelected())
				Main.SETTINGS.setStringProperty(
						"calendar.selected",
						calendar.getId().toString());
		}
	}
	
}
