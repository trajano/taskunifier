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
package com.leclercb.taskunifier.gui.components.searcheredit;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterElementPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterElementTreeNode;
import com.leclercb.taskunifier.gui.components.searcheredit.filter.TaskFilterPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.searcher.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterPanel;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class SearcherEditPanel extends JPanel implements TreeSelectionListener {
	
	private TaskSearcher searcher;
	
	private TaskSearcherPanel searcherPanel;
	private TaskFilterElementPanel elementPanel;
	private TaskSorterPanel sorterPanel;
	private TaskFilterPanel filterPanel;
	private JTextArea searcherInfo;
	
	public SearcherEditPanel(TaskSearcher searcher) {
		this.searcher = searcher;
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.CENTER);
		
		JPanel searcherInfoPanel = new JPanel(new BorderLayout());
		searcherInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.searcherInfo = new JTextArea(3, 0);
		this.searcherInfo.setEditable(false);
		this.searcherInfo.setLineWrap(true);
		this.searcherInfo.setWrapStyleWord(true);
		
		searcherInfoPanel.add(ComponentFactory.createJScrollPane(
				this.searcherInfo,
				true));
		this.add(searcherInfoPanel, BorderLayout.SOUTH);
		
		this.searcherPanel = new TaskSearcherPanel(this.searcher);
		panel.add(this.searcherPanel, BorderLayout.NORTH);
		
		this.elementPanel = new TaskFilterElementPanel();
		panel.add(this.elementPanel, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		panel.add(splitPane, BorderLayout.CENTER);
		
		this.sorterPanel = new TaskSorterPanel(this.searcher.getSorter());
		splitPane.setLeftComponent(this.sorterPanel);
		
		this.filterPanel = new TaskFilterPanel(this.searcher.getFilter());
		this.filterPanel.getTree().addTreeSelectionListener(this);
		splitPane.setRightComponent(this.filterPanel);
		
		splitPane.setDividerLocation(300);
		
		this.searcherInfo.setText(this.searcher.getFilter().toString());
	}
	
	public void close() {
		this.elementPanel.saveElement();
		this.searcherInfo.setText(this.searcher.getFilter().toString());
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent evt) {
		this.elementPanel.saveElement();
		
		if (this.filterPanel.getTree().getSelectionCount() != 0) {
			TreeNode node = (TreeNode) this.filterPanel.getTree().getLastSelectedPathComponent();
			
			if (node instanceof TaskFilterElementTreeNode) {
				this.elementPanel.setElement(((TaskFilterElementTreeNode) node).getElement());
				this.searcherInfo.setText(this.searcher.getFilter().toString());
				return;
			}
		}
		
		this.elementPanel.setElement(null);
		
		this.searcherInfo.setText(this.searcher.getFilter().toString());
	}
	
}
