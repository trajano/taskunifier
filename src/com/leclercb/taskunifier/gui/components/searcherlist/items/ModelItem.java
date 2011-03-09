package com.leclercb.taskunifier.gui.components.searcherlist.items;

import java.util.List;

import javax.swing.SortOrder;

import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.api.GuiModel;
import com.leclercb.taskunifier.gui.components.searcherlist.TaskSearcherElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.ModelCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.swing.ColorBadgeIcon;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ModelItem extends SourceListItem implements TaskSearcherElement {
	
	private ModelType modelType;
	private Model model;
	
	public ModelItem(ModelType modelType, Model model) {
		super(
				(model == null ? Translations.getString("searcherlist.none") : model.getTitle()));
		
		if (model instanceof GuiModel)
			this.setIcon(new ColorBadgeIcon(
					((GuiModel) model).getColor(),
					12,
					12));
		
		if (model == null)
			this.setIcon(new ColorBadgeIcon(null, 12, 12));
		
		this.modelType = modelType;
		this.model = model;
		this.updateBadgeCount();
	}
	
	public ModelType getModelType() {
		return this.modelType;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void updateBadgeCount() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		int count = 0;
		for (Task task : tasks)
			if (TaskUtils.showTask(task, searcher.getFilter()))
				count++;
		
		this.setCounterValue(count);
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		Template template = new Template("ModelTemplate");
		TaskColumn column = null;
		
		switch (this.modelType) {
			case CONTEXT:
				column = TaskColumn.CONTEXT;
				if (model != null)
					template.setTaskContext(model.getModelId());
				break;
			case FOLDER:
				column = TaskColumn.FOLDER;
				if (model != null)
					template.setTaskFolder(model.getModelId());
				break;
			case GOAL:
				column = TaskColumn.GOAL;
				if (model != null)
					template.setTaskGoal(model.getModelId());
				break;
			case LOCATION:
				column = TaskColumn.LOCATION;
				if (model != null)
					template.setTaskLocation(model.getModelId());
				break;
		}
		
		TaskSorter sorter = new TaskSorter();
		
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end") != null
				&& Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks_at_the_end"))
			sorter.addElement(new TaskSorterElement(
					0,
					TaskColumn.COMPLETED,
					SortOrder.ASCENDING));
		
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.PRIORITY,
				SortOrder.DESCENDING));
		sorter.addElement(new TaskSorterElement(
				2,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				column,
				ModelCondition.EQUALS,
				this.model));
		
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks") != null
				&& !Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks")) {
			filter.addElement(new TaskFilterElement(
					TaskColumn.COMPLETED,
					StringCondition.EQUALS,
			"false"));
			
			template.setTaskCompleted(false);
		}
		
		String title = (this.model == null ? Translations.getString("searcherlist.none") : this.model.getTitle());
		TaskSearcher searcher = new TaskSearcher(title, null, filter, sorter, template);
		
		return searcher;
	}
}
