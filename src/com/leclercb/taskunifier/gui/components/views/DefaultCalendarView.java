package com.leclercb.taskunifier.gui.components.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.calendar.TasksCalendarPanel;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

class DefaultCalendarView extends JPanel implements CalendarView {
	
	private MainView mainView;
	
	private TasksCalendarPanel calendarPanel;
	
	public DefaultCalendarView(MainView mainView) {
		CheckUtils.isNotNull(mainView, "Main view cannot be null");
		this.mainView = mainView;
		
		this.initialize();
	}
	
	@Override
	public ViewType getViewType() {
		return ViewType.CALENDAR;
	}
	
	@Override
	public JPanel getViewContent() {
		return this;
	}
	
	private void updateCalendar() {
		this.calendarPanel.updateEventsForActiveCalendars();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new TasksCalendarPanel(this.mainView);
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
		
		this.mainView.addPropertyChangeListener(
				MainView.PROP_SELECTED_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (DefaultCalendarView.this.mainView.getSelectedViewType() == DefaultCalendarView.this.getViewType())
							DefaultCalendarView.this.updateCalendar();
					}
					
				});
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultCalendarView.this.updateCalendar();
			}
			
		};
		
		JButton udpateButton = new JButton(
				Translations.getString("general.update"));
		udpateButton.setActionCommand("UPDATE");
		udpateButton.addActionListener(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(udpateButton);
		
		this.add(panel, BorderLayout.NORTH);
	}
	
}
