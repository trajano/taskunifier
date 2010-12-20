package com.leclercb.taskunifier.gui.components.searcherlist;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.SortOrder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.CategoryTreeNode;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.ModelTreeNode;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.SearcherTreeNode;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.DaysCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.EnumCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SearcherTreeModel extends DefaultTreeModel implements ListChangeListener, PropertyChangeListener {
	
	private static final TaskSearcher[] GENERAL_TASK_SEARCHERS;
	
	static {
		GENERAL_TASK_SEARCHERS = new TaskSearcher[5];
		
		TaskFilter filter;
		TaskSorter sorter;
		
		// All Tasks
		filter = new TaskFilter();
		
		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		GENERAL_TASK_SEARCHERS[0] = new TaskSearcher(
				Translations.getString("searcherlist.general.all_tasks"),
				Images.getResourceFile("document.png"),
				filter,
				sorter);
		
		// Hot List
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.DUE_DATE,
				DaysCondition.LESS_THAN_OR_EQUALS,
				3));
		filter.addElement(new TaskFilterElement(
				TaskColumn.PRIORITY,
				EnumCondition.GREATER_THAN_OR_EQUALS,
				TaskPriority.HIGH));
		
		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		GENERAL_TASK_SEARCHERS[1] = new TaskSearcher(
				Translations.getString("searcherlist.general.hot_list"),
				Images.getResourceFile("hot_pepper.png"),
				filter,
				sorter);
		
		// Starred
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.STAR,
				StringCondition.EQUALS,
				"true"));
		
		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		GENERAL_TASK_SEARCHERS[2] = new TaskSearcher(
				Translations.getString("searcherlist.general.starred"),
				Images.getResourceFile("star.png"),
				filter,
				sorter);
		
		// Next Action
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"false"));
		filter.addElement(new TaskFilterElement(
				TaskColumn.STATUS,
				EnumCondition.EQUALS,
				TaskStatus.NEXT_ACTION));
		
		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		GENERAL_TASK_SEARCHERS[3] = new TaskSearcher(
				Translations.getString("searcherlist.general.next_action"),
				Images.getResourceFile("next.png"),
				filter,
				sorter);
		
		// Completed
		filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.COMPLETED,
				StringCondition.EQUALS,
				"true"));
		
		sorter = new TaskSorter();
		sorter.addElement(new TaskSorterElement(
				0,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		GENERAL_TASK_SEARCHERS[4] = new TaskSearcher(
				Translations.getString("searcherlist.general.completed"),
				Images.getResourceFile("check.png"),
				filter,
				sorter);
	}
	
	private CategoryTreeNode categoryGeneral;
	private CategoryTreeNode categoryContext;
	private CategoryTreeNode categoryFolder;
	private CategoryTreeNode categoryGoal;
	private CategoryTreeNode categoryLocation;
	private CategoryTreeNode categoryPersonal;
	
	public SearcherTreeModel() {
		super(new DefaultMutableTreeNode());
		
		this.initializeGeneralCategory();
		this.initializeContextCategory();
		this.initializeFolderCategory();
		this.initializeGoalCategory();
		this.initializeLocationCategory();
		this.initializePersonalCategory();
	}
	
	public TreePath getDefaultTaskSearcherPath() {
		return new TreePath(
				this.getPathToRoot(this.categoryGeneral.getChildAt(0)));
	}
	
	private void initializeGeneralCategory() {
		this.categoryGeneral = new CategoryTreeNode(
				null,
				"searcher.category.general.expanded",
				Translations.getString("searcherlist.general"));
		((DefaultMutableTreeNode) this.getRoot()).add(this.categoryGeneral);
		
		for (TaskSearcher searcher : GENERAL_TASK_SEARCHERS)
			this.categoryGeneral.add(new SearcherTreeNode(searcher));
	}
	
	private void initializeContextCategory() {
		this.categoryContext = new CategoryTreeNode(
				ModelType.CONTEXT,
				"searcher.category.context.expanded",
				Translations.getString("general.contexts"));
		((DefaultMutableTreeNode) this.getRoot()).add(this.categoryContext);
		
		this.categoryContext.add(new ModelTreeNode(ModelType.CONTEXT, null));
		
		List<Context> contexts = ContextFactory.getInstance().getList();
		for (Context context : contexts)
			if (context.getModelStatus().equals(ModelStatus.LOADED)
					|| context.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.categoryContext.add(new ModelTreeNode(
						ModelType.CONTEXT,
						context));
		
		ContextFactory.getInstance().addListChangeListener(this);
		ContextFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeFolderCategory() {
		this.categoryFolder = new CategoryTreeNode(
				ModelType.FOLDER,
				"searcher.category.folder.expanded",
				Translations.getString("general.folders"));
		((DefaultMutableTreeNode) this.getRoot()).add(this.categoryFolder);
		
		this.categoryFolder.add(new ModelTreeNode(ModelType.FOLDER, null));
		
		List<Folder> folders = FolderFactory.getInstance().getList();
		for (Folder folder : folders)
			if (folder.getModelStatus().equals(ModelStatus.LOADED)
					|| folder.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.categoryFolder.add(new ModelTreeNode(
						ModelType.FOLDER,
						folder));
		
		FolderFactory.getInstance().addListChangeListener(this);
		FolderFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeGoalCategory() {
		this.categoryGoal = new CategoryTreeNode(
				ModelType.GOAL,
				"searcher.category.goal.expanded",
				Translations.getString("general.goals"));
		((DefaultMutableTreeNode) this.getRoot()).add(this.categoryGoal);
		
		this.categoryGoal.add(new ModelTreeNode(ModelType.GOAL, null));
		
		List<Goal> goals = GoalFactory.getInstance().getList();
		for (Goal goal : goals)
			if (goal.getModelStatus().equals(ModelStatus.LOADED)
					|| goal.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.categoryGoal.add(new ModelTreeNode(ModelType.GOAL, goal));
		
		GoalFactory.getInstance().addListChangeListener(this);
		GoalFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeLocationCategory() {
		this.categoryLocation = new CategoryTreeNode(
				ModelType.LOCATION,
				"searcher.category.location.expanded",
				Translations.getString("general.locations"));
		((DefaultMutableTreeNode) this.getRoot()).add(this.categoryLocation);
		
		this.categoryLocation.add(new ModelTreeNode(ModelType.LOCATION, null));
		
		List<Location> locations = LocationFactory.getInstance().getList();
		for (Location location : locations)
			if (location.getModelStatus().equals(ModelStatus.LOADED)
					|| location.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.categoryLocation.add(new ModelTreeNode(
						ModelType.LOCATION,
						location));
		
		LocationFactory.getInstance().addListChangeListener(this);
		LocationFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializePersonalCategory() {
		this.categoryPersonal = new CategoryTreeNode(
				null,
				"searcher.category.personal.expanded",
				Translations.getString("searcherlist.personal"));
		((DefaultMutableTreeNode) this.getRoot()).add(this.categoryPersonal);
		
		for (TaskSearcher searcher : TaskSearcherFactory.getInstance().getList())
			this.categoryPersonal.add(new SearcherTreeNode(searcher));
		
		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private CategoryTreeNode getCategoryFromModel(Model model) {
		if (model instanceof Context)
			return this.categoryContext;
		else if (model instanceof Folder)
			return this.categoryFolder;
		else if (model instanceof Goal)
			return this.categoryGoal;
		else if (model instanceof Location)
			return this.categoryLocation;
		
		return null;
	}
	
	private MutableTreeNode getTreeNodeFromUserObject(Object userObject) {
		return this.getTreeNodeFromUserObject(
				(DefaultMutableTreeNode) this.getRoot(),
				userObject);
	}
	
	private MutableTreeNode getTreeNodeFromUserObject(
			DefaultMutableTreeNode parent,
			Object userObject) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			if (EqualsUtils.equals(
					((DefaultMutableTreeNode) parent.getChildAt(i)).getUserObject(),
					userObject))
				return (MutableTreeNode) parent.getChildAt(i);
			
			if (!parent.getChildAt(i).isLeaf()) {
				MutableTreeNode node = this.getTreeNodeFromUserObject(
						(DefaultMutableTreeNode) parent.getChildAt(i),
						userObject);
				
				if (node != null)
					return node;
			}
		}
		
		return null;
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getValue() instanceof Model) {
			CategoryTreeNode node = this.getCategoryFromModel((Model) event.getValue());
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				this.insertNodeInto(new ModelTreeNode(
						node.getModelType(),
						(Model) event.getValue()), node, node.getChildCount());
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				MutableTreeNode child = this.getTreeNodeFromUserObject(event.getValue());
				
				if (child != null)
					this.removeNodeFromParent(child);
			}
		}
		
		if (event.getValue() instanceof TaskSearcher) {
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				this.insertNodeInto(
						new SearcherTreeNode((TaskSearcher) event.getValue()),
						this.categoryPersonal,
						this.categoryPersonal.getChildCount());
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				MutableTreeNode child = this.getTreeNodeFromUserObject(event.getValue());
				
				if (child != null)
					this.removeNodeFromParent(child);
			}
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof Model) {
			CategoryTreeNode node = this.getCategoryFromModel((Model) event.getSource());
			
			if (!((Model) event.getSource()).getModelStatus().equals(
					ModelStatus.LOADED)
					&& !((Model) event.getSource()).getModelStatus().equals(
							ModelStatus.TO_UPDATE)) {
				MutableTreeNode child = this.getTreeNodeFromUserObject(event.getSource());
				
				if (child != null)
					this.removeNodeFromParent(child);
			} else {
				MutableTreeNode child = this.getTreeNodeFromUserObject(event.getSource());
				
				if (child == null)
					this.insertNodeInto(
							new ModelTreeNode(
									node.getModelType(),
									(Model) event.getSource()),
							node,
							node.getChildCount());
				else
					this.nodeChanged(new ModelTreeNode(
							node.getModelType(),
							(Model) event.getSource()));
			}
		}
		
		if (event.getSource() instanceof TaskSearcher) {
			if (event.getPropertyName().equals(TaskSearcher.PROP_TITLE)
					|| event.getPropertyName().equals(TaskSearcher.PROP_ICON)) {
				this.nodeChanged(new SearcherTreeNode(
						(TaskSearcher) event.getSource()));
			}
		}
	}
	
}
