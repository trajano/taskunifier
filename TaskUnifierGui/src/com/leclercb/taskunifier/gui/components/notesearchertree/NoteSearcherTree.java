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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeNode;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.notesearchertree.draganddrop.NoteSearcherTransferHandler;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.FolderItem;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.NoteSearcherProvider;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;

public class NoteSearcherTree extends JTree implements NoteSearcherView, SavePropertiesListener {
	
	private String settingsPrefix;
	
	public NoteSearcherTree(String settingsPrefix) {
		this.settingsPrefix = settingsPrefix;
		this.initialize();
	}
	
	private void initialize() {
		Main.getSettings().addSavePropertiesListener(this);
		
		this.setOpaque(false);
		this.setRootVisible(false);
		this.setLargeModel(true);
		this.setShowsRootHandles(true);
		this.setRowHeight(20);
		
		this.setSelectionModel(new NoteSearcherTreeSelectionModel());
		this.setModel(new NoteSearcherTreeModel(
				this.settingsPrefix,
				this.getSelectionModel()));
		this.setUI(new NoteSearcherTreeUI());
		
		this.initializeToolTipText();
		this.initializeDragAndDrop();
		this.initializeCopyAndPaste();
		this.initializeExpandedState();
		
		Synchronizing.getInstance().addPropertyChangeListener(
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (!(Boolean) evt.getNewValue())
							NoteSearcherTree.this.updateBadges();
					}
					
				});
	}
	
	public NoteSearcherTreeModel getSearcherModel() {
		return (NoteSearcherTreeModel) this.getModel();
	}
	
	@Override
	public void addNoteSearcherSelectionChangeListener(
			NoteSearcherSelectionListener listener) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void removeNoteSearcherSelectionChangeListener(
			NoteSearcherSelectionListener listener) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addExtraNotes(Note[] notes) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setExtraNotes(Note[] notes) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setSearchFilter(String filter) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean selectNoteSearcher(NoteSearcher searcher) {
		TreeNode node = this.getSearcherModel().findItemFromSearcher(searcher);
		
		if (node == null)
			return false;
		
		this.setSelectionPath(TreeUtils.getPath(node));
		return true;
	}
	
	@Override
	public boolean selectFolder(Folder folder) {
		TreeNode node = this.getSearcherModel().findItemFromFolder(folder);
		
		if (node == null)
			return false;
		
		this.setSelectionPath(TreeUtils.getPath(node));
		return true;
	}
	
	@Override
	public void selectDefaultNoteSearcher() {
		TreeNode node = this.getSearcherModel().getDefaultSearcher();
		this.setSelectionPath(TreeUtils.getPath(node));
	}
	
	public Folder getSelectedFolder() {
		if (this.getSelectionPath() == null)
			return null;
		
		TreeNode node = (TreeNode) this.getSelectionPath().getLastPathComponent();
		
		if (node == null || !(node instanceof FolderItem))
			return null;
		
		return ((FolderItem) node).getFolder();
	}
	
	@Override
	public NoteSearcher getSelectedNoteSearcher() {
		if (this.getSelectionPath() == null)
			return null;
		
		TreeNode node = (TreeNode) this.getSelectionPath().getLastPathComponent();
		
		if (node == null || !(node instanceof NoteSearcherProvider))
			return null;
		
		return ((NoteSearcherProvider) node).getNoteSearcher();
	}
	
	@Override
	public NoteSearcher getSelectedOriginalNoteSearcher() {
		return this.getSelectedNoteSearcher();
	}
	
	@Override
	public void refreshNoteSearcher() {
		this.updateBadges();
	}
	
	public void updateBadges() {
		this.getSearcherModel().updateBadges();
	}
	
	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		this.setTransferHandler(new NoteSearcherTransferHandler());
		this.setDropMode(DropMode.ON_OR_INSERT);
	}
	
	private void initializeToolTipText() {
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	private void initializeCopyAndPaste() {
		ActionMap amap = this.getActionMap();
		amap.put(
				TransferHandler.getCutAction().getValue(Action.NAME),
				TransferHandler.getCutAction());
		amap.put(
				TransferHandler.getCopyAction().getValue(Action.NAME),
				TransferHandler.getCopyAction());
		amap.put(
				TransferHandler.getPasteAction().getValue(Action.NAME),
				TransferHandler.getPasteAction());
		
		InputMap imap = this.getInputMap();
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
		Main.getSettings().addPropertyChangeListener(
				this.settingsPrefix + ".category",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						NoteSearcherTree.this.updateExpandedState();
					}
					
				});
		
		this.updateExpandedState();
	}
	
	private void updateExpandedState() {
		SearcherCategory[] categories = this.getSearcherModel().getCategories();
		for (SearcherCategory category : categories) {
			if (category.getExpandedPropetyName() != null) {
				this.setExpandedState(
						TreeUtils.getPath(category),
						Main.getSettings().getBooleanProperty(
								category.getExpandedPropetyName(),
								false));
			}
		}
	}
	
	@Override
	public void saveProperties() {
		SearcherCategory[] categories = this.getSearcherModel().getCategories();
		for (SearcherCategory category : categories) {
			if (category.getExpandedPropetyName() != null) {
				Main.getSettings().setBooleanProperty(
						category.getExpandedPropetyName(),
						this.isExpanded(TreeUtils.getPath(category)));
			}
		}
	}
	
}
