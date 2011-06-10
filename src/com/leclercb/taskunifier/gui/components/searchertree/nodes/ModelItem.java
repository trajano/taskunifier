package com.leclercb.taskunifier.gui.components.searchertree.nodes;

import java.util.List;

import javax.swing.Icon;
import javax.swing.SortOrder;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.swing.ColorBadgeIcon;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ModelItem extends DefaultMutableTreeNode implements SearcherNode {
	
	private Icon icon;
	
	private ModelType modelType;
	
	public ModelItem(ModelType modelType, Model model) {
		super(model);
		
		CheckUtils.isNotNull(modelType, "Model type cannot be null");
		
		this.modelType = modelType;
		
		if (model != null && model instanceof GuiModel)
			this.icon = new ColorBadgeIcon(
					((GuiModel) model).getColor(),
					12,
					12);
		else
			this.icon = new ColorBadgeIcon(null, 12, 12);
	}
	
	public ModelType getModelType() {
		return this.modelType;
	}
	
	public Model getModel() {
		return (Model) this.getUserObject();
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		Template template = new Template("ModelTemplate");
		TaskColumn column = null;
		TaskSearcherType type = null;
		
		switch (this.modelType) {
			case CONTEXT:
				column = TaskColumn.CONTEXT;
				type = TaskSearcherType.CONTEXT;
				if (this.getModel() != null)
					template.setTaskContext(this.getModel().getModelId());
				break;
			case FOLDER:
				column = TaskColumn.FOLDER;
				type = TaskSearcherType.FOLDER;
				if (this.getModel() != null)
					template.setTaskFolder(this.getModel().getModelId());
				break;
			case GOAL:
				column = TaskColumn.GOAL;
				type = TaskSearcherType.GOAL;
				if (this.getModel() != null)
					template.setTaskGoal(this.getModel().getModelId());
				break;
			case LOCATION:
				column = TaskColumn.LOCATION;
				type = TaskSearcherType.LOCATION;
				if (this.getModel() != null)
					template.setTaskLocation(this.getModel().getModelId());
				break;
		}
		
		TaskSorter sorter = new TaskSorter();
		
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				2,
				TaskColumn.PRIORITY,
				SortOrder.DESCENDING));
		sorter.addElement(new TaskSorterElement(
				3,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				column,
				ModelCondition.EQUALS,
				this.getModel()));
		
		String title = (this.getModel() == null ? Translations.getString("searcherlist.none") : this.getModel().getTitle());
		TaskSearcher searcher = new TaskSearcher(
				type,
				0,
				title,
				null,
				filter,
				sorter,
				template);
		
		return searcher;
	}
	
	@Override
	public Icon getIcon() {
		return this.icon;
	}
	
	@Override
	public String getText() {
		return (this.getModel() == null ? Translations.getString("searcherlist.none") : this.getModel().getTitle());
	}
	
	@Override
	public String getBadge() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		int count = 0;
		for (Task task : tasks)
			if (TaskUtils.showTask(task, searcher.getFilter()))
				count++;
		
		return "<html>" + count + "</html>";
	}
	
}
