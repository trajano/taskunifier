package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.api.settings.SaveSettingsListener;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.CategoryTreeNode;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.TaskSearcherTreeNode;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherTree extends JTree implements SaveSettingsListener {
	
	public SearcherTree() {
		this.initialize();
	}
	
	private void initialize() {
		Settings.addSaveSettingsListener(this);
		
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.setRootVisible(false);
		this.setRowHeight(25);
		this.setModel(new SearcherTreeModel());
		this.setCellRenderer(new SearcherTreeRenderer());
		
		for (int i = 0; i < this.getRowCount(); i++) {
			TreeNode node = (TreeNode) this.getPathForRow(i).getLastPathComponent();
			if (node instanceof CategoryTreeNode) {
				Boolean expanded = Settings.getBooleanProperty(((CategoryTreeNode) node).getExpandedPropetyName());
				
				if (expanded != null && expanded)
					this.expandRow(i);
				else
					this.collapseRow(i);
			}
		}
	}
	
	public void selectDefaultTaskSearcher() {
		this.setSelectionPath(((SearcherTreeModel) this.getModel()).getDefaultTaskSearcherPath());
	}
	
	public TaskSearcher getSelectedTaskSearcher() {
		TaskSearcherTreeNode node = (TaskSearcherTreeNode) this.getLastSelectedPathComponent();
		
		if (node == null)
			return null;
		
		return node.getTaskSearcher();
	}
	
	@Override
	public void saveSettings() {
		for (int i = 0; i < this.getRowCount(); i++) {
			TreeNode node = (TreeNode) this.getPathForRow(i).getLastPathComponent();
			if (node instanceof CategoryTreeNode) {
				Boolean expanded = this.isExpanded(i);
				
				Settings.setBooleanProperty(
						((CategoryTreeNode) node).getExpandedPropetyName(),
						expanded);
			}
		}
	}
	
}
