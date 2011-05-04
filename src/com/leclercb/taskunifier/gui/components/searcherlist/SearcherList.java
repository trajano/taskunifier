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
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
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
import com.leclercb.taskunifier.gui.swing.macwidgets.SourceListColorScheme;
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
				true, this);

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

		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);

		this.list = new SourceList(this.model);
		this.list.setColorScheme(new SourceListColorScheme());

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
		
		this.model.addItemToCategory(
						new SearcherItem(Constants.DEFAULT_SEARCHER),
						this.generalCategory);
		
		List<TaskSearcher> searchers = new ArrayList<TaskSearcher>(
				TaskSearcherFactory.getInstance().getList());
		//Collections.sort(searchers, new TaskSearcherComparator());

		for (TaskSearcher searcher : searchers)
			if (searcher.getType() == TaskSearcherType.GENERAL)
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
			if (searcher.getType() == TaskSearcherType.PERSONAL)
				this.model.addItemToCategory(
						new SearcherItem(searcher),
						this.personalCategory);
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
		SearcherCategory[] categories = new SearcherCategory[] {
				this.generalCategory,
				this.personalCategory
		};

		for (SearcherCategory category : categories) {
			List<SourceListItem> items = category.getItems();
			for (SourceListItem item : items) {
				if (EqualsUtils.equals(
						((SearcherItem) item).getTaskSearcher(),
						searcher))
					return item;
			}
		}

		return null;
	}

	private SourceListCategory getCategoryFromTaskSearcherType(TaskSearcherType type) {
		switch (type) {
		case GENERAL: return this.generalCategory;
		case PERSONAL: return this.personalCategory;
		default: return null;
		}
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
				TaskSearcher searcher = (TaskSearcher) event.getValue();
				SearcherItem item = new SearcherItem(
						searcher);

				SourceListCategory category = getCategoryFromTaskSearcherType(searcher.getType());
				
				this.model.addItemToCategory(item, category);

				this.list.setSelectedItem(item);
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				TaskSearcher searcher = (TaskSearcher) event.getValue();
				SourceListItem item = this.findItemFromSearcher(searcher);

				if (item != null) {
					SourceListCategory category = getCategoryFromTaskSearcherType(searcher.getType());
					
					this.model.removeItemFromCategory(
							item,
							category);
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

				SourceListCategory category = getCategoryFromTaskSearcherType(searcher.getType());

				if (item != null) {
					this.model.removeItemFromCategory(
							item,
							category);
					this.selectDefaultTaskSearcher();
				}

				item = new SearcherItem(searcher);

				this.model.addItemToCategory(item, category);
				this.list.setSelectedItem(item);
			}
			
			if (event.getPropertyName().equals(TaskSearcher.PROP_TYPE)) {
				TaskSearcher searcher = (TaskSearcher) event.getSource();
				SourceListItem item = this.findItemFromSearcher(searcher);

				SourceListCategory category = getCategoryFromTaskSearcherType((TaskSearcherType) event.getOldValue());

				if (item != null && category != null) {
					this.model.removeItemFromCategory(
							item,
							category);
					this.selectDefaultTaskSearcher();
				}
				
				category = getCategoryFromTaskSearcherType(searcher.getType());

				item = new SearcherItem(searcher);

				this.model.addItemToCategory(item, category);
				this.list.setSelectedItem(item);
			}

			return;
		}
	}

	@Override
	public void saveProperties() {
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
