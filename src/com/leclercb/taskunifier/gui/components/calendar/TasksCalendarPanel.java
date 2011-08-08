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
import lu.tudor.santec.bizcal.views.ListViewPanel;
import lu.tudor.santec.bizcal.views.MonthViewPanel;
import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.CalendarListener.CalendarAdapter;
import bizcal.util.DateInterval;
import bizcal.util.TimeOfDay;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.MainFrame;
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
		EventModel monthModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_MONTH);
		EventModel listModel = new EventModel(
				this.eventDataList,
				EventModel.TYPE_MONTH);
		
		this.dayViewPanel = new DayViewPanel(dayModel, config);
		this.weekViewPanel = new DayViewPanel(weekModel, config);
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
	
	private class TasksCalendarListener extends CalendarAdapter {
		
		@Override
		public void newEvent(Object id, DateInterval interval) throws Exception {
			Task task = ActionAddTask.addTask(null, false, false);
			
			long diff = interval.getDuration();
			diff = diff / (60 * 1000);
			
			Calendar dueDate = Calendar.getInstance();
			dueDate.setTime(interval.getEndDate());
			
			task.setLength((int) diff);
			task.setDueDate(dueDate);
			
			TasksCalendarPanel.this.updateEventsForActiveCalendars();
		}
		
		@Override
		public void eventDoubleClick(
				Object id,
				Event event,
				MouseEvent mouseEvent) {
			MainFrame.getInstance().setSelectedViewType(ViewType.TASKS);
			Task[] tasks = new Task[] { this.getTask(event) };
			((TaskView) ViewType.TASKS.getView()).getTaskTableView().setSelectedTasks(
					tasks);
		}
		
		@Override
		public void moved(
				Event event,
				Object orgCalId,
				Date orgDate,
				Object newCalId,
				Date newDate) throws Exception {
			Task task = this.getTask(event);
			
			int length = task.getLength();
			
			Calendar dueDate = Calendar.getInstance();
			dueDate.setTime(newDate);
			dueDate.add(Calendar.MINUTE, length);
			
			task.setDueDate(dueDate);
			
			TasksCalendarPanel.this.updateEventsForActiveCalendars();
		}
		
		@Override
		public void resized(
				Event event,
				Object orgCalId,
				Date orgEndDate,
				Date newEndDate) throws Exception {
			Task task = this.getTask(event);
			
			long diff = orgEndDate.getTime() - newEndDate.getTime();
			diff = diff / (60 * 1000);
			
			Calendar dueDate = Calendar.getInstance();
			dueDate.setTime(newEndDate);
			
			task.setLength(task.getLength() - (int) diff);
			task.setDueDate(dueDate);
			
			TasksCalendarPanel.this.updateEventsForActiveCalendars();
		}
		
		public Task getTask(Event event) {
			return TaskFactory.getInstance().get((ModelId) event.getId());
		}
		
	}
	
}
