/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.tasksearchertree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.utils.TaskTagList;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.comparators.TaskSearcherComparator;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.ModelItem;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.SearcherItem;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.SearcherNode;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.TagItem;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;

public class TaskSearcherTreeModel extends DefaultTreeModel implements ListChangeListener, PropertyChangeListener {
	
	private TreeSelectionModel treeSelectionModel;
	
	private String settingsPrefix;
	
	private SearcherItem defaultSearcher;
	private SearcherCategory generalCategory;
	private SearcherCategory contextCategory;
	private SearcherCategory folderCategory;
	private SearcherCategory goalCategory;
	private SearcherCategory locationCategory;
	private SearcherCategory tagCategory;
	private SearcherCategory personalCategory;
	
	public TaskSearcherTreeModel(
			String settingsPrefix,
			TreeSelectionModel treeSelectionModel) {
		super(new SearcherCategory(TaskSearcherType.DEFAULT, null));
		
		this.settingsPrefix = settingsPrefix;
		
		this.treeSelectionModel = treeSelectionModel;
		
		this.initializeDefaultSearcher();
		this.initializeGeneralCategory();
		this.initializeContextCategory();
		this.initializeFolderCategory();
		this.initializeGoalCategory();
		this.initializeLocationCategory();
		this.initializeTagCategory();
		this.initializePersonalCategory();
		
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
		
		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);
		
