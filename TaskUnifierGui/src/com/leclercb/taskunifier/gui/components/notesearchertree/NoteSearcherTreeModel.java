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
package com.leclercb.taskunifier.gui.components.notesearchertree;

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
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherType;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.comparators.NoteSearcherComparator;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.FolderItem;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherItem;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherNode;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.constants.Constants;

public class NoteSearcherTreeModel extends DefaultTreeModel implements ListChangeListener, PropertyChangeListener {
	
	private TreeSelectionModel treeSelectionModel;
	
	private String settingsPrefix;
	
	private SearcherItem defaultSearcher;
	private SearcherCategory folderCategory;
	private SearcherCategory personalCategory;
	
	public NoteSearcherTreeModel(
			String settingsPrefix,
			TreeSelectionModel treeSelectionModel) {
		super(new SearcherCategory(NoteSearcherType.DEFAULT, null));
		
		this.settingsPrefix = settingsPrefix;
		
		this.treeSelectionModel = treeSelectionModel;
		
		this.initializeDefaultSearcher();
		this.initializeFolderCategory();
		this.initializePersonalCategory();
		
		NoteFactory.getInstance().addListChangeListener(this);
		NoteFactory.getInstance().addPropertyChangeListener(this);
		
		NoteSearcherFactory.getInstance().addListChangeListener(this);
		NoteSearcherFactory.getInstance().addPropertyChangeListener(this);
	}
	
	public SearcherItem getDefaultSearcher() {
		return this.defaultSearcher;
	}
	
	public SearcherCategory[] getCategories() {
		return new SearcherCategory[] {
				this.folderCategory,
				this.personalCategory };
	}
	
	private void initializeDefaultSearcher() {
		this.defaultSearcher = new SearcherItem(
				Constants.getDefaultNoteSearcher());
		((DefaultMutableTreeNode) this.getRoot()).add(this.defaultSearcher);
	}
	
	private void initializeFolderCategory() {
		this.folderCategory = new SearcherCategory(
				NoteSearcherType.FOLDER,
				this.settingsPrefix + ".category.folder.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.folderCategory);
		
		this.folderCategory.add(new FolderItem(null));
		
		List<Folder> folders = new ArrayList<Folder>(
				FolderFactory.getInstance().getList());
		Collections.sort(folders, ModelComparator.INSTANCE);
		
		for (Folder folder : folders)
			if (folder.getModelStatus().isEndUserStatus())
				if (!folder.isArchived())
					this.folderCategory.add(new FolderItem(folder));
		
		FolderFactory.getInstance().addListChangeListener(this);
		FolderFactory.getInstance().addPropertyChangeListener(this);
	}
	
	private void initializePersonalCategory() {
		this.personalCategory = new SearcherCategory(
				NoteSearcherType.PERSONAL,
				this.settingsPrefix + ".category.personal.expanded");
		((DefaultMutableTreeNode) this.getRoot()).add(this.personalCategory);
		
		List<NoteSearcher> searchers = new ArrayList<NoteSearcher>(
				NoteSearcherFactory.getInstance().getList());
		Collections.sort(searchers, NoteSearcherComparator.INSTANCE);
		
		for (NoteSearcher searcher : searchers)
			if (searcher.getType() == NoteSearcherType.PERSONAL)
				this.personalCategory.add(new SearcherItem(searcher));
	}
	
	public int findNewIndexInFolderCategory(Folder folder) {
		List<Folder> folders = new ArrayList<Folder>();
		for (int i = 0; i < this.folderCategory.getChildCount(); i++) {
			TreeNode node = this.folderCategory.getChildAt(i);
			if (node instanceof FolderItem) {
				folders.add(((FolderItem) node).getFolder());
			}
		}
		
		folders.add(folder);
		Collections.sort(folders, ModelComparator.INSTANCE);
		
		return folders.indexOf(folder);
	}
	
	public FolderItem findItemFromFolder(Folder folder) {
		for (int i = 0; i < this.folderCategory.getChildCount(); i++) {
			TreeNode node = this.folderCategory.getChildAt(i);
			if (node instanceof FolderItem) {
				if (EqualsUtils.equals(((FolderItem) node).getFolder(), folder)) {
					return (FolderItem) node;
				}
			}
		}
		
		return null;
	}
	
	public SearcherItem findItemFromSearcher(NoteSearcher searcher) {
		return this.findItemFromSearcher(searcher, searcher.getType());
	}
	
	private SearcherItem findItemFromSearcher(
			NoteSearcher searcher,
			NoteSearcherType type) {
		SearcherCategory category = this.getCategoryFromNoteSearcherType(type);
		
		for (int i = 0; i < category.getChildCount(); i++) {
			TreeNode node = category.getChildAt(i);
			if (node instanceof SearcherItem) {
				if (EqualsUtils.equals(
						((SearcherItem) node).getNoteSearcher(),
						searcher)) {
					return (SearcherItem) node;
				}
			}
		}
		
		return null;
	}
	
	private SearcherCategory getCategoryFromNoteSearcherType(
			NoteSearcherType type) {
		switch (type) {
			case DEFAULT:
				return (SearcherCategory) this.getRoot();
			case FOLDER:
				return this.folderCategory;
			case PERSONAL:
				return this.personalCategory;
		}
		
		return null;
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getValue() instanceof Note) {
			if (!Synchronizing.getInstance().isSynchronizing())
				this.updateBadges();
			return;
		}
		
		if (event.getValue() instanceof Folder) {
			Folder folder = (Folder) event.getValue();
			
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				if (!folder.getModelStatus().isEndUserStatus())
					return;
				
				if (folder.isArchived())
					return;
				
				FolderItem item = new FolderItem(folder);
				
				try {
					this.insertNodeInto(
							item,
							this.folderCategory,
							this.findNewIndexInFolderCategory(folder));
				} catch (Exception e) {
					this.insertNodeInto(item, this.folderCategory, 0);
				}
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				FolderItem item = this.findItemFromFolder(folder);
				
				if (item != null)
					this.removeNodeFromParent(item);
			}
			
			this.updateSelection();
			return;
		}
		
