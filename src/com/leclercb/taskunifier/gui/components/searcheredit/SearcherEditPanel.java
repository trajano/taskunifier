package com.leclercb.taskunifier.gui.components.searcheredit;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterElementPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterElementTreeNode;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.searcher.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterPanel;

public class SearcherEditPanel extends JPanel implements TreeSelectionListener {
	
	private TaskSearcherPanel searcherPanel;
	private TaskFilterElementPanel elementPanel;
	private TaskSorterPanel sorterPanel;
	private TaskFilterPanel filterPanel;
	
	public SearcherEditPanel(TaskSearcher searcher) {
		this.initialize(searcher);
	}
	
	private void initialize(TaskSearcher searcher) {
		this.setLayout(new BorderLayout());
		
		this.add(Help.getHelpButton("searcher.html"), BorderLayout.NORTH);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.CENTER);
		
		this.searcherPanel = new TaskSearcherPanel(searcher);
		panel.add(this.searcherPanel, BorderLayout.NORTH);
		
		this.elementPanel = new TaskFilterElementPanel();
		panel.add(this.elementPanel, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		panel.add(splitPane, BorderLayout.CENTER);
		
		this.sorterPanel = new TaskSorterPanel(searcher.getSorter());
		splitPane.setLeftComponent(this.sorterPanel);
		
		this.filterPanel = new TaskFilterPanel(searcher.getFilter());
		this.filterPanel.getTree().addTreeSelectionListener(this);
		splitPane.setRightComponent(this.filterPanel);
		
		splitPane.setDividerLocation(300);
	}
	
	public void close() {
		this.elementPanel.saveElement();
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent evt) {
		this.elementPanel.saveElement();
		
		if (this.filterPanel.getTree().getSelectionCount() != 0) {
			TreeNode node = (TreeNode) this.filterPanel.getTree().getLastSelectedPathComponent();
			
			if (node instanceof TaskFilterElementTreeNode) {
				this.elementPanel.setElement(((TaskFilterElementTreeNode) node).getElement());
				return;
			}
		}
		
		this.elementPanel.setElement(null);
	}
	
}
