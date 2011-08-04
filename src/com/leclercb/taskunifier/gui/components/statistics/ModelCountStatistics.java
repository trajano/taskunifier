package com.leclercb.taskunifier.gui.components.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class ModelCountStatistics extends JPanel implements Statistics {
	
	private DefaultPieDataset dataset;
	
	public ModelCountStatistics() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.dataset = new DefaultPieDataset();
		
		this.updateStatistics();
		
		JFreeChart chart = ChartFactory.createPieChart(
				Translations.getString("statistics.model_count"),
				this.dataset,
				true,
				true,
				false);
		
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(chartPanel, BorderLayout.CENTER);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void updateStatistics() {
		this.dataset.clear();
		
		boolean showCompleted = Main.SETTINGS.getBooleanProperty("tasksearcher.show_completed_tasks");
		
		for (ModelType type : ModelType.values()) {
			int count = 0;
			List models = ModelFactoryUtils.getFactory(type).getList();
			for (Object model : models) {
				if (((Model) model).getModelStatus().isEndUserStatus())
					if (!(model instanceof Task)
							|| showCompleted
							|| !((Task) model).isCompleted())
						count++;
			}
			
			this.dataset.setValue(
					TranslationsUtils.translateModelType(type, true),
					count);
		}
	}
	
}