		if (event.getValue() instanceof NoteSearcher) {
			NoteSearcher searcher = (NoteSearcher) event.getValue();
			SearcherCategory category = this.getCategoryFromNoteSearcherType(searcher.getType());
			
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
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof Note) {
			if (!Synchronizing.getInstance().isSynchronizing())
				this.updateBadges();
			return;
		}
		
		if (event.getSource() instanceof Folder) {
			Folder folder = (Folder) event.getSource();
			FolderItem item = this.findItemFromFolder(folder);
			
			if (!((Model) event.getSource()).getModelStatus().isEndUserStatus()) {
				if (item != null)
					this.removeNodeFromParent(item);
			} else if (folder.isArchived()) {
				if (item != null)
					this.removeNodeFromParent(item);
			} else if (item == null) {
				item = new FolderItem(folder);
				
				try {
					this.insertNodeInto(
							item,
							this.folderCategory,
							this.findNewIndexInFolderCategory(folder));
				} catch (Exception e) {
					this.insertNodeInto(item, this.folderCategory, 0);
				}
			} else if (event.getPropertyName().equals(BasicModel.PROP_TITLE)) {
				this.removeNodeFromParent(item);
				
				try {
					this.insertNodeInto(
							item,
							this.folderCategory,
							this.findNewIndexInFolderCategory(folder));
				} catch (Exception e) {
					this.insertNodeInto(item, this.folderCategory, 0);
				}
			} else if (event.getPropertyName().equals(GuiModel.PROP_COLOR)) {
				this.nodeChanged(item);
			}
			
			this.updateSelection();
			return;
		}
		
		if (event.getSource() instanceof NoteSearcher) {
			NoteSearcher searcher = (NoteSearcher) event.getSource();
			
			if (event.getPropertyName().equals(NoteSearcher.PROP_TITLE)
					|| event.getPropertyName().equals(NoteSearcher.PROP_ICON)) {
				SearcherItem item = this.findItemFromSearcher(searcher);
				this.nodeChanged(item);
			}
			
			if (event.getPropertyName().equals(NoteSearcher.PROP_TYPE)) {
				SearcherItem item = this.findItemFromSearcher(
						searcher,
						(NoteSearcherType) event.getOldValue());
				
				if (item != null)
					this.removeNodeFromParent(item);
				
				SearcherCategory category = this.getCategoryFromNoteSearcherType(searcher.getType());
				
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
