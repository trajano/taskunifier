package com.leclercb.taskunifier.gui.components.views;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.calendar.TaskCalendarView;
import com.leclercb.taskunifier.gui.components.calendar.TasksCalendarPanel;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.main.MainView;

public class DefaultCalendarView extends JPanel implements CalendarView {
	
	private MainView mainView;
	
	private TasksCalendarPanel calendarPanel;
	
	public DefaultCalendarView(MainView mainView) {
		CheckUtils.isNotNull(mainView);
		this.mainView = mainView;
		
		this.initialize();
	}
	
	@Override
	public JPanel getViewContent() {
		return this;
	}
	
	@Override
	public TaskSearcherView getTaskSearcherView() {
		return this.calendarPanel.getTaskSearcherView();
	}
	
	@Override
	public TaskCalendarView getTaskCalendarView() {
		return this.calendarPanel;
	}
	
	private void initialize() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new TasksCalendarPanel(this.mainView);
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
		
		ViewList.getInstance().addPropertyChangeListener(
				ViewList.PROP_CURRENT_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (ViewList.getInstance().getCurrentView().getViewType() == ViewType.CALENDAR)
							DefaultCalendarView.this.calendarPanel.refreshTasks();
					}
					
				});
		
		Synchronizing.addPropertyChangeListener(
				Synchronizing.PROP_SYNCHRONIZING,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (!(Boolean) evt.getNewValue())
							DefaultCalendarView.this.calendarPanel.refreshTasks();
					}
					
				});
	}
	
}
