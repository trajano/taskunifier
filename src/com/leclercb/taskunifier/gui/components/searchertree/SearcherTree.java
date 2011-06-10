package com.leclercb.taskunifier.gui.components.searchertree;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXTree;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherNode;
import com.leclercb.taskunifier.gui.components.searchertree.transfer.TaskSearcherTransferHandler;
import com.leclercb.taskunifier.gui.main.Main;

public class SearcherTree extends JXTree implements SearcherView, SavePropertiesListener {
	
	private TaskSearcherSelectionChangeSupport taskSearcherSelectionChangeSupport;
	
	public SearcherTree() {
		this.taskSearcherSelectionChangeSupport = new TaskSearcherSelectionChangeSupport(
				this);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setModel(new SearcherTreeModel());
		
		this.setUI(new SearcherTreeUI());
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
	
	public Model getSelectedModel() {

	}
	
	public void selectTaskSearcher(TaskSearcher searcher) {

	}
	
	public void selectModel(Model model) {

	}
	
	@Override
	public void selectDefaultTaskSearcher() {

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
	
	private void initializeCopyAndPaste() {
		this.setTransferHandler(new TaskSearcherTransferHandler(this));
		
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
	
}
