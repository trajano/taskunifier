package com.leclercb.taskunifier.gui.components.views.statistics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.components.statistics.ModelCountStatistics;
import com.leclercb.taskunifier.gui.components.statistics.TasksPerStatusStatistics;
import com.leclercb.taskunifier.gui.components.statistics.TasksPerTagStatistics;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class DefaultStatisticsView extends JPanel implements StatisticsView {
	
	private ModelCountStatistics modelStatistics;
	private TasksPerStatusStatistics tasksPerStatusStatistics;
	private TasksPerTagStatistics tasksPerTagStatistics;
	
	public DefaultStatisticsView(MainView mainView) {
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
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.modelStatistics = new ModelCountStatistics();
		this.tasksPerStatusStatistics = new TasksPerStatusStatistics();
		this.tasksPerTagStatistics = new TasksPerTagStatistics();
		
		panel.add(this.modelStatistics);
		panel.add(this.tasksPerStatusStatistics);
		panel.add(this.tasksPerTagStatistics);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultStatisticsView.this.modelStatistics.updateStatistics();
				DefaultStatisticsView.this.tasksPerStatusStatistics.updateStatistics();
				DefaultStatisticsView.this.tasksPerTagStatistics.updateStatistics();
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
