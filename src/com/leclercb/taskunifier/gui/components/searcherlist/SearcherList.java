/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import com.explodingpixels.macwidgets.SourceListSelectionListener;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.properties.SavePropertiesListener;
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
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.comparators.TaskSearcherComparator;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.searcherlist.items.ModelItem;
import com.leclercb.taskunifier.gui.components.searcherlist.items.SearcherCategory;
import com.leclercb.taskunifier.gui.components.searcherlist.items.SearcherItem;
import com.leclercb.taskunifier.gui.components.searcherlist.transfer.TaskSearcherTransferHandler;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.macwidgets.SourceList;
import com.leclercb.taskunifier.gui.swing.macwidgets.SourceListToolTipProvider;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SearcherList implements SearcherView, ListChangeListener, PropertyChangeListener, SavePropertiesListener {
	
	private TaskSearcherSelectionChangeSupport taskSearcherSelectionChangeSupport;
	
	private SourceList list;
	private SourceListModel model;
	
	private SearcherCategory generalCategory;
	private SearcherCategory contextCategory;
	private SearcherCategory folderCategory;
	private SearcherCategory goalCategory;
	private SearcherCategory locationCategory;
	private SearcherCategory personalCategory;
	
	public SearcherList() {
		this.taskSearcherSelectionChangeSupport = new TaskSearcherSelectionChangeSupport(
				this);
		
		this.initialize();
	}
	
	private void initialize() {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
		
		Synchronizing.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!(Boolean) evt.getNewValue())
					SearcherList.this.updateBadges();
			}
			
		});
		
		this.model = new SourceListModel();
		
		this.initializeGeneralCategory();
		this.initializeContextCategory();
		this.initializeFolderCategory();
		this.initializeGoalCategory();
		this.initializeLocationCategory();
		this.initializePersonalCategory();
		
		this.list = new SourceList(this.model);
		
		this.initializeToolTipText();
		this.initializeCopyAndPaste();
		this.initializeExpandedState();
		
		this.list.addSourceListSelectionListener(new SourceListSelectionListener() {
			
			@Override
			public void sourceListItemSelected(SourceListItem e) {
				SearcherList.this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(SearcherList.this.getSelectedTaskSearcher());
			}
			
		});
		
		this.updateBadges();
	}
	
	private void initializeToolTipText() {
		SourceListToolTipProvider toolTipProvider = new SourceListToolTipProvider() {
			
			@Override
			public String getTooltip(SourceListItem item) {
				TaskSearcher searcher = null;
				
				if (item instanceof ModelItem)
					searcher = ((ModelItem) item).getTaskSearcher();
				
				if (item instanceof SearcherItem)
					searcher = ((SearcherItem) item).getTaskSearcher();
				
				if (searcher == null)
					return null;
				
				return "<html>"
						+ searcher.getTitle()
						+ "<br />"
						+ searcher.getSorter()
						+ "<br />"
						+ searcher.getFilter()
						+ "</html>";
			}
			
			@Override
			public String getTooltip(SourceListCategory category) {
				return null;
			}
			
		};
		
		this.list.setToolTipProvider(toolTipProvider);
	}
	
	private void initializeCopyAndPaste() {
		this.list.setTransferHandler(new TaskSearcherTransferHandler(this));
		
		JComponent component = this.list.getComponent();
		
		ActionMap amap = component.getActionMap();
		amap.put(
				TransferHandler.getCutAction().getValue(Action.NAME),
				TransferHandler.getCutAction());
		amap.put(
				TransferHandler.getCopyAction().getValue(Action.NAME),
				TransferHandler.getCopyAction());
		amap.put(
				TransferHandler.getPasteAction().getValue(Action.NAME),
				TransferHandler.getPasteAction());
		
		InputMap imap = component.getInputMap();
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_X,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getCutAction().getValue(Action.NAME));
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_C,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getCopyAction().getValue(Action.NAME));
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_V,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getPasteAction().getValue(Action.NAME));
	}
	
	private void initializeExpandedState() {
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().startsWith("searcher.category"))
					SearcherList.this.updateExpandedState();
			}
			
		});
		
		this.updateExpandedState();
	}
	
	private void updateExpandedState() {
		Boolean expanded;
		
		expanded = Main.SETTINGS.getBooleanProperty(this.generalCategory.getExpandedPropetyName());
		this.list.setExpanded(
				this.generalCategory,
				(expanded != null && expanded));
		
		expanded = Main.SETTINGS.getBooleanProperty(this.contextCategory.getExpandedPropetyName());
		this.list.setExpanded(
				this.contextCategory,
				(expanded != null && expanded));
		
		expanded = Main.SETTINGS.getBooleanProperty(this.folderCategory.getExpandedPropetyName());
		this.list.setExpanded(
				this.folderCategory,
				(expanded != null && expanded));
		
		expanded = Main.SETTINGS.getBooleanProperty(this.goalCategory.getExpandedPropetyName());
		this.list.setExpanded(this.goalCategory, (expanded != null && expanded));
		
		expanded = Main.SETTINGS.getBooleanProperty(this.locationCategory.getExpandedPropetyName());
		this.list.setExpanded(
				this.locationCategory,
				(expanded != null && expanded));
		
		expanded = Main.SETTINGS.getBooleanProperty(this.personalCategory.getExpandedPropetyName());
		this.list.setExpanded(
				this.personalCategory,
				(expanded != null && expanded));
	}
	
	private void initializeGeneralCategory() {
		this.generalCategory = new SearcherCategory(
				Translations.getString("searcherlist.general"),
				"searcher.category.general.expanded");
		this.model.addCategory(this.generalCategory);
		
		for (TaskSearcher searcher : Constants.GENERAL_TASK_SEARCHERS)
			this.model.addItemToCategory(
					new SearcherItem(searcher),
					this.generalCategory);
	}
	
	private void initializeContextCategory() {
		this.contextCategory = new SearcherCategory(
				Translations.getString("general.contexts"),
				"searcher.category.context.expanded");
		this.model.addCategory(this.contextCategory);
		
		this.model.addItemToCategory(
				new ModelItem(ModelType.CONTEXT, null),
				this.contextCategory);
		
		List<Context> contexts = new ArrayList<Context>(
				ContextFactory.getInstance().getList());
		Collections.sort(contexts, new ModelComparator());
		
		for (Context context : contexts)
			if (context.getModelStatus().equals(ModelStatus.LOADED)
					|| context.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.model.addItemToCategory(new ModelItem(
						ModelType.CONTEXT,
						context), this.contextCategory);
		
		ContextFactory.getInstance().addListChangeListener(this);
		ContextFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeFolderCategory() {
		this.folderCategory = new SearcherCategory(
				Translations.getString("general.folders"),
				"searcher.category.folder.expanded");
		this.model.addCategory(this.folderCategory);
		
		this.model.addItemToCategory(
				new ModelItem(ModelType.FOLDER, null),
				this.folderCategory);
		
		List<Folder> folders = new ArrayList<Folder>(
				FolderFactory.getInstance().getList());
		Collections.sort(folders, new ModelComparator());
		
		for (Folder folder : folders)
			if (folder.getModelStatus().equals(ModelStatus.LOADED)
					|| folder.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.model.addItemToCategory(new ModelItem(
						ModelType.FOLDER,
						folder), this.folderCategory);
		
		FolderFactory.getInstance().addListChangeListener(this);
		FolderFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeGoalCategory() {
		this.goalCategory = new SearcherCategory(
				Translations.getString("general.goals"),
				"searcher.category.goal.expanded");
		this.model.addCategory(this.goalCategory);
		
		this.model.addItemToCategory(
				new ModelItem(ModelType.GOAL, null),
				this.goalCategory);
		
		List<Goal> goals = new ArrayList<Goal>(
				GoalFactory.getInstance().getList());
		Collections.sort(goals, new ModelComparator());
		
		for (Goal goal : goals)
			if (goal.getModelStatus().equals(ModelStatus.LOADED)
					|| goal.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.model.addItemToCategory(
						new ModelItem(ModelType.GOAL, goal),
						this.goalCategory);
		
		GoalFactory.getInstance().addListChangeListener(this);
		GoalFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeLocationCategory() {
		this.locationCategory = new SearcherCategory(
				Translations.getString("general.locations"),
				"searcher.category.location.expanded");
		this.model.addCategory(this.locationCategory);
		
		this.model.addItemToCategory(
				new ModelItem(ModelType.LOCATION, null),
				this.locationCategory);
		
		List<Location> locations = new ArrayList<Location>(
				LocationFactory.getInstance().getList());
		Collections.sort(locations, new ModelComparator());
		
		for (Location location : locations)
			if (location.getModelStatus().equals(ModelStatus.LOADED)
					|| location.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.model.addItemToCategory(new ModelItem(
						ModelType.LOCATION,
						location), this.locationCategory);
		
		LocationFactory.getInstance().addListChangeListener(this);
		LocationFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializePersonalCategory() {
		this.personalCategory = new SearcherCategory(
				Translations.getString("searcherlist.personal"),
				"searcher.category.personal.expanded");
		this.model.addCategory(this.personalCategory);
		
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				TaskSearcherFactory.getInstance().getList());
		Collections.sort(searchers, new TaskSearcherComparator());
		
		for (TaskSearcher searcher : searchers)
			this.model.addItemToCategory(
					new SearcherItem(searcher),
					this.personalCategory);
		
		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);
	}
	
	public SourceList getSourceList() {
		return this.list;
	}
	
	@Override
	public void setTitleFilter(String title) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void selectDefaultTaskSearcher() {
		this.list.setSelectedItem(this.generalCategory.getItems().get(0));
	}
	
	@Override
	public TaskSearcher getSelectedTaskSearcher() {
		SourceListItem item = this.list.getSelectedItem();
		
		if (item == null)
			return null;
		
		return ((TaskSearcherElement) item).getTaskSearcher();
	}
	
	@Override
	public void refreshTaskSearcher() {

	}
	
	@Override
	public void addTaskSearcherSelectionChangeListener(
			TaskSearcherSelectionListener listener) {
		this.taskSearcherSelectionChangeSupport.addTaskSearcherSelectionChangeListener(listener);
	}
	
	@Override
	public void removeTaskSearcherSelectionChangeListener(
			TaskSearcherSelectionListener listener) {
		this.taskSearcherSelectionChangeSupport.removeTaskSearcherSelectionChangeListener(listener);
	}
	
	private SourceListItem findItemFromModel(Model model) {
		List<SourceListItem> items = this.getCategoryFromModel(model).getItems();
		for (SourceListItem item : items) {
			if (EqualsUtils.equals(((ModelItem) item).getModel(), model))
				return item;
		}
		
		return null;
	}
	
	private SourceListItem findItemFromSearcher(TaskSearcher searcher) {
		List<SourceListItem> items = this.personalCategory.getItems();
		for (SourceListItem item : items) {
			if (EqualsUtils.equals(
					((SearcherItem) item).getTaskSearcher(),
					searcher))
				return item;
		}
		
		return null;
	}
	
	private SourceListCategory getCategoryFromModel(Model model) {
		if (model instanceof Context)
			return this.contextCategory;
		else if (model instanceof Folder)
			return this.folderCategory;
		else if (model instanceof Goal)
			return this.goalCategory;
		else if (model instanceof Location)
			return this.locationCategory;
		
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
			SourceListCategory category = this.getCategoryFromModel((Model) event.getValue());
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				ModelItem item = new ModelItem(
						((Model) event.getValue()).getModelType(),
						(Model) event.getValue());
				
				this.model.addItemToCategory(item, category);
				this.list.setSelectedItem(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				SourceListItem item = this.findItemFromModel((Model) event.getValue());
				
				if (item != null) {
					this.model.removeItemFromCategory(item, category);
					this.selectDefaultTaskSearcher();
				}
			}
			
			return;
		}
		
		if (event.getValue() instanceof TaskSearcher) {
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				SearcherItem item = new SearcherItem(
						(TaskSearcher) event.getValue());
				
				this.model.addItemToCategory(item, this.personalCategory);
				this.list.setSelectedItem(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				SourceListItem item = this.findItemFromSearcher((TaskSearcher) event.getValue());
				
				if (item != null) {
					this.model.removeItemFromCategory(
							item,
							this.personalCategory);
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
				SourceListItem item = this.findItemFromSearcher((TaskSearcher) event.getSource());
				
				if (item != null) {
					this.model.removeItemFromCategory(
							item,
							this.personalCategory);
					this.selectDefaultTaskSearcher();
				}
				
				item = new SearcherItem((TaskSearcher) event.getSource());
				
				this.model.addItemToCategory(item, this.personalCategory);
				this.list.setSelectedItem(item);
			}
			
			return;
		}
	}
	
	@Override
	public void saveSettings() {
		Main.SETTINGS.setBooleanProperty(
				this.generalCategory.getExpandedPropetyName(),
				this.list.isExpanded(this.generalCategory));
		
		Main.SETTINGS.setBooleanProperty(
				this.contextCategory.getExpandedPropetyName(),
				this.list.isExpanded(this.contextCategory));
		
		Main.SETTINGS.setBooleanProperty(
				this.folderCategory.getExpandedPropetyName(),
				this.list.isExpanded(this.folderCategory));
		
		Main.SETTINGS.setBooleanProperty(
				this.goalCategory.getExpandedPropetyName(),
				this.list.isExpanded(this.goalCategory));
		
		Main.SETTINGS.setBooleanProperty(
				this.locationCategory.getExpandedPropetyName(),
				this.list.isExpanded(this.locationCategory));
		
		Main.SETTINGS.setBooleanProperty(
				this.personalCategory.getExpandedPropetyName(),
				this.list.isExpanded(this.personalCategory));
	}
	
	public void updateBadges() {
		Boolean showBadges = Main.SETTINGS.getBooleanProperty("searcher.show_badges");
		if (showBadges == null || !showBadges)
			return;
		
		List<SourceListItem> items = null;
		
		items = this.generalCategory.getItems();
		for (SourceListItem i : items)
			((SearcherItem) i).updateBadgeCount();
		
		items = this.contextCategory.getItems();
		for (SourceListItem i : items)
			((ModelItem) i).updateBadgeCount();
		
		items = this.folderCategory.getItems();
		for (SourceListItem i : items)
			((ModelItem) i).updateBadgeCount();
		
		items = this.goalCategory.getItems();
		for (SourceListItem i : items)
			((ModelItem) i).updateBadgeCount();
		
		items = this.locationCategory.getItems();
		for (SourceListItem i : items)
			((ModelItem) i).updateBadgeCount();
		
		items = this.personalCategory.getItems();
		for (SourceListItem i : items)
			((SearcherItem) i).updateBadgeCount();
	}
	
}
