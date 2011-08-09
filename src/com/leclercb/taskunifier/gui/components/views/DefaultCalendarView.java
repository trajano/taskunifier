package com.leclercb.taskunifier.gui.components.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.calendar.TasksCalendarPanel;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

class DefaultCalendarView extends JPanel implements CalendarView {
	
	private MainView mainView;
	
	private JCheckBox showCompletedTasksCheckBox;
	
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
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new TasksCalendarPanel(this.mainView);
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		
		this.initializeShowCompletedTasksCheckBox(topPanel);
		this.initializeButtonsPanel(topPanel);
		
		this.mainView.addPropertyChangeListener(
				MainView.PROP_SELECTED_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (DefaultCalendarView.this.mainView.getSelectedViewType() == DefaultCalendarView.this.getViewType())
							DefaultCalendarView.this.calendarPanel.updateEventsForActiveCalendars();
					}
					
				});
		
		boolean selected = Main.SETTINGS.getBooleanProperty("tasksearcher.show_completed_tasks");
		this.calendarPanel.setShowCompletedTasks(selected);
	}
	
	private void initializeButtonsPanel(JPanel topPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultCalendarView.this.calendarPanel.updateEventsForActiveCalendars();
			}
			
		};
		
		JButton udpateButton = new JButton(
				Translations.getString("general.update"));
		udpateButton.setActionCommand("UPDATE");
		udpateButton.addActionListener(listener);
		
		JButton helpButton = Help.getHelpButton(Help.getHelpFile("calendar.html"));
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				udpateButton,
				helpButton);
		
		topPanel.add(panel, BorderLayout.EAST);
	}
	
	private void initializeShowCompletedTasksCheckBox(JPanel topPanel) {
		this.showCompletedTasksCheckBox = new JCheckBox(
				Translations.getString("configuration.general.show_completed_tasks"));
		
		this.showCompletedTasksCheckBox.setSelected(Main.SETTINGS.getBooleanProperty("tasksearcher.show_completed_tasks"));
		
		Main.SETTINGS.addPropertyChangeListener(
				"tasksearcher.show_completed_tasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						boolean selected = Main.SETTINGS.getBooleanProperty("tasksearcher.show_completed_tasks");
						DefaultCalendarView.this.showCompletedTasksCheckBox.setSelected(selected);
						DefaultCalendarView.this.calendarPanel.setShowCompletedTasks(selected);
					}
					
				});
		
		this.showCompletedTasksCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selected = DefaultCalendarView.this.showCompletedTasksCheckBox.isSelected();
				Main.SETTINGS.setBooleanProperty(
						"tasksearcher.show_completed_tasks",
						selected);
				DefaultCalendarView.this.calendarPanel.setShowCompletedTasks(selected);
			}
			
		});
		
		topPanel.add(this.showCompletedTasksCheckBox, BorderLayout.WEST);
	}
	
}
