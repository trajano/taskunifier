package com.leclercb.taskunifier.gui.components.searchertree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.SourceListBadgeContentProvider;
import com.explodingpixels.macwidgets.SourceListColorScheme;
import com.explodingpixels.macwidgets.SourceListCountBadgeRenderer;
import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.explodingpixels.painter.FocusStatePainter;
import com.explodingpixels.painter.RectanglePainter;
import com.explodingpixels.widgets.TreeUtils;
import com.explodingpixels.widgets.WindowUtils;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherNode;

public class SearcherTreeUI extends BasicTreeUI {
	
	private Font categoryFont = UIManager.getFont("Label.font").deriveFont(
			Font.BOLD,
			11.0f);
	private Font itemFont = UIManager.getFont("Label.font").deriveFont(11.0f);
	private Font itemSelectedFont = this.itemFont.deriveFont(Font.BOLD);
	
	private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
	
	private final String SELECT_NEXT = "selectNext";
	private final String SELECT_PREVIOUS = "selectPrevious";
	
	private SourceListColorScheme fColorScheme;
	private FocusStatePainter fBackgroundPainter;
	private FocusStatePainter fSelectionBackgroundPainter;
	
	private CustomTreeModelListener fTreeModelListener = new CustomTreeModelListener();
	
	@Override
	protected void completeUIInstall() {
		super.completeUIInstall();
		
		this.tree.setSelectionModel(new SourceListTreeSelectionModel());
		
		this.tree.setOpaque(false);
		this.tree.setRootVisible(false);
		this.tree.setLargeModel(true);
		this.tree.setRootVisible(false);
		this.tree.setShowsRootHandles(true);
		this.tree.setRowHeight(20);
		
		this.setColorScheme(new SourceListStandardColorScheme());
	}
	
	public Font getCategoryFont() {
		return this.categoryFont;
	}
	
	public void setCategoryFont(Font categoryFont) {
		this.categoryFont = categoryFont;
	}
	
	public Font getItemFont() {
		return this.itemFont;
	}
	
	public void setItemFont(Font itemFont) {
		this.itemFont = itemFont;
	}
	
	public Font getItemSelectedFont() {
		return this.itemSelectedFont;
	}
	
	public void setItemSelectedFont(Font itemSelectedFont) {
		this.itemSelectedFont = itemSelectedFont;
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		WindowUtils.installJComponentRepainterOnWindowFocusChanged(this.tree);
	}
	
	@Override
	protected void installKeyboardActions() {
		super.installKeyboardActions();
		this.tree.getInputMap().put(
				KeyStroke.getKeyStroke("pressed DOWN"),
				this.SELECT_NEXT);
		this.tree.getInputMap().put(
				KeyStroke.getKeyStroke("pressed UP"),
				this.SELECT_PREVIOUS);
		this.tree.getActionMap().put(this.SELECT_NEXT, this.createNextAction());
		this.tree.getActionMap().put(
				this.SELECT_PREVIOUS,
				this.createPreviousAction());
	}
	
	@Override
	protected void setModel(TreeModel model) {
		if (this.treeModel != null) {
			this.treeModel.removeTreeModelListener(this.fTreeModelListener);
		}
		
		super.setModel(model);
		
		if (model != null) {
			model.addTreeModelListener(new CustomTreeModelListener());
		}
	}
	
	public SourceListColorScheme getColorScheme() {
		return this.fColorScheme;
	}
	
	public void setColorScheme(SourceListColorScheme colorScheme) {
		checkColorSchemeNotNull(colorScheme);
		this.fColorScheme = colorScheme;
		this.fBackgroundPainter = new FocusStatePainter(
				new RectanglePainter(
						this.fColorScheme.getActiveBackgroundColor()),
				new RectanglePainter(
						this.fColorScheme.getActiveBackgroundColor()),
				new RectanglePainter(
						this.fColorScheme.getInactiveBackgroundColor()));
		this.fSelectionBackgroundPainter = new FocusStatePainter(
				this.fColorScheme.getActiveFocusedSelectedItemPainter(),
				this.fColorScheme.getActiveUnfocusedSelectedItemPainter(),
				this.fColorScheme.getInactiveSelectedItemPainter());
		
		this.tree.setCellRenderer(new SourceListTreeCellRenderer());
		this.installDisclosureIcons();
	}
	
