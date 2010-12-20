package com.leclercb.taskunifier.gui.components.searcherlist.nodes;

import javax.swing.SortOrder;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.utils.EqualsBuilder;
import com.leclercb.taskunifier.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.ModelCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ModelTreeNode extends DefaultMutableTreeNode implements TaskSearcherTreeNode {
	
	public ModelTreeNode(Model model) {
		super(model);
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		Model model = (Model) this.getUserObject();
		TaskColumn column = null;
		
		if (model instanceof Context)
			column = TaskColumn.CONTEXT;
		else if (model instanceof Folder)
			column = TaskColumn.FOLDER;
		else if (model instanceof Goal)
			column = TaskColumn.GOAL;
		else if (model instanceof Location)
			column = TaskColumn.LOCATION;
		
		TaskSorter sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		
		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				column,
				ModelCondition.EQUALS,
				model));
		
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
