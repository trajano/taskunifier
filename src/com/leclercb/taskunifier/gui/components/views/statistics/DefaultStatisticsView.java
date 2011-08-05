package com.leclercb.taskunifier.gui.components.views.statistics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.statistics.ModelCountStatistics;
import com.leclercb.taskunifier.gui.components.statistics.Statistics;
import com.leclercb.taskunifier.gui.components.statistics.TasksPerFolderStatistics;
import com.leclercb.taskunifier.gui.components.statistics.TasksPerStatusStatistics;
import com.leclercb.taskunifier.gui.components.statistics.TasksPerTagStatistics;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class DefaultStatisticsView extends JPanel implements StatisticsView {
	
	private MainView mainView;
	
	private JCheckBox showCompletedTasksCheckBox;
	
	private Statistics[] statistics = {
			new ModelCountStatistics(),
			new TasksPerStatusStatistics(),
			new TasksPerTagStatistics(),
			new TasksPerFolderStatistics() };
	
	public DefaultStatisticsView(MainView mainView) {
		CheckUtils.isNotNull(mainView, "Main view cannot be null");
		this.mainView = mainView;
		
		this.initialize();
	}
	
	@Override
	public ViewType getViewType() {
		return ViewType.STATISTICS;
	}
	
	@Override
	public JPanel getViewContent() {
		return this;
	}
	
	private void updateStatistics() {
		for (Statistics s : this.statistics)
			s.updateStatistics();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 3, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		for (Statistics s : this.statistics)
			panel.add(s.getComponent());
		
		this.add(panel, BorderLayout.CENTER);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		
		this.initializeShowCompletedTasksCheckBox(topPanel);
		this.initializeButtonsPanel(topPanel);
		
		this.mainView.addPropertyChangeListener(
				MainView.PROP_SELECTED_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (DefaultStatisticsView.this.mainView.getSelectedViewType() == DefaultStatisticsView.this.getViewType())
							DefaultStatisticsView.this.updateStatistics();
					}
					
				});
	}
	
	private void initializeButtonsPanel(JPanel topPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultStatisticsView.this.updateStatistics();
			}
			
		};
		
		JButton udpateButton = new JButton(
				Translations.getString("general.update"));
		udpateButton.setActionCommand("UPDATE");
		udpateButton.addActionListener(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(udpateButton);
		
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
						DefaultStatisticsView.this.showCompletedTasksCheckBox.setSelected(selected);
						DefaultStatisticsView.this.updateStatistics();
					}
					
				});
		
		this.showCompletedTasksCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.SETTINGS.setBooleanProperty(
						"tasksearcher.show_completed_tasks",
						DefaultStatisticsView.this.showCompletedTasksCheckBox.isSelected());
			}
			
		});
		
		topPanel.add(this.showCompletedTasksCheckBox, BorderLayout.WEST);
	}
	
}
