package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.leclercb.taskunifier.gui.components.searcherlist.nodes.ModelTreeNode;
import com.leclercb.taskunifier.gui.components.searcherlist.nodes.SearcherTreeNode;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherTreeRenderer extends DefaultTreeCellRenderer {
	
	private Icon originalLeafIcon;
	
	public SearcherTreeRenderer() {
		this.setLeafIcon(Images.getResourceImage("tree_leaf.png"));
		this.setOpenIcon(Images.getResourceImage("tree_open.png"));
		this.setClosedIcon(Images.getResourceImage("tree_closed.png"));
		
		this.originalLeafIcon = this.getLeafIcon();
	}
	
	@Override
	public Color getBackgroundNonSelectionColor() {
		return null;
	}
	
	@Override
	public Color getBackground() {
		return null;
	}
	
	@Override
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		if (value instanceof SearcherTreeNode) {
			TaskSearcher searcher = ((SearcherTreeNode) value).getTaskSearcher();
			
			if (searcher.getIcon() != null)
				this.setLeafIcon(Images.getImage(searcher.getIcon(), 16, 16));
			else
				this.setLeafIcon(this.originalLeafIcon);
			
			this.setToolTipText("<html>"
					+ searcher.getTitle()
					+ "<br />"
					+ searcher.getSorter()
					+ "<br />"
					+ searcher.getFilter()
					+ "</html>");
		} else if (value instanceof ModelTreeNode) {
			TaskSearcher searcher = ((ModelTreeNode) value).getTaskSearcher();
			
			this.setLeafIcon(this.originalLeafIcon);
			
			this.setToolTipText("<html>"
					+ searcher.getTitle()
					+ "<br />"
					+ searcher.getSorter()
					+ "<br />"
					+ searcher.getFilter()
					+ "</html>");
		} else {
			this.setLeafIcon(this.originalLeafIcon);
			this.setToolTipText(null);
		}
		
		Component component = super.getTreeCellRendererComponent(
				tree,
				value,
				sel,
				expanded,
				leaf,
				row,
				hasFocus);
		
		return component;
	}
}
