package com.leclercb.taskunifier.gui.components.searchertree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.SourceListBadgeContentProvider;
import com.explodingpixels.macwidgets.SourceListCountBadgeRenderer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;

public class SearcherTreeCellRenderer implements TreeCellRenderer {
	
	private CategoryTreeCellRenderer categoryRenderer = new CategoryTreeCellRenderer();
	private ItemTreeCellRenderer itemRenderer = new ItemTreeCellRenderer();
	
	@Override
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean selected,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		
		TreeCellRenderer renderer = this.itemRenderer;
		
		if (value instanceof SearcherCategory)
			renderer = this.categoryRenderer;
		
		return renderer.getTreeCellRendererComponent(
				tree,
				value,
				selected,
				expanded,
				leaf,
				row,
				hasFocus);
	}
	
	private class CategoryTreeCellRenderer implements TreeCellRenderer {
		
		private JLabel fLabel = MacWidgetFactory.makeEmphasizedLabel(
				new JLabel(),
				SearcherTreeUI.this.fColorScheme.getCategoryTextColor(),
				SearcherTreeUI.this.fColorScheme.getCategoryTextColor(),
				SearcherTreeUI.this.fColorScheme.getCategoryTextShadowColor());
		
		private CategoryTreeCellRenderer() {

		}
		
		@Override
		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean selected,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {
			this.fLabel.setFont(SearcherTreeUI.this.getCategoryFont());
			TreeNode node = (TreeNode) value;
			this.fLabel.setText(SearcherTreeUI.this.getTextForNode(
					node,
					selected,
					expanded,
					leaf,
					row,
					hasFocus).toUpperCase());
			return this.fLabel;
		}
	}
	
	private class ItemTreeCellRenderer implements TreeCellRenderer {
		
		private PanelBuilder fBuilder;
		
		private SourceListCountBadgeRenderer fCountRenderer = new SourceListCountBadgeRenderer(
				SearcherTreeUI.this.fColorScheme.getSelectedBadgeColor(),
				SearcherTreeUI.this.fColorScheme.getActiveUnselectedBadgeColor(),
				SearcherTreeUI.this.fColorScheme.getInativeUnselectedBadgeColor(),
				SearcherTreeUI.this.fColorScheme.getBadgeTextColor());
		
		private JLabel fSelectedLabel = MacWidgetFactory.makeEmphasizedLabel(
				new JLabel(),
				SearcherTreeUI.this.fColorScheme.getSelectedItemTextColor(),
				SearcherTreeUI.this.fColorScheme.getSelectedItemTextColor(),
				SearcherTreeUI.this.fColorScheme.getSelectedItemFontShadowColor());
		
		private JLabel fUnselectedLabel = MacWidgetFactory.makeEmphasizedLabel(
				new JLabel(),
				SearcherTreeUI.this.fColorScheme.getUnselectedItemTextColor(),
				SearcherTreeUI.this.fColorScheme.getUnselectedItemTextColor(),
				TRANSPARENT_COLOR);
		
		private ItemTreeCellRenderer() {
			FormLayout layout = new FormLayout(
					"fill:0px:grow, 5px, p, 5px",
					"3px, fill:p:grow, 3px");
			this.fBuilder = new PanelBuilder(layout);
			this.fBuilder.getPanel().setOpaque(false);
		}
		
		@Override
		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean selected,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {
			this.fSelectedLabel.setFont(SearcherTreeUI.this.getItemSelectedFont());
			this.fUnselectedLabel.setFont(SearcherTreeUI.this.getItemFont());
			
			TreeNode node = (TreeNode) value;
			JLabel label = selected ? this.fSelectedLabel : this.fUnselectedLabel;
			label.setText(SearcherTreeUI.this.getTextForNode(
					node,
					selected,
					expanded,
					leaf,
					row,
					hasFocus));
			label.setIcon(SearcherTreeUI.this.getIconForNode(node));
			
			this.fBuilder.getPanel().removeAll();
			CellConstraints cc = new CellConstraints();
			this.fBuilder.add(label, cc.xywh(1, 1, 1, 3));
			
			if (value instanceof SourceListBadgeContentProvider) {
				SourceListBadgeContentProvider badgeContentProvider = (SourceListBadgeContentProvider) value;
				if (badgeContentProvider.getCounterValue() > 0) {
					this.fBuilder.add(
							this.fCountRenderer.getComponent(),
							cc.xy(3, 2, "center, fill"));
					this.fCountRenderer.setState(
							badgeContentProvider.getCounterValue(),
							selected);
				}
			}
			
			return this.fBuilder.getPanel();
		}
	}
	
}
