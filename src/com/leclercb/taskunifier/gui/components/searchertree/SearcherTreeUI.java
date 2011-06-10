package com.leclercb.taskunifier.gui.components.searchertree;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreePath;

import com.explodingpixels.macwidgets.SourceListColorScheme;
import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.explodingpixels.painter.FocusStatePainter;
import com.explodingpixels.painter.RectanglePainter;

public class SearcherTreeUI extends BasicTreeUI {
	
	private Font categoryFont = UIManager.getFont("Label.font").deriveFont(
			Font.BOLD,
			11.0f);
	private Font itemFont = UIManager.getFont("Label.font").deriveFont(11.0f);
	private Font itemSelectedFont = this.itemFont.deriveFont(Font.BOLD);
	
	private SourceListColorScheme fColorScheme;
	private FocusStatePainter fBackgroundPainter;
	private FocusStatePainter fSelectionBackgroundPainter;
	
	@Override
	protected void completeUIInstall() {
		super.completeUIInstall();
		
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
	
	public SourceListColorScheme getColorScheme() {
		return this.fColorScheme;
	}
	
	public void setColorScheme(SourceListColorScheme colorScheme) {
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
	
}
