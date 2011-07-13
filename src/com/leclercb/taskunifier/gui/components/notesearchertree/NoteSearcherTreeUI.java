/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.notesearchertree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.SourceListColorScheme;
import com.explodingpixels.macwidgets.SourceListCountBadgeRenderer;
import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.explodingpixels.painter.FocusStatePainter;
import com.explodingpixels.painter.RectanglePainter;
import com.explodingpixels.widgets.WindowUtils;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherNode;

public class NoteSearcherTreeUI extends BasicTreeUI {
	
	private Font categoryFont = UIManager.getFont("Label.font").deriveFont(
			Font.BOLD);
	private Font itemFont = UIManager.getFont("Label.font").deriveFont(
			Font.PLAIN);
	private Font itemSelectedFont = UIManager.getFont("Label.font").deriveFont(
			Font.BOLD);
	
	private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
	
	private SourceListColorScheme colorScheme;
	private FocusStatePainter backgroundPainter;
	private FocusStatePainter selectionBackgroundPainter;
	
	public NoteSearcherTreeUI() {

	}
	
	@Override
	protected void completeUIInstall() {
		super.completeUIInstall();
		this.setColorScheme(new SourceListStandardColorScheme());
	}
	
	public void setColorScheme(SourceListColorScheme colorScheme) {
		this.colorScheme = colorScheme;
		this.backgroundPainter = new FocusStatePainter(
				new RectanglePainter(
						this.colorScheme.getActiveBackgroundColor()),
				new RectanglePainter(
						this.colorScheme.getActiveBackgroundColor()),
				new RectanglePainter(
						this.colorScheme.getActiveBackgroundColor()));
		this.selectionBackgroundPainter = new FocusStatePainter(
				this.colorScheme.getActiveFocusedSelectedItemPainter(),
				this.colorScheme.getActiveUnfocusedSelectedItemPainter(),
				this.colorScheme.getInactiveSelectedItemPainter());
		
		this.tree.setCellRenderer(new SearcherTreeCellRenderer());
		
		this.installDisclosureIcons();
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		WindowUtils.installJComponentRepainterOnWindowFocusChanged(this.tree);
	}
	
	private void installDisclosureIcons() {
		this.setCollapsedIcon(this.colorScheme.getUnselectedCollapsedIcon());
		this.setExpandedIcon(this.colorScheme.getUnselectedExpandedIcon());
		int indent = this.colorScheme.getUnselectedCollapsedIcon().getIconWidth() / 2 + 4;
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
		
		Icon expandIcon = isPathSelected ? this.colorScheme.getSelectedExpandedIcon() : this.colorScheme.getUnselectedExpandedIcon();
		Icon collapseIcon = isPathSelected ? this.colorScheme.getSelectedCollapsedIcon() : this.colorScheme.getUnselectedCollapsedIcon();
		
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
				int containerWidth = NoteSearcherTreeUI.this.tree.getParent() instanceof JViewport ? NoteSearcherTreeUI.this.tree.getParent().getWidth() : NoteSearcherTreeUI.this.tree.getWidth();
				
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
		this.backgroundPainter.paint(
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
			this.selectionBackgroundPainter.paint(
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
	
	private class SearcherTreeCellRenderer implements TreeCellRenderer {
		
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
		
	}
	
	private class CategoryTreeCellRenderer implements TreeCellRenderer {
		
		private JLabel label = MacWidgetFactory.makeEmphasizedLabel(
				new JLabel(),
				NoteSearcherTreeUI.this.colorScheme.getCategoryTextColor(),
				NoteSearcherTreeUI.this.colorScheme.getCategoryTextColor(),
				NoteSearcherTreeUI.this.colorScheme.getCategoryTextShadowColor());
		
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
			SearcherCategory node = (SearcherCategory) value;
			
			this.label.setFont(NoteSearcherTreeUI.this.categoryFont);
			this.label.setText(node.getTitle());
			
			return this.label;
		}
		
	}
	
	private class ItemTreeCellRenderer implements TreeCellRenderer {
		
		private PanelBuilder fBuilder;
		
		private SourceListCountBadgeRenderer normalCountRenderer = new SourceListCountBadgeRenderer(
				NoteSearcherTreeUI.this.colorScheme.getSelectedBadgeColor(),
				NoteSearcherTreeUI.this.colorScheme.getActiveUnselectedBadgeColor(),
				NoteSearcherTreeUI.this.colorScheme.getInativeUnselectedBadgeColor(),
				NoteSearcherTreeUI.this.colorScheme.getBadgeTextColor());
		
		private JLabel selectedLabel = MacWidgetFactory.makeEmphasizedLabel(
				new JLabel(),
				NoteSearcherTreeUI.this.colorScheme.getSelectedItemTextColor(),
				NoteSearcherTreeUI.this.colorScheme.getSelectedItemTextColor(),
				NoteSearcherTreeUI.this.colorScheme.getSelectedItemFontShadowColor());
		
		private JLabel unselectedLabel = MacWidgetFactory.makeEmphasizedLabel(
				new JLabel(),
				NoteSearcherTreeUI.this.colorScheme.getUnselectedItemTextColor(),
				NoteSearcherTreeUI.this.colorScheme.getUnselectedItemTextColor(),
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
			this.selectedLabel.setFont(NoteSearcherTreeUI.this.itemSelectedFont);
			this.unselectedLabel.setFont(NoteSearcherTreeUI.this.itemFont);
			
			SearcherNode node = (SearcherNode) value;
			JLabel label = (selected ? this.selectedLabel : this.unselectedLabel);
			label.setText(node.getText());
			label.setIcon(node.getIcon());
			
			this.fBuilder.getPanel().removeAll();
			CellConstraints cc = new CellConstraints();
			this.fBuilder.add(label, cc.xywh(1, 1, 1, 3));
			
			if (node.getBadgeCount() != null) {
				if (node.getBadgeCount().getNormalCount() != 0) {
					this.normalCountRenderer.setState(
							node.getBadgeCount().getNormalCount(),
							selected);
					this.fBuilder.add(
							this.normalCountRenderer.getComponent(),
							cc.xy(3, 2, "center, fill"));
				}
			}
			
			this.fBuilder.getPanel().setToolTipText(
					"<html>"
							+ node.getNoteSearcher().getTitle()
							+ "<br />"
							+ node.getNoteSearcher().getSorter()
							+ "<br />"
							+ node.getNoteSearcher().getFilter()
							+ "</html>");
			
			return this.fBuilder.getPanel();
		}
		
	}
	
}
