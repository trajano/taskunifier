package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.leclercb.taskunifier.gui.components.searcherlist.nodes.SearcherTreeNode;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherTreeRenderer extends DefaultTreeCellRenderer {

	public SearcherTreeRenderer() {
		this.setLeafIcon(Images.getResourceImage("tree_leaf.png"));
		this.setOpenIcon(Images.getResourceImage("tree_open.png"));
		this.setClosedIcon(Images.getResourceImage("tree_closed.png"));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		Icon leafIcon = this.getLeafIcon();

		if (value instanceof SearcherTreeNode) {
			TaskSearcher searcher = (TaskSearcher) ((SearcherTreeNode) value).getUserObject();

			if (searcher.getIcon() != null)
				this.setLeafIcon(Images.getImage(searcher.getIcon(), 16, 16));
		}

		Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		this.setLeafIcon(leafIcon);

		return component;
	}
}
