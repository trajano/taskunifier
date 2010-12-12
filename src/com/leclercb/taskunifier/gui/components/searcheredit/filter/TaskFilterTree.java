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

import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.Link;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class TaskFilterTree extends JTree {

	public TaskFilterTree(TaskFilter filter) {
		super(new TaskFilterTreeModel(filter));
		this.initialize();
	}

	private void initialize() {
		this.setRootVisible(true);

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(Images.getResourceImage("tree_leaf.png"));
		renderer.setOpenIcon(Images.getResourceImage("tree_open.png"));
		renderer.setClosedIcon(Images.getResourceImage("tree_closed.png"));
		this.setCellRenderer(renderer);

		this.initializePopupMenu();

		for (int i=0; i<this.getRowCount(); i++)
			this.expandRow(i);
	}

	private void initializePopupMenu() {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON3) {
					TreePath path = getPathForLocation(event.getX(), event.getY());
					final TreeNode node = (TreeNode) path.getLastPathComponent();

					if (node instanceof TaskFilterTreeNode) {
						JPopupMenu popup = new JPopupMenu();

						ButtonGroup group = new ButtonGroup();

						JRadioButtonMenuItem itemAnd = new JRadioButtonMenuItem(TranslationsUtils.translateTaskFilterLink(Link.AND));
						JRadioButtonMenuItem itemOr = new JRadioButtonMenuItem(TranslationsUtils.translateTaskFilterLink(Link.OR));

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
	}

}
