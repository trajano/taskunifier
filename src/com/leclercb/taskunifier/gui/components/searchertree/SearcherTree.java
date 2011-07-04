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
package com.leclercb.taskunifier.gui.components.searchertree;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.searchertree.draganddrop.TaskSearcherTransferHandler;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.ModelItem;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.TagItem;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.TaskSearcherProvider;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class SearcherTree extends JTree implements SearcherView, SavePropertiesListener {
	
	private TaskSearcherSelectionChangeSupport taskSearcherSelectionChangeSupport;
	
	public SearcherTree() {
		this.taskSearcherSelectionChangeSupport = new TaskSearcherSelectionChangeSupport(
				this);
		
		this.initialize();
	}
	
	private void initialize() {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.setOpaque(false);
		this.setRootVisible(false);
		this.setLargeModel(true);
		this.setShowsRootHandles(true);
		this.setRowHeight(20);
		
		this.setSelectionModel(new SearcherTreeSelectionModel());
		this.setModel(new SearcherTreeModel(this.getSelectionModel()));
		this.setUI(new SearcherTreeUI());
		
		this.initializeToolTipText();
		this.initializeDragAndDrop();
		this.initializeCopyAndPaste();
		this.initializeExpandedState();
		
		Synchronizing.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!(Boolean) evt.getNewValue())
					SearcherTree.this.updateBadges();
			}
			
		});
		
		this.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent evt) {
				SearcherTree.this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(SearcherTree.this.getSelectedTaskSearcher());
			}
			
		});
	}
	
	public SearcherTreeModel getSearcherModel() {
		return (SearcherTreeModel) this.getModel();
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
	
	@Override
	public void setTitleFilter(String title) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void selectTaskSearcher(TaskSearcher searcher) {
		TreeNode node = this.getSearcherModel().findItemFromSearcher(searcher);
		this.setSelectionPath(TreeUtils.getPath(node));
	}
	
	@Override
	public void selectModel(Model model) {
		TreeNode node = this.getSearcherModel().findItemFromModel(model);
		this.setSelectionPath(TreeUtils.getPath(node));
	}
	
	@Override
	public void selectTag(String tag) {
		TreeNode node = this.getSearcherModel().findItemFromTag(tag);
		this.setSelectionPath(TreeUtils.getPath(node));
	}
	
	@Override
	public void selectDefaultTaskSearcher() {
		TreeNode node = this.getSearcherModel().getDefaultSearcher();
		this.setSelectionPath(TreeUtils.getPath(node));
	}
	
	public Model getSelectedModel() {
		if (this.getSelectionPath() == null)
			return null;
		
		TreeNode node = (TreeNode) this.getSelectionPath().getLastPathComponent();
		
		if (node == null || !(node instanceof ModelItem))
			return null;
		
		return ((ModelItem) node).getModel();
	}
	
	@Override
	public TaskSearcher getSelectedTaskSearcher() {
		if (this.getSelectionPath() == null)
			return null;
		
		TreeNode node = (TreeNode) this.getSelectionPath().getLastPathComponent();
		
		if (node == null || !(node instanceof TaskSearcherProvider))
			return null;
		
		return ((TaskSearcherProvider) node).getTaskSearcher();
	}
	
	public String getSelectedTag() {
		if (this.getSelectionPath() == null)
			return null;
		
		TreeNode node = (TreeNode) this.getSelectionPath().getLastPathComponent();
		
		if (node == null || !(node instanceof TagItem))
			return null;
		
		return ((TagItem) node).getTag();
	}
	
	@Override
	public void refreshTaskSearcher() {

	}
	
	public void updateBadges() {
		this.getSearcherModel().updateBadges();
	}
	
	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		this.setTransferHandler(new TaskSearcherTransferHandler());
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
		Main.SETTINGS.addPropertyChangeListener(
				"searcher.category",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						SearcherTree.this.updateExpandedState();
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
						Main.SETTINGS.getBooleanProperty(category.getExpandedPropetyName()));
			}
		}
	}
	
	@Override
	public void saveProperties() {
		SearcherCategory[] categories = this.getSearcherModel().getCategories();
		for (SearcherCategory category : categories) {
			if (category.getExpandedPropetyName() != null) {
				Main.SETTINGS.setBooleanProperty(
						category.getExpandedPropetyName(),
						this.isExpanded(TreeUtils.getPath(category)));
			}
		}
	}
	
}
