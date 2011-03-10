package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskFilterPanel extends JPanel {
	
	private TaskFilter filter;
	private TaskFilterTree tree;
	
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
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonsPanel);
	}
	
	private void initializeButtons(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().startsWith("ADD")) {
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
				} else {
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
		buttonsPanel.add(this.addElementButton);
		
		this.addFilterButton = new JButton(
				Translations.getString("searcheredit.add_filter"),
				Images.getResourceImage("add.png", 16, 16));
		this.addFilterButton.setActionCommand("ADD_FILTER");
		this.addFilterButton.addActionListener(listener);
		this.addFilterButton.setEnabled(false);
		buttonsPanel.add(this.addFilterButton);
		
		this.removeButton = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		buttonsPanel.add(this.removeButton);
	}
	
}
