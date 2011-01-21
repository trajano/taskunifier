package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.leclercb.commons.api.properties.SavePropertiesListener;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.components.searcherlist.draganddrop.TaskSearcherTransferHandler;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.CategoryTreeNode;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.TaskSearcherTreeNode;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherTree extends JTree implements SavePropertiesListener, ActionListener {
	
	public SearcherTree() {
		super(new SearcherTreeModel());
		this.initialize();
	}
	
	private void initialize() {
		Main.SETTINGS.addSaveSettingsListener(this);
		
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
				Boolean expanded = Main.SETTINGS.getBooleanProperty(((CategoryTreeNode) node).getExpandedPropetyName());
				
				if (expanded != null && expanded)
					this.expandRow(i);
				else
					this.collapseRow(i);
			}
		}
		
		if (Main.SETTINGS.getBooleanProperty("theme.color.enabled")) {
			this.setBackground(Main.SETTINGS.getColorProperty("theme.color.searcher_list"));
		} else {
			this.setBackground(UIManager.getColor("Tree.background"));
		}
		
		this.initializeDragAndDrop();
		this.initializeCopyAndPaste();
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
				
				Main.SETTINGS.setBooleanProperty(
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
		
		if (e.getActionCommand().equals(SearcherTreeModel.ACT_NODE_REMOVED)) {
			this.selectDefaultTaskSearcher();
		}
	}
	
	private void initializeDragAndDrop() {
		this.setDragEnabled(false);
		this.setTransferHandler(new TaskSearcherTransferHandler());
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
	
}
