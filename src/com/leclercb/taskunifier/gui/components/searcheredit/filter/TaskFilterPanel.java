package com.leclercb.taskunifier.gui.components.searcheredit.filter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.Link;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.translations.Translations;

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
		return tree;
	}

	private void initialize() {
		this.setLayout(new BorderLayout());

		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BorderLayout());
		treePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		this.tree = new TaskFilterTree(filter);
		this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				if (tree.getSelectionCount() != 0) {
					TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();

					if (node instanceof TaskFilterTreeNode) {
						if (((TaskFilterTreeNode) node).getFilter().getParent() != null) {
							removeButton.setEnabled(true);
						} else {
							removeButton.setEnabled(false);
						}

						addElementButton.setEnabled(true);
						addFilterButton.setEnabled(true);
						return;
					} else if (node instanceof TaskFilterElementTreeNode) {
						addElementButton.setEnabled(false);
						addFilterButton.setEnabled(false);
						removeButton.setEnabled(true);
						return;
					}
				}

				addElementButton.setEnabled(false);
				addFilterButton.setEnabled(false);
				removeButton.setEnabled(false);
			}

		});
		this.tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON3) {
					TreePath path = tree.getPathForLocation(event.getX(), event.getY());
					final TreeNode node = (TreeNode) path.getLastPathComponent();

					if (node instanceof TaskFilterTreeNode) {
						JPopupMenu popup = new JPopupMenu();

						ButtonGroup group = new ButtonGroup();

						JRadioButtonMenuItem itemAnd = new JRadioButtonMenuItem(Link.AND.toString());
						JRadioButtonMenuItem itemOr = new JRadioButtonMenuItem(Link.OR.toString());

						group.add(itemAnd);
						group.add(itemOr);

						popup.add(itemAnd);
						popup.add(itemOr);

						if (((TaskFilterTreeNode) node).getFilter().getLink().equals(Link.AND))
							itemAnd.setSelected(true);
						else
							itemOr.setSelected(true);

						itemAnd.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent evt) {
								((TaskFilterTreeNode) node).getFilter().setLink(Link.AND);
							}

						});

						itemOr.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent evt) {
								((TaskFilterTreeNode) node).getFilter().setLink(Link.OR);
							}

						});

						popup.show(event.getComponent(), event.getX(), event.getY());
					}
				}
			}

		});

		treePanel.add(new JScrollPane(this.tree), BorderLayout.CENTER);

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
					TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();

					if (node == null || !(node instanceof TaskFilterTreeNode))
						return;

					if (event.getActionCommand().equals("ADD_ELEMENT")) {
						TaskFilterElement element = new TaskFilterElement(
								TaskColumn.TITLE, 
								StringCondition.EQUALS, 
								""
						);

						((TaskFilterTreeNode) node).getFilter().addElement(element);
					} else if (event.getActionCommand().equals("ADD_FILTER")) {
						((TaskFilterTreeNode) node).getFilter().addFilter(new TaskFilter());
					}

					for (int i=0; i<tree.getRowCount(); i++)
						tree.expandRow(i);
				} else {
					TreeNode node = (TreeNode) tree.getLastSelectedPathComponent();

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

		addElementButton = new JButton(Translations.getString("searcheredit.add_element"), Images.getResourceImage("add.png", 16, 16));
		addElementButton.setActionCommand("ADD_ELEMENT");
		addElementButton.addActionListener(listener);
		addElementButton.setEnabled(false);
		buttonsPanel.add(addElementButton);

		addFilterButton = new JButton(Translations.getString("searcheredit.add_filter"), Images.getResourceImage("add.png", 16, 16));
		addFilterButton.setActionCommand("ADD_FILTER");
		addFilterButton.addActionListener(listener);
		addFilterButton.setEnabled(false);
		buttonsPanel.add(addFilterButton);

		removeButton = new JButton(Images.getResourceImage("remove.png", 16, 16));
		removeButton.setActionCommand("REMOVE");
		removeButton.addActionListener(listener);
		removeButton.setEnabled(false);
		buttonsPanel.add(removeButton);
	}

}
