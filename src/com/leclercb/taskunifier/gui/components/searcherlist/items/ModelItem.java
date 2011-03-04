package com.leclercb.taskunifier.gui.components.searcherlist.items;

import java.util.List;

import javax.swing.SortOrder;

import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.api.GuiFolder;
import com.leclercb.taskunifier.gui.components.searcherlist.TaskSearcherElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.ModelCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ColorBadgeIcon;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ModelItem extends SourceListItem implements TaskSearcherElement {
	
	private ModelType modelType;
	private Model model;
	
	public ModelItem(ModelType modelType, Model model) {
		super(
				(model == null ? Translations.getString("searcherlist.none") : model.getTitle()));
		
		if (model instanceof GuiFolder)
			this.setIcon(new ColorBadgeIcon(
					((GuiFolder) model).getColor(),
					16,
					16));
		
		if (model == null && modelType == ModelType.FOLDER)
			this.setIcon(new ColorBadgeIcon(null, 16, 16));
		
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
		TaskColumn column = null;
		
		switch (this.modelType) {
			case CONTEXT:
				column = TaskColumn.CONTEXT;
				break;
			case FOLDER:
				column = TaskColumn.FOLDER;
				break;
			case GOAL:
				column = TaskColumn.GOAL;
				break;
			case LOCATION:
				column = TaskColumn.LOCATION;
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
				&& !Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks"))
			filter.addElement(new TaskFilterElement(
					TaskColumn.COMPLETED,
					StringCondition.EQUALS,
					"false"));
		
		String title = (this.model == null ? Translations.getString("searcherlist.none") : this.model.getTitle());
		TaskSearcher searcher = new TaskSearcher(title, filter, sorter);
		
		return searcher;
	}
}
