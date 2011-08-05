package com.leclercb.taskunifier.gui.components.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.utils.TaskTagList;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TasksPerTagStatistics extends JPanel implements Statistics {
	
	private DefaultPieDataset dataset;
	
	public TasksPerTagStatistics() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.dataset = new DefaultPieDataset();
		
		this.updateStatistics();
		
		JFreeChart chart = ChartFactory.createPieChart(
				Translations.getString("statistics.tasks_per_tag"),
				this.dataset,
				true,
				true,
				false);
		
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setLabelFont(new Font(null, Font.PLAIN, 10));
		plot.setIgnoreZeroValues(true);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(chartPanel, BorderLayout.CENTER);
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}
	
	@Override
	public void updateStatistics() {
		this.dataset.clear();
		
		boolean showCompleted = Main.SETTINGS.getBooleanProperty("tasksearcher.show_completed_tasks");
		
		for (Tag tag : TaskTagList.getInstance().getTags()) {
			int count = 0;
			List<Task> tasks = TaskFactory.getInstance().getList();
			for (Task task : tasks) {
				if (task.getModelStatus().isEndUserStatus())
					if (task.getTags().containsTag(tag))
						if (showCompleted || !task.isCompleted())
							count++;
			}
			
			this.dataset.setValue(tag, count);
		}
	}
	
}