	private void installDisclosureIcons() {
		this.setCollapsedIcon(this.fColorScheme.getUnselectedCollapsedIcon());
		this.setExpandedIcon(this.fColorScheme.getUnselectedExpandedIcon());
		int indent = this.fColorScheme.getUnselectedCollapsedIcon().getIconWidth() / 2 + 4;
		this.setLeftChildIndent(indent);
		this.setRightChildIndent(indent);
	}
	
	@Override
	protected void paintExpandControl(
			Graphics g,
			Rectangle clipBounds,
			Insets insets,
			Rectangle bounds,
			TreePath path,
			int row,
			boolean isExpanded,
			boolean hasBeenExpanded,
			boolean isLeaf) {
		boolean isPathSelected = this.tree.getSelectionModel().isPathSelected(
				path);
		
		Icon expandIcon = isPathSelected ? this.fColorScheme.getSelectedExpandedIcon() : this.fColorScheme.getUnselectedExpandedIcon();
		Icon collapseIcon = isPathSelected ? this.fColorScheme.getSelectedCollapsedIcon() : this.fColorScheme.getUnselectedCollapsedIcon();
		
		boolean setIcon = true;
		
		this.setExpandedIcon(setIcon ? expandIcon : null);
		this.setCollapsedIcon(setIcon ? collapseIcon : null);
		
		super.paintExpandControl(
				g,
				clipBounds,
				insets,
				bounds,
				path,
				row,
				isExpanded,
				hasBeenExpanded,
				isLeaf);
	}
	
