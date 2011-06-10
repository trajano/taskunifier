package com.leclercb.taskunifier.gui.components.searchertree;

import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXTree;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherNode;

public class SearcherTree extends JXTree implements SearcherView {
	
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
	
}
