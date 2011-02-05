package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.SortOrder;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsBuilder;
import com.leclercb.commons.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.Main;
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

public class ModelTreeNode extends DefaultMutableTreeNode implements TaskSearcherElement {
	
	private ModelType modelType;
	
	public ModelTreeNode(ModelType modelType, Model model) {
		super(model);
		
		CheckUtils.isNotNull(modelType, "Model type cannot be null");
		this.modelType = modelType;
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		Model model = (Model) this.getUserObject();
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
				model));
		
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks") != null
				&& !Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks"))
			filter.addElement(new TaskFilterElement(
					TaskColumn.COMPLETED,
					StringCondition.EQUALS,
					"false"));
		
		String title = (model == null ? Translations.getString("searcherlist.none") : model.getTitle());
		TaskSearcher searcher = new TaskSearcher(title, filter, sorter);
		
		return searcher;
	}
	
	@Override
	public String toString() {
		if (this.getUserObject() == null)
			return Translations.getString("searcherlist.none");
		
		return this.getUserObject().toString();
	}
	
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof ModelTreeNode) {
			ModelTreeNode node = (ModelTreeNode) o;
			
			return new EqualsBuilder().append(
					this.getUserObject(),
					node.getUserObject()).isEqual();
		}
		
		return false;
	}
	
	@Override
	public final int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.getUserObject());
		
		return hashCode.toHashCode();
	}
	
}
