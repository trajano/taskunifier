package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.leclercb.taskunifier.api.settings.SaveSettingsListener;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.CategoryTreeNode;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.TaskSearcherTreeNode;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.utils.TreeUtils;

public class SearcherTree extends JTree implements SaveSettingsListener, ActionListener {
	
	public SearcherTree() {
		super(new SearcherTreeModel());
		this.initialize();
	}
	
	private void initialize() {
		Settings.addSaveSettingsListener(this);
		
		((SearcherTreeModel) this.getModel()).addActionListener(this);
		
		// Enable tooltips
		ToolTipManager.sharedInstance().registerComponent(this);
		
		// Warning: might not work with all UIs
		this.setLargeModel(true);
		
		this.setRootVisible(false);
		this.setRowHeight(25);
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
		
		if (Settings.getBooleanProperty("theme.color.enabled")) {
			this.setBackground(Settings.getColorProperty("theme.color.searcher_list"));
		} else {
			this.setBackground(UIManager.getColor("Tree.background"));
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(SearcherTreeModel.ACT_NODE_ADDED)) {
			if (e.getSource() == null)
				return;
			
			TreePath path = TreeUtils.getPath((TreeNode) e.getSource());
			if (path != null) {
				this.setSelectionPath(path);
			}
		}
	}
	
}
