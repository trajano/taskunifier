package com.leclercb.taskunifier.gui.components.searchertree;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXTree;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.ModelItem;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherNode;
import com.leclercb.taskunifier.gui.components.searchertree.transfer.TaskSearcherTransferHandler;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;

public class SearcherTree extends JXTree implements SearcherView, SavePropertiesListener {
	
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
		
		// this.initializeToolTipText();
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
	
	private SearcherTreeModel getSearcherModel() {
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
	
	public void selectTaskSearcher(TaskSearcher searcher) {
		TreeNode node = this.getSearcherModel().findItemFromSearcher(searcher);
		this.setSelectionPath(TreeUtils.getPath(node));
	}
	
	public void selectModel(Model model) {
		TreeNode node = this.getSearcherModel().findItemFromModel(model);
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
		
		if (node == null || !(node instanceof SearcherNode))
			return null;
		
		return ((SearcherNode) node).getTaskSearcher();
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
		this.setDropMode(DropMode.INSERT);
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
		Boolean expanded;
		
		SearcherCategory[] categories = this.getSearcherModel().getCategories();
		for (SearcherCategory category : categories) {
			expanded = Main.SETTINGS.getBooleanProperty(category.getExpandedPropetyName());
			this.setExpandedState(
					TreeUtils.getPath(category),
					(expanded != null && expanded));
		}
	}
	
	@Override
	public void saveProperties() {
		SearcherCategory[] categories = this.getSearcherModel().getCategories();
		for (SearcherCategory category : categories) {
			Main.SETTINGS.setBooleanProperty(
					category.getExpandedPropetyName(),
					this.isExpanded(TreeUtils.getPath(category)));
		}
	}
	
}