	@Override
	protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
		return new NodeDimensionsHandler() {
			
			@Override
			public Rectangle getNodeDimensions(
					Object value,
					int row,
					int depth,
					boolean expanded,
					Rectangle size) {
				
				Rectangle dimensions = super.getNodeDimensions(
						value,
						row,
						depth,
						expanded,
						size);
				int containerWidth = SearcherTreeUI.this.tree.getParent() instanceof JViewport ? SearcherTreeUI.this.tree.getParent().getWidth() : SearcherTreeUI.this.tree.getWidth();
				
				dimensions.width = containerWidth - this.getRowX(row, depth);
				
				return dimensions;
			}
		};
	}
	
	@Override
	public Rectangle getPathBounds(JTree tree, TreePath path) {
		Rectangle bounds = super.getPathBounds(tree, path);
		
		if (bounds != null) {
			bounds.x = 0;
			bounds.width = tree.getWidth();
		}
		
		return bounds;
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D backgroundGraphics = (Graphics2D) g.create();
		this.fBackgroundPainter.paint(
				backgroundGraphics,
				c,
				c.getWidth(),
				c.getHeight());
		backgroundGraphics.dispose();
		
		int selectedRow = this.getSelectionModel().getLeadSelectionRow();
		if (selectedRow >= 0
				&& this.tree.isVisible(this.tree.getPathForRow(selectedRow))) {
			
			Rectangle bounds = this.tree.getRowBounds(selectedRow);
			
			Graphics2D selectionBackgroundGraphics = (Graphics2D) g.create();
			selectionBackgroundGraphics.translate(0, bounds.y);
			this.fSelectionBackgroundPainter.paint(
					selectionBackgroundGraphics,
					c,
					c.getWidth(),
					bounds.height);
			selectionBackgroundGraphics.dispose();
		}
		
		super.paint(g, c);
	}
	
	@Override
	protected void paintHorizontalLine(
			Graphics g,
			JComponent c,
			int y,
			int left,
			int right) {

	}
	
	@Override
	protected void paintVerticalPartOfLeg(
			Graphics g,
			Rectangle clipBounds,
			Insets insets,
			TreePath path) {

	}
	
	@Override
	protected void selectPathForEvent(TreePath path, MouseEvent event) {
		if (!this.isLocationInExpandControl(path, event.getX(), event.getY())) {
			super.selectPathForEvent(path, event);
		}
	}
	
	private Action createNextAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = SearcherTreeUI.this.tree.getLeadSelectionRow();
				int rowToSelect = selectedRow + 1;
				while (rowToSelect >= 0
						&& rowToSelect < SearcherTreeUI.this.tree.getRowCount()) {
					if (SearcherTreeUI.this.isItemRow(rowToSelect)) {
						SearcherTreeUI.this.tree.setSelectionRow(rowToSelect);
						break;
					} else {
						rowToSelect++;
					}
				}
			}
		};
	}
	
	private Action createPreviousAction() {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = SearcherTreeUI.this.tree.getLeadSelectionRow();
				int rowToSelect = selectedRow - 1;
				while (rowToSelect >= 0
						&& rowToSelect < SearcherTreeUI.this.tree.getRowCount()) {
					if (SearcherTreeUI.this.isItemRow(rowToSelect)) {
						SearcherTreeUI.this.tree.setSelectionRow(rowToSelect);
						break;
					} else {
						rowToSelect--;
					}
				}
			}
		};
	}
	
	private boolean isCategoryRow(int row) {
		return !this.isItemRow(row);
	}
	
	private boolean isItemRow(int row) {
		return this.isItemPath(this.tree.getPathForRow(row));
	}
	
	private boolean isItemPath(TreePath path) {
		if (path == null)
			return false;
		
		return !(path.getLastPathComponent() instanceof SearcherCategory);
	}
	
	private String getTextForNode(
			TreeNode node,
			boolean selected,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		String retVal;
		
		if (node instanceof SearcherNode) {
			retVal = ((SearcherNode) node).getText();
		} else {
			retVal = this.tree.convertValueToText(
					node,
					selected,
					expanded,
					leaf,
					row,
					hasFocus);
		}
		
		return retVal;
	}
	
	private Icon getIconForNode(TreeNode node) {
		Icon retVal = null;
		
		if (node instanceof SearcherNode) {
			retVal = ((SearcherNode) node).getIcon();
		}
		
		return retVal;
	}
	
	private static void checkColorSchemeNotNull(
			SourceListColorScheme colorScheme) {
		if (colorScheme == null) {
			throw new IllegalArgumentException(
					"The given SourceListColorScheme cannot be null.");
		}
	}
	
	private class CustomTreeModelListener implements TreeModelListener {
		
		@Override
		public void treeNodesChanged(TreeModelEvent e) {

		}
		
		@Override
		public void treeNodesInserted(TreeModelEvent e) {
			TreePath path = e.getTreePath();
			Object root = SearcherTreeUI.this.tree.getModel().getRoot();
			TreePath pathToRoot = new TreePath(root);
			if (path != null
					&& path.getParentPath() != null
					&& path.getParentPath().getLastPathComponent().equals(root)
					&& !SearcherTreeUI.this.tree.isExpanded(pathToRoot)) {
				TreeUtils.expandPathOnEdt(
						SearcherTreeUI.this.tree,
						new TreePath(root));
			}
		}
		
		@Override
		public void treeNodesRemoved(TreeModelEvent e) {

		}
		
		@Override
		public void treeStructureChanged(TreeModelEvent e) {

		}
	}
	
	private class SourceListTreeCellRenderer implements TreeCellRenderer {
		
		private CategoryTreeCellRenderer iCategoryRenderer = new CategoryTreeCellRenderer();
		private ItemTreeCellRenderer iItemRenderer = new ItemTreeCellRenderer();
		
		@Override
		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean selected,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {
			
			TreeCellRenderer render = SearcherTreeUI.this.isCategoryRow(row) ? this.iCategoryRenderer : this.iItemRenderer;
			return render.getTreeCellRendererComponent(
					tree,
					value,
					selected,
					expanded,
					leaf,
					row,
					hasFocus);
		}
		
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
	
	private class SourceListTreeSelectionModel extends DefaultTreeSelectionModel {
		
		public SourceListTreeSelectionModel() {
			this.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		
		private boolean canSelect(TreePath path) {
			return SearcherTreeUI.this.isItemPath(path);
		}
		
		@Override
		public void setSelectionPath(TreePath path) {
			if (this.canSelect(path)) {
				super.setSelectionPath(path);
			}
		}
		
		@Override
		public void setSelectionPaths(TreePath[] paths) {
			if (this.canSelect(paths[0])) {
				super.setSelectionPaths(paths);
			}
		}
	}
	
}
