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
package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TaskFilterTree extends JTree {
	
	public TaskFilterTree(TaskFilter filter) {
		super(new TaskFilterTreeModel(filter));
		this.initialize();
	}
	
	private void initialize() {
		// Warning: might not work with all UIs
		this.setLargeModel(true);
		this.setRootVisible(true);
		
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(ImageUtils.getResourceImage("tree_leaf.png"));
		renderer.setOpenIcon(ImageUtils.getResourceImage("tree_open.png"));
		renderer.setClosedIcon(ImageUtils.getResourceImage("tree_closed.png"));
		this.setCellRenderer(renderer);
		
		this.initializePopupMenu();
		
		for (int i = 0; i < this.getRowCount(); i++)
			this.expandRow(i);
	}
	
	private void initializePopupMenu() {
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger()
						|| event.getButton() == MouseEvent.BUTTON3) {
					TreePath path = TaskFilterTree.this.getPathForLocation(
							event.getX(),
							event.getY());
					final TreeNode node = (TreeNode) path.getLastPathComponent();
					
					if (node instanceof TaskFilterTreeNode) {
						JPopupMenu popup = new JPopupMenu();
						
						ButtonGroup group = new ButtonGroup();
						
						JRadioButtonMenuItem itemAnd = new JRadioButtonMenuItem(
								TranslationsUtils.translateFilterLink(FilterLink.AND));
						JRadioButtonMenuItem itemOr = new JRadioButtonMenuItem(
								TranslationsUtils.translateFilterLink(FilterLink.OR));
						
						group.add(itemAnd);
						group.add(itemOr);
						
						popup.add(itemAnd);
						popup.add(itemOr);
						
						if (((TaskFilterTreeNode) node).getFilter().getLink().equals(
								FilterLink.AND))
							itemAnd.setSelected(true);
						else
							itemOr.setSelected(true);
						
						itemAnd.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent evt) {
								((TaskFilterTreeNode) node).getFilter().setLink(
										FilterLink.AND);
							}
							
						});
						
						itemOr.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent evt) {
								((TaskFilterTreeNode) node).getFilter().setLink(
										FilterLink.OR);
							}
							
						});
						
						popup.show(
								event.getComponent(),
								event.getX(),
								event.getY());
					}
				}
			}
			
		});
	}
	
}
