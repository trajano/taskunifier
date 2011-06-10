package com.leclercb.taskunifier.gui.components.searchertree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
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
	
	private SearcherCategory generalCategory;
	private SearcherCategory contextCategory;
	private SearcherCategory folderCategory;
	private SearcherCategory goalCategory;
	private SearcherCategory locationCategory;
	private SearcherCategory personalCategory;
	
	public SearcherTreeModel() {
		super(new DefaultMutableTreeNode("Root"));
		
		this.initializeGeneralCategory();
		this.initializeContextCategory();
		this.initializeFolderCategory();
		this.initializeGoalCategory();
		this.initializeLocationCategory();
		this.initializePersonalCategory();
		
		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);
	}
	
	public SearcherNode getDefaultSearcherElement() {
		return (SearcherNode) this.generalCategory.getChildAt(0);
	}
	
	private void initializeGeneralCategory() {
		this.generalCategory = new SearcherCategory(
				Translations.getString("searcherlist.general"),
		"searcher.category.general.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.generalCategory);
		
		this.generalCategory.add(new SearcherItem(Constants.DEFAULT_SEARCHER));
		
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
				this.contextCategory.add(new ModelItem(ModelType.CONTEXT, context));
		
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
				this.locationCategory.add(new ModelItem(ModelType.LOCATION, location));
		
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
	
	private SearcherNode findItemFromModel(Model model) {
		
	}
	
	private SearcherNode findItemFromSearcher(TaskSearcher searcher) {
		
	}
	
	private SearcherCategory getCategoryFromTaskSearcherType(TaskSearcherType type) {
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
			SearcherCategory category = this.getCategoryFromModelType(((Model) event.getValue()).getModelType());
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				ModelItem item = new ModelItem(
						((Model) event.getValue()).getModelType(),
						(Model) event.getValue());
				
				category.add(item);
				this.list.setSelectedItem(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				SearcherNode item = this.findItemFromModel((Model) event.getValue());
				
				if (item != null) {
					category.remove(item);
					this.selectDefaultTaskSearcher();
				}
			}
			
			return;
		}
		
		if (event.getValue() instanceof TaskSearcher) {
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				TaskSearcher searcher = (TaskSearcher) event.getValue();
				SearcherItem item = new SearcherItem(searcher);
				
				SearcherCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
				
				this.model.addItemToCategory(item, category);
				
				this.list.setSelectedItem(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				TaskSearcher searcher = (TaskSearcher) event.getValue();
				SourceListItem item = this.findItemFromSearcher(searcher);
				
				if (item != null) {
					SourceListCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
					
					this.model.removeItemFromCategory(item, category);
					this.selectDefaultTaskSearcher();
				}
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
			SourceListCategory category = this.getCategoryFromModel((Model) event.getSource());
			
			if (!((Model) event.getSource()).getModelStatus().equals(
					ModelStatus.LOADED)
					&& !((Model) event.getSource()).getModelStatus().equals(
							ModelStatus.TO_UPDATE)) {
				SourceListItem item = this.findItemFromModel((Model) event.getSource());
				
				if (item != null) {
					this.model.removeItemFromCategory(item, category);
					this.selectDefaultTaskSearcher();
				}
			} else {
				if (event.getPropertyName().equals(Model.PROP_TITLE)
						|| event.getPropertyName().equals(GuiModel.PROP_COLOR)) {
					SourceListItem item = this.findItemFromModel((Model) event.getSource());
					
					if (item != null) {
						this.model.removeItemFromCategory(item, category);
						this.selectDefaultTaskSearcher();
					}
					
					item = new ModelItem(
							((Model) event.getSource()).getModelType(),
							(Model) event.getSource());
					
					this.model.addItemToCategory(item, category);
					this.list.setSelectedItem(item);
				}
			}
			
			return;
		}
		
		if (event.getSource() instanceof TaskSearcher) {
			if (event.getPropertyName().equals(TaskSearcher.PROP_TITLE)
					|| event.getPropertyName().equals(TaskSearcher.PROP_ICON)) {
				TaskSearcher searcher = (TaskSearcher) event.getSource();
				SourceListItem item = this.findItemFromSearcher(searcher);
				
				SourceListCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
				
				if (item != null) {
					this.model.removeItemFromCategory(item, category);
					this.selectDefaultTaskSearcher();
				}
				
				item = new SearcherItem(searcher);
				
				this.model.addItemToCategory(item, category);
				this.list.setSelectedItem(item);
			}
			
			if (event.getPropertyName().equals(TaskSearcher.PROP_TYPE)) {
				TaskSearcher searcher = (TaskSearcher) event.getSource();
				SourceListItem item = this.findItemFromSearcher(searcher);
				
				SourceListCategory category = this.getCategoryFromTaskSearcherType((TaskSearcherType) event.getOldValue());
				
				if (item != null && category != null) {
					this.model.removeItemFromCategory(item, category);
					this.selectDefaultTaskSearcher();
				}
				
				category = this.getCategoryFromTaskSearcherType(searcher.getType());
				
				item = new SearcherItem(searcher);
				
				this.model.addItemToCategory(item, category);
				this.list.setSelectedItem(item);
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
