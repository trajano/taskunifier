package com.leclercb.taskunifier.gui.components.searchertree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.EqualsUtils;
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
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.comparators.TaskSearcherComparator;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.ModelItem;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherItem;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherNode;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SearcherTreeModel extends DefaultTreeModel implements ListChangeListener, PropertyChangeListener {
	
	private SearcherNode defaultSearcher;
	private SearcherCategory generalCategory;
	private SearcherCategory contextCategory;
	private SearcherCategory folderCategory;
	private SearcherCategory goalCategory;
	private SearcherCategory locationCategory;
	private SearcherCategory personalCategory;
	
	public SearcherTreeModel() {
		super(new DefaultMutableTreeNode("Root"));
		
		this.initializeDefaultSearcher();
		this.initializeGeneralCategory();
		this.initializeContextCategory();
		this.initializeFolderCategory();
		this.initializeGoalCategory();
		this.initializeLocationCategory();
		this.initializePersonalCategory();
		
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
		
		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);
	}
	
	public SearcherNode getDefaultSearcher() {
		return this.defaultSearcher;
	}
	
	public SearcherCategory[] getCategories() {
		return new SearcherCategory[] {
				this.generalCategory,
				this.contextCategory,
				this.folderCategory,
				this.goalCategory,
				this.locationCategory,
				this.personalCategory };
	}
	
	private void initializeDefaultSearcher() {
		this.defaultSearcher = new SearcherItem(Constants.DEFAULT_SEARCHER);
		((DefaultMutableTreeNode) this.getRoot()).add(this.defaultSearcher);
	}
	
	private void initializeGeneralCategory() {
		this.generalCategory = new SearcherCategory(
				Translations.getString("searcherlist.general"),
				"searcher.category.general.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.generalCategory);
		
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				TaskSearcherFactory.getInstance().getList());
		Collections.sort(searchers, new TaskSearcherComparator());
		
		for (TaskSearcher searcher : searchers)
			if (searcher.getType() == TaskSearcherType.GENERAL)
				this.generalCategory.add(new SearcherItem(searcher));
	}
	
	private void initializeContextCategory() {
		this.contextCategory = new SearcherCategory(
				Translations.getString("general.contexts"),
				"searcher.category.context.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.contextCategory);
		
		this.contextCategory.add(new ModelItem(ModelType.CONTEXT, null));
		
		List<Context> contexts = new ArrayList<Context>(
				ContextFactory.getInstance().getList());
		Collections.sort(contexts, new ModelComparator());
		
		for (Context context : contexts)
			if (context.getModelStatus().equals(ModelStatus.LOADED)
					|| context.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.contextCategory.add(new ModelItem(
						ModelType.CONTEXT,
						context));
		
		ContextFactory.getInstance().addListChangeListener(this);
		ContextFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeFolderCategory() {
		this.folderCategory = new SearcherCategory(
				Translations.getString("general.folders"),
				"searcher.category.folder.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.folderCategory);
		
		this.folderCategory.add(new ModelItem(ModelType.FOLDER, null));
		
		List<Folder> folders = new ArrayList<Folder>(
				FolderFactory.getInstance().getList());
		Collections.sort(folders, new ModelComparator());
		
		for (Folder folder : folders)
			if (folder.getModelStatus().equals(ModelStatus.LOADED)
					|| folder.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.folderCategory.add(new ModelItem(ModelType.FOLDER, folder));
		
		FolderFactory.getInstance().addListChangeListener(this);
		FolderFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeGoalCategory() {
		this.goalCategory = new SearcherCategory(
				Translations.getString("general.goals"),
				"searcher.category.goal.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.goalCategory);
		
		this.goalCategory.add(new ModelItem(ModelType.GOAL, null));
		
		List<Goal> goals = new ArrayList<Goal>(
				GoalFactory.getInstance().getList());
		Collections.sort(goals, new ModelComparator());
		
		for (Goal goal : goals)
			if (goal.getModelStatus().equals(ModelStatus.LOADED)
					|| goal.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.goalCategory.add(new ModelItem(ModelType.GOAL, goal));
		
		GoalFactory.getInstance().addListChangeListener(this);
		GoalFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeLocationCategory() {
		this.locationCategory = new SearcherCategory(
				Translations.getString("general.locations"),
				"searcher.category.location.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.locationCategory);
		
		this.locationCategory.add(new ModelItem(ModelType.LOCATION, null));
		
		List<Location> locations = new ArrayList<Location>(
				LocationFactory.getInstance().getList());
		Collections.sort(locations, new ModelComparator());
		
		for (Location location : locations)
			if (location.getModelStatus().equals(ModelStatus.LOADED)
					|| location.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.locationCategory.add(new ModelItem(
						ModelType.LOCATION,
						location));
		
		LocationFactory.getInstance().addListChangeListener(this);
		LocationFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializePersonalCategory() {
		this.personalCategory = new SearcherCategory(
				Translations.getString("searcherlist.personal"),
				"searcher.category.personal.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.personalCategory);
		
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				TaskSearcherFactory.getInstance().getList());
		Collections.sort(searchers, new TaskSearcherComparator());
		
		for (TaskSearcher searcher : searchers)
			if (searcher.getType() == TaskSearcherType.PERSONAL)
				this.personalCategory.add(new SearcherItem(searcher));
	}
	
	public SearcherNode findItemFromModel(Model model) {
		SearcherCategory category = this.getCategoryFromModelType(model.getModelType());
		
		for (int i = 0; i < category.getChildCount(); i++) {
			TreeNode node = category.getChildAt(i);
			if (node instanceof ModelItem) {
				if (EqualsUtils.equals(((ModelItem) node).getModel(), model)) {
					return (ModelItem) node;
				}
			}
		}
		
		return null;
	}
	
	public SearcherNode findItemFromSearcher(TaskSearcher searcher) {
		SearcherCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
		
		for (int i = 0; i < category.getChildCount(); i++) {
			TreeNode node = category.getChildAt(i);
			if (node instanceof SearcherItem) {
				if (EqualsUtils.equals(
						((SearcherItem) node).getTaskSearcher(),
						searcher)) {
					return (SearcherItem) node;
				}
			}
		}
		
		return null;
	}
	
	private SearcherCategory getCategoryFromTaskSearcherType(
			TaskSearcherType type) {
		switch (type) {
			case GENERAL:
				return this.generalCategory;
			case CONTEXT:
				return this.contextCategory;
			case FOLDER:
				return this.folderCategory;
			case GOAL:
				return this.goalCategory;
			case LOCATION:
				return this.locationCategory;
			case PERSONAL:
				return this.personalCategory;
		}
		
		return null;
	}
	
	private SearcherCategory getCategoryFromModelType(ModelType type) {
		switch (type) {
			case CONTEXT:
				return this.contextCategory;
			case FOLDER:
				return this.folderCategory;
			case GOAL:
				return this.goalCategory;
			case LOCATION:
				return this.locationCategory;
		}
		
		return null;
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getValue() instanceof Task) {
			if (!Synchronizing.isSynchronizing())
				this.updateBadges();
			return;
		}
		
		if (event.getValue() instanceof Model) {
			Model model = (Model) event.getValue();
			SearcherCategory category = this.getCategoryFromModelType(model.getModelType());
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				ModelItem item = new ModelItem(model.getModelType(), model);
				
				category.add(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				SearcherNode item = this.findItemFromModel(model);
				
				if (item != null)
					category.remove(item);
			}
			
			return;
		}
		
		if (event.getValue() instanceof TaskSearcher) {
			TaskSearcher searcher = (TaskSearcher) event.getValue();
			SearcherCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				SearcherItem item = new SearcherItem(searcher);
				
				category.add(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				SearcherNode item = this.findItemFromSearcher(searcher);
				
				if (item != null)
					category.remove(item);
			}
			
			return;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof Task) {
			if (!Synchronizing.isSynchronizing())
				this.updateBadges();
			return;
		}
		
		if (event.getSource() instanceof Model) {
			Model model = (Model) event.getSource();
			SearcherCategory category = this.getCategoryFromModelType(model.getModelType());
			SearcherNode item = this.findItemFromModel(model);
			
			if (!((Model) event.getSource()).getModelStatus().equals(
					ModelStatus.LOADED)
					&& !((Model) event.getSource()).getModelStatus().equals(
							ModelStatus.TO_UPDATE)) {
				if (item != null)
					category.remove(item);
			} else {
				if (event.getPropertyName().equals(Model.PROP_TITLE)
						|| event.getPropertyName().equals(GuiModel.PROP_COLOR)) {
					this.nodeChanged(item);
				}
			}
			
			return;
		}
		
		if (event.getSource() instanceof TaskSearcher) {
			TaskSearcher searcher = (TaskSearcher) event.getSource();
			SearcherNode item = this.findItemFromSearcher(searcher);
			
			if (event.getPropertyName().equals(TaskSearcher.PROP_TITLE)
					|| event.getPropertyName().equals(TaskSearcher.PROP_ICON)) {
				this.nodeChanged(item);
			}
			
			if (event.getPropertyName().equals(TaskSearcher.PROP_TYPE)) {
				SearcherCategory category = null;
				
				category = this.getCategoryFromTaskSearcherType((TaskSearcherType) event.getOldValue());
				
				if (item != null && category != null)
					category.remove(item);
				
				category = this.getCategoryFromTaskSearcherType(searcher.getType());
				
				item = new SearcherItem(searcher);
				
				category.add(item);
			}
			
			return;
		}
	}
	
	public void updateBadges() {
		Boolean showBadges = Main.SETTINGS.getBooleanProperty("searcher.show_badges");
		if (showBadges == null || !showBadges)
			return;
		
		this.nodeChanged((TreeNode) this.getRoot());
	}
	
}
