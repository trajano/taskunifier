package com.leclercb.taskunifier.gui.components.searcherlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import com.explodingpixels.macwidgets.SourceListSelectionListener;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.components.searcherlist.items.ModelItem;
import com.leclercb.taskunifier.gui.components.searcherlist.items.SearcherItem;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.models.ModelComparator;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SearcherList implements SearcherView {
	
	private TaskSearcherSelectionChangeSupport taskSearcherSelectionChangeSupport;
	
	private SourceList list;
	private SourceListModel model;
	
	private SourceListCategory generalCategory;
	private SourceListCategory contextCategory;
	private SourceListCategory folderCategory;
	private SourceListCategory goalCategory;
	private SourceListCategory locationCategory;
	private SourceListCategory personalCategory;
	
	public SearcherList() {
		this.taskSearcherSelectionChangeSupport = new TaskSearcherSelectionChangeSupport(
				this);
		
		this.initialize();
	}
	
	private void initialize() {
		this.model = new SourceListModel();
		
		this.initializeGeneralCategory();
		this.initializeContextCategory();
		this.initializeFolderCategory();
		
		this.goalCategory = new SourceListCategory(
				Translations.getString("general.goals"));
		this.model.addCategory(this.goalCategory);
		
		this.locationCategory = new SourceListCategory(
				Translations.getString("general.locations"));
		this.model.addCategory(this.locationCategory);
		
		this.personalCategory = new SourceListCategory(
				Translations.getString("searcherlist.personal"));
		this.model.addCategory(this.personalCategory);
		
		this.list = new SourceList(this.model);
		
		this.list.addSourceListSelectionListener(new SourceListSelectionListener() {
			
			@Override
			public void sourceListItemSelected(SourceListItem e) {
				SearcherList.this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(SearcherList.this.getSelectedTaskSearcher());
			}
			
		});
	}
	
	private void initializeGeneralCategory() {
		this.generalCategory = new SourceListCategory(
				Translations.getString("searcherlist.general"));
		this.model.addCategory(this.generalCategory);
		
		for (TaskSearcher searcher : Constants.GENERAL_TASK_SEARCHERS)
			this.model.addItemToCategory(
					new SearcherItem(searcher),
					this.generalCategory);
	}
	
	private void initializeContextCategory() {
		this.contextCategory = new SourceListCategory(
				Translations.getString("general.contexts"));
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
		
		// ContextFactory.getInstance().addListChangeListener(this);
		// ContextFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializeFolderCategory() {
		this.folderCategory = new SourceListCategory(
				Translations.getString("general.folders"));
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
		
		// FolderFactory.getInstance().addListChangeListener(this);
		// FolderFactory.getInstance().addPropertyChangeListener(this);
	}
	
	public SourceList getSourceList() {
		return this.list;
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
	
}