		Main.SETTINGS.addPropertyChangeListener(
				"tasksearcher.show_completed_tasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TaskSearcherTreeModel.this.updateBadges();
					}
					
				});
	}
	
	public SearcherItem getDefaultSearcher() {
		return this.defaultSearcher;
	}
	
	public SearcherCategory[] getCategories() {
		return new SearcherCategory[] {
				this.generalCategory,
				this.contextCategory,
				this.folderCategory,
				this.goalCategory,
				this.locationCategory,
				this.tagCategory,
				this.personalCategory };
	}
	
	private void initializeDefaultSearcher() {
		this.defaultSearcher = new SearcherItem(
				Constants.getDefaultTaskSearcher());
		((DefaultMutableTreeNode) this.getRoot()).add(this.defaultSearcher);
	}
	
	private void initializeGeneralCategory() {
		this.generalCategory = new SearcherCategory(
				TaskSearcherType.GENERAL,
				this.settingsPrefix + ".category.general.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.generalCategory);
		
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				TaskSearcherFactory.getInstance().getList());
		Collections.sort(searchers, TaskSearcherComparator.INSTANCE);
		
		for (TaskSearcher searcher : searchers)
			if (searcher.getType() == TaskSearcherType.GENERAL)
				this.generalCategory.add(new SearcherItem(searcher));
	}
	
	private void initializeContextCategory() {
		this.contextCategory = new SearcherCategory(
				TaskSearcherType.CONTEXT,
				this.settingsPrefix + ".category.context.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.contextCategory);
		
		this.contextCategory.add(new ModelItem(ModelType.CONTEXT, null));
		
		List<Context> contexts = new ArrayList<Context>(
				ContextFactory.getInstance().getList());
		Collections.sort(contexts, ModelComparator.INSTANCE);
		
		for (Context context : contexts)
			if (context.getModelStatus().isEndUserStatus())
				this.contextCategory.add(new ModelItem(
						ModelType.CONTEXT,
						context));
		
		ContextFactory.getInstance().addListChangeListener(this);
		ContextFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeFolderCategory() {
		this.folderCategory = new SearcherCategory(
				TaskSearcherType.FOLDER,
				this.settingsPrefix + ".category.folder.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.folderCategory);
		
		this.folderCategory.add(new ModelItem(ModelType.FOLDER, null));
		
		List<Folder> folders = new ArrayList<Folder>(
				FolderFactory.getInstance().getList());
		Collections.sort(folders, ModelComparator.INSTANCE);
		
		for (Folder folder : folders)
			if (folder.getModelStatus().isEndUserStatus())
				if (!folder.isArchived())
					this.folderCategory.add(new ModelItem(
							ModelType.FOLDER,
							folder));
		
		FolderFactory.getInstance().addListChangeListener(this);
		FolderFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeGoalCategory() {
		this.goalCategory = new SearcherCategory(
				TaskSearcherType.GOAL,
				this.settingsPrefix + ".category.goal.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.goalCategory);
		
		this.goalCategory.add(new ModelItem(ModelType.GOAL, null));
		
		List<Goal> goals = new ArrayList<Goal>(
				GoalFactory.getInstance().getList());
		Collections.sort(goals, ModelComparator.INSTANCE);
		
		for (Goal goal : goals)
			if (goal.getModelStatus().isEndUserStatus())
				this.goalCategory.add(new ModelItem(ModelType.GOAL, goal));
		
		GoalFactory.getInstance().addListChangeListener(this);
		GoalFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeLocationCategory() {
		this.locationCategory = new SearcherCategory(
				TaskSearcherType.LOCATION,
				this.settingsPrefix + ".category.location.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.locationCategory);
		
		this.locationCategory.add(new ModelItem(ModelType.LOCATION, null));
		
		List<Location> locations = new ArrayList<Location>(
				LocationFactory.getInstance().getList());
		Collections.sort(locations, ModelComparator.INSTANCE);
		
		for (Location location : locations)
			if (location.getModelStatus().isEndUserStatus())
				this.locationCategory.add(new ModelItem(
						ModelType.LOCATION,
						location));
		
		LocationFactory.getInstance().addListChangeListener(this);
		LocationFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeTagCategory() {
		this.tagCategory = new SearcherCategory(
				TaskSearcherType.TAG,
				this.settingsPrefix + ".category.tag.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.tagCategory);
		
		TagList tags = TaskTagList.getInstance().getTags();
		
		for (Tag tag : tags)
			this.tagCategory.add(new TagItem(tag));
		
		TaskTagList.getInstance().addListChangeListener(this);
	}
	
	private void initializePersonalCategory() {
		this.personalCategory = new SearcherCategory(
				TaskSearcherType.PERSONAL,
				this.settingsPrefix + ".category.personal.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.personalCategory);
		
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				TaskSearcherFactory.getInstance().getList());
		Collections.sort(searchers, TaskSearcherComparator.INSTANCE);
		
		for (TaskSearcher searcher : searchers)
			if (searcher.getType() == TaskSearcherType.PERSONAL)
				this.personalCategory.add(new SearcherItem(searcher));
	}
	
	public int findNewIndexInModelCategory(
			SearcherCategory category,
			Model model) {
		List<Model> models = new ArrayList<Model>();
		for (int i = 0; i < category.getChildCount(); i++) {
			TreeNode node = category.getChildAt(i);
			if (node instanceof ModelItem) {
				models.add(((ModelItem) node).getModel());
			}
		}
		
		models.add(model);
		Collections.sort(models, ModelComparator.INSTANCE);
		
		return models.indexOf(model);
	}
	
	public ModelItem findItemFromModel(Model model) {
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
	
	public SearcherItem findItemFromSearcher(TaskSearcher searcher) {
		return this.findItemFromSearcher(searcher, searcher.getType());
	}
	
	private SearcherItem findItemFromSearcher(
			TaskSearcher searcher,
			TaskSearcherType type) {
		SearcherCategory category = this.getCategoryFromTaskSearcherType(type);
		
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
	
	public TagItem findItemFromTag(Tag tag) {
		for (int i = 0; i < this.tagCategory.getChildCount(); i++) {
			TreeNode node = this.tagCategory.getChildAt(i);
			if (node instanceof TagItem) {
				if (((TagItem) node).getTag().equals(tag)) {
					return (TagItem) node;
				}
			}
		}
		
		return null;
	}
	
	private SearcherCategory getCategoryFromTaskSearcherType(
			TaskSearcherType type) {
		switch (type) {
			case DEFAULT:
				return (SearcherCategory) this.getRoot();
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
			case TAG:
				return this.tagCategory;
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
				if (!model.getModelStatus().isEndUserStatus())
					return;
				
				if (event.getValue() instanceof Folder)
					if (((Folder) event.getValue()).isArchived())
						return;
				
				ModelItem item = new ModelItem(model.getModelType(), model);
				
				try {
					this.insertNodeInto(
							item,
							category,
							this.findNewIndexInModelCategory(category, model));
				} catch (Exception e) {
					this.insertNodeInto(item, category, 0);
				}
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				ModelItem item = this.findItemFromModel(model);
				
				if (item != null)
					this.removeNodeFromParent(item);
			}
			
			this.updateSelection();
			return;
		}
		
		if (event.getValue() instanceof TaskSearcher) {
			TaskSearcher searcher = (TaskSearcher) event.getValue();
			SearcherCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				SearcherItem item = new SearcherItem(searcher);
				
				this.insertNodeInto(item, category, 0);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				SearcherItem item = this.findItemFromSearcher(searcher);
				
				if (item != null)
					this.removeNodeFromParent(item);
			}
			
			this.updateSelection();
			return;
		}
		
		if (event.getValue() instanceof Tag) {
			Tag tag = (Tag) event.getValue();
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				TagItem item = new TagItem(tag);
				
				this.insertNodeInto(item, this.tagCategory, event.getIndex());
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				TagItem item = this.findItemFromTag(tag);
				
				if (item != null)
					this.removeNodeFromParent(item);
			}
			
			this.updateSelection();
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
			ModelItem item = this.findItemFromModel(model);
			
			if (!((Model) event.getSource()).getModelStatus().isEndUserStatus()) {
				if (item != null)
					this.removeNodeFromParent(item);
			} else if (event.getSource() instanceof Folder
					&& ((Folder) event.getSource()).isArchived()) {
				if (item != null)
					this.removeNodeFromParent(item);
			} else if (item == null) {
				item = new ModelItem(model.getModelType(), model);
				
				try {
					this.insertNodeInto(
							item,
							category,
							this.findNewIndexInModelCategory(category, model));
				} catch (Exception e) {
					this.insertNodeInto(item, category, 0);
				}
			} else if (event.getPropertyName().equals(Model.PROP_TITLE)) {
				this.removeNodeFromParent(item);
				
				try {
					this.insertNodeInto(
							item,
							category,
							this.findNewIndexInModelCategory(category, model));
				} catch (Exception e) {
					this.insertNodeInto(item, category, 0);
				}
			} else if (event.getPropertyName().equals(GuiModel.PROP_COLOR)) {
				this.nodeChanged(item);
			}
			
			this.updateSelection();
			return;
		}
		
		if (event.getSource() instanceof TaskSearcher) {
			TaskSearcher searcher = (TaskSearcher) event.getSource();
			
			if (event.getPropertyName().equals(TaskSearcher.PROP_TITLE)
					|| event.getPropertyName().equals(TaskSearcher.PROP_ICON)) {
				SearcherItem item = this.findItemFromSearcher(searcher);
				this.nodeChanged(item);
			}
			
			if (event.getPropertyName().equals(TaskSearcher.PROP_TYPE)) {
				SearcherItem item = this.findItemFromSearcher(
						searcher,
						(TaskSearcherType) event.getOldValue());
				
				if (item != null)
					this.removeNodeFromParent(item);
				
				SearcherCategory category = this.getCategoryFromTaskSearcherType(searcher.getType());
				
				item = new SearcherItem(searcher);
				
				this.insertNodeInto(item, category, 0);
			}
			
			this.updateSelection();
			return;
		}
	}
	
	public void updateBadges() {
		this.defaultSearcher.updateBadgeCount();
		
		SearcherCategory[] categories = this.getCategories();
		for (SearcherCategory category : categories) {
			for (int i = 0; i < category.getChildCount(); i++)
				((SearcherNode) category.getChildAt(i)).updateBadgeCount();
		}
		
		this.nodeChanged((TreeNode) this.getRoot());
	}
	
	private void updateSelection() {
		if (this.treeSelectionModel.getSelectionPath() == null)
			this.treeSelectionModel.setSelectionPath(TreeUtils.getPath(this.getDefaultSearcher()));
	}
	
}
