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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskFilterPanel extends JPanel {
	
	private TaskFilter filter;
	private TaskFilterTree tree;
	
	private JButton autoFillButton;
	private JButton addElementButton;
	private JButton addFilterButton;
	private JButton removeButton;
	
	public TaskFilterPanel(TaskFilter filter) {
		this.filter = filter;
		
		this.initialize();
	}
	
	public TaskFilterTree getTree() {
		return this.tree;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BorderLayout());
		
		this.tree = new TaskFilterTree(this.filter);
		this.tree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					
					@Override
					public void valueChanged(TreeSelectionEvent event) {
						if (TaskFilterPanel.this.tree.getSelectionCount() != 0) {
							TreeNode node = (TreeNode) TaskFilterPanel.this.tree.getLastSelectedPathComponent();
							
							if (node instanceof TaskFilterTreeNode) {
								if (((TaskFilterTreeNode) node).getFilter().getParent() != null) {
									TaskFilterPanel.this.removeButton.setEnabled(true);
								} else {
									TaskFilterPanel.this.removeButton.setEnabled(false);
								}
								
								TaskFilterPanel.this.addElementButton.setEnabled(true);
								TaskFilterPanel.this.addFilterButton.setEnabled(true);
								return;
							} else if (node instanceof TaskFilterElementTreeNode) {
								TaskFilterPanel.this.addElementButton.setEnabled(false);
								TaskFilterPanel.this.addFilterButton.setEnabled(false);
								TaskFilterPanel.this.removeButton.setEnabled(true);
								return;
							}
						}
						
						TaskFilterPanel.this.addElementButton.setEnabled(false);
						TaskFilterPanel.this.addFilterButton.setEnabled(false);
						TaskFilterPanel.this.removeButton.setEnabled(false);
					}
					
				});
		
		treePanel.add(
				ComponentFactory.createJScrollPane(this.tree, true),
				BorderLayout.CENTER);
		
		this.add(treePanel, BorderLayout.CENTER);
		
		this.initializeButtons();
	}
	
	private void initializeButtons() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("AUTO_FILL")) {
					TaskFilterPanel.this.filter.clearElement();
					TaskFilterPanel.this.filter.clearFilters();
					
					TaskFilterPanel.this.filter.setLink(FilterLink.OR);
					
					Task[] tasks = ViewType.getTaskView().getTaskTableView().getSelectedTasks();
					for (Task task : tasks) {
						TaskFilterPanel.this.filter.addElement(new TaskFilterElement(
								TaskColumn.MODEL,
								ModelCondition.EQUALS,
								task));
					}
				} else if (event.getActionCommand().startsWith("ADD")) {
					TreeNode node = (TreeNode) TaskFilterPanel.this.tree.getLastSelectedPathComponent();
					
					if (node == null || !(node instanceof TaskFilterTreeNode))
						return;
					
					if (event.getActionCommand().equals("ADD_ELEMENT")) {
						TaskFilterElement element = new TaskFilterElement(
								TaskColumn.TITLE,
								StringCondition.EQUALS,
								"");
						
						((TaskFilterTreeNode) node).getFilter().addElement(
								element);
					} else if (event.getActionCommand().equals("ADD_FILTER")) {
						((TaskFilterTreeNode) node).getFilter().addFilter(
								new TaskFilter());
					}
					
					for (int i = 0; i < TaskFilterPanel.this.tree.getRowCount(); i++)
						TaskFilterPanel.this.tree.expandRow(i);
				} else if (event.getActionCommand().equals("REMOVE")) {
					TreeNode node = (TreeNode) TaskFilterPanel.this.tree.getLastSelectedPathComponent();
					
					if (node == null)
						return;
					
					if (node instanceof TaskFilterTreeNode) {
						((TaskFilterTreeNode) node).getFilter().getParent().removeFilter(
								((TaskFilterTreeNode) node).getFilter());
					} else if (node instanceof TaskFilterElementTreeNode) {
						((TaskFilterElementTreeNode) node).getElement().getParent().removeElement(
								((TaskFilterElementTreeNode) node).getElement());
					}
				}
			}
			
		};
		
		this.addElementButton = new JButton(
				Translations.getString("searcheredit.add_element"),
				Images.getResourceImage("add.png", 16, 16));
		this.addElementButton.setActionCommand("ADD_ELEMENT");
		this.addElementButton.addActionListener(listener);
		this.addElementButton.setEnabled(false);
		
		this.addFilterButton = new JButton(
				Translations.getString("searcheredit.add_filter"),
				Images.getResourceImage("add.png", 16, 16));
		this.addFilterButton.setActionCommand("ADD_FILTER");
		this.addFilterButton.addActionListener(listener);
		this.addFilterButton.setEnabled(false);
		
		this.removeButton = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		
		this.autoFillButton = new JButton(
				Translations.getString("searcheredit.clear_and_auto_fill_with_selected_tasks"),
				Images.getResourceImage("synchronize.png", 16, 16));
		this.autoFillButton.setActionCommand("AUTO_FILL");
		this.autoFillButton.addActionListener(listener);
		this.autoFillButton.setEnabled(true);
		
		// Do not show the auto fill button
		this.autoFillButton.setVisible(false);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				this.addElementButton,
				this.addFilterButton,
				this.removeButton,
				this.autoFillButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
}
