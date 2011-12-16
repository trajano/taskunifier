package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

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
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TasksCalendarPanel extends JPanel implements TaskCalendarView, SavePropertiesListener {
	
	private ModelSelectionChangeSupport modelSelectionChangeSupport;
	
	private ObservableEventList eventDataList;
	
	private DayViewPanel dayViewPanel;
	private DayViewPanel weekViewPanel;
	
	private TasksCalendar[] tasksCalendars = new TasksCalendar[] {
			new TasksStartDateCalendar(),
			new TasksDueDateCalendar() };
	
	private CalendarPanel calendarPanel;
	
	public TasksCalendarPanel(MainView mainView) {
		this.modelSelectionChangeSupport = new ModelSelectionChangeSupport(this);
		Main.getSettings().addSavePropertiesListener(this);
		
		this.initialize();
		
		this.refreshTasks();
	}
	
	@Override
	public TaskSearcherView getTaskSearcherView() {
		return this.calendarPanel.getTaskSearcherPanel();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new CalendarPanel();
		this.eventDataList = new ObservableEventList();
		
		DayViewConfig config = new DayViewConfig();
		config.setStartView(new TimeOfDay(0, 0));
		config.setEndView(new TimeOfDay(24, 0));
		config.setDefaultDayStartHour(0);
		config.setDayBreak(12);
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
		String selectedCalendar = Main.getSettings().getStringProperty(
				"calendar.selected");
		
		for (TasksCalendar calendar : this.tasksCalendars) {
			try {
				boolean active = Main.getSettings().getBooleanProperty(
						"calendar." + calendar.getId() + ".active");
				
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
		
		this.calendarPanel.getTaskSearcherPanel().addTaskSearcherSelectionChangeListener(
				new TaskSearcherSelectionListener() {
					
					@Override
					public void taskSearcherSelectionChange(
							TaskSearcherSelectionChangeEvent event) {
						TasksCalendarPanel.this.refreshTasks();
					}
					
				});
		
		this.calendarPanel.getTaskSearcherPanel().addPropertyChangeListener(
				TaskSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TasksCalendarPanel.this.refreshTasks();
					}
					
				});
		
		Main.getSettings().addPropertyChangeListener(
				"tasksearcher.show_completed_tasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TasksCalendarPanel.this.refreshTasks();
					}
					
				});
		
		int zoom = Main.getSettings().getIntegerProperty(
				"view.calendar.zoom",
				80);
		this.calendarPanel.getZoomSlider().setValue(zoom);
		
		Main.getSettings().addPropertyChangeListener(
				"view.calendar.zoom",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						int zoom = Main.getSettings().getIntegerProperty(
								"view.calendar.zoom",
								80);
						TasksCalendarPanel.this.calendarPanel.getZoomSlider().setValue(
								zoom);
					}
					
				});
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
	}
	
	@Override
	public Task[] getSelectedTasks() {
		Event[] events = this.calendarPanel.getCurrentView().getView().getSelectedEvents();
		return TasksCalendar.getTasks(Arrays.asList(events));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public synchronized void refreshTasks() {
		boolean selected = Main.getSettings().getBooleanProperty(
				"tasksearcher.show_completed_tasks");
		TaskSearcher searcher = this.calendarPanel.getTaskSearcherPanel().getSelectedTaskSearcher();
		
		for (TasksCalendar calendar : this.tasksCalendars)
			calendar.updateEvents(selected, searcher);
		
		List<Event> allActiveEvents = new ArrayList<Event>();
		
		for (NamedCalendar nc : this.calendarPanel.getCalendars()) {
			if (nc.isActive())
				allActiveEvents.addAll(nc.getEvents(null, null));
		}
		
		Collections.sort(allActiveEvents);
		
		this.eventDataList.clear();
		this.eventDataList.addAll(allActiveEvents);
	}
	
	@Override
	public boolean shouldBeDisplayed(Task task) {
		TaskSearcher searcher = this.calendarPanel.getTaskSearcherPanel().getSelectedTaskSearcher();
		
		if (searcher != null
				&& !TaskUtils.showUnindentTask(task, searcher.getFilter()))
			return false;
		
		return true;
	}
	
	private class TasksCalendarListener extends CalendarAdapter {
		
		@Override
		public void eventSelected(Object id, Event event) throws Exception {
			this.eventsSelected(Arrays.asList(event));
		}
		
		@Override
		public void eventsSelected(List<Event> events) throws Exception {
			TasksCalendarPanel.this.modelSelectionChangeSupport.fireModelSelectionChange(TasksCalendar.getTasks(events));
		}
		
		@Override
		public void eventDoubleClick(
				Object id,
				Event event,
				MouseEvent mouseEvent) {
			Task[] tasks = new Task[] { TasksCalendar.getTask(event) };
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
			Task[] tasks = TasksCalendar.getTasks(events);
			for (Task task : tasks) {
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
		Main.getSettings().remove("calendar.selected");
		
		for (TasksCalendar calendar : this.tasksCalendars) {
			Main.getSettings().setBooleanProperty(
					"calendar." + calendar.getId() + ".active",
					calendar.isActive());
			
			if (calendar.isSelected())
				Main.getSettings().setStringProperty(
						"calendar.selected",
						calendar.getId().toString());
		}
		
		Main.getSettings().setIntegerProperty(
				"view.calendar.zoom",
				this.calendarPanel.getZoomSlider().getValue());
	}
	
	@Override
	public void addModelSelectionChangeListener(ModelSelectionListener listener) {
		this.modelSelectionChangeSupport.addModelSelectionChangeListener(listener);
	}
	
	@Override
	public void removeModelSelectionChangeListener(
			ModelSelectionListener listener) {
		this.modelSelectionChangeSupport.removeModelSelectionChangeListener(listener);
	}
	
}
