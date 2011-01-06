/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionEditSearcher;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.Link;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SearcherPanel extends JPanel implements SearcherView, TreeSelectionListener {
	
	public static final String ACT_SEARCHER_SELECTED = "SEARCHER_SELECTED";
	
	private List<ActionListener> listeners;
	
	private JTextField filterTitle;
	private SearcherTree searcherTree;
	
	private JButton addButton;
	private JButton removeButton;
	private JButton editButton;
	
	public SearcherPanel() {
		this.initialize();
	}
	
	@Override
	public void selectDefaultTaskSearcher() {
		this.searcherTree.selectDefaultTaskSearcher();
	}
	
	@Override
	public TaskSearcher getSelectedTaskSearcher() {
		TaskSearcher searcher = this.searcherTree.getSelectedTaskSearcher();
		
		if (searcher == null)
			return null;
		
		if (this.filterTitle.getText().length() == 0)
			return searcher;
		
		searcher = searcher.clone();
		
		TaskFilter originalFilter = searcher.getFilter();
		
		TaskFilter newFilter = new TaskFilter();
		newFilter.setLink(Link.AND);
		newFilter.addElement(new TaskFilterElement(
				TaskColumn.TITLE,
				StringCondition.CONTAINS,
				this.filterTitle.getText()));
		newFilter.addFilter(originalFilter);
		
		searcher.setFilter(newFilter);
		
		return searcher;
	}
	
	public void addActionListener(ActionListener listener) {
		CheckUtils.isNotNull(listener, "Listener cannot be null");
		
		if (!this.listeners.contains(listener))
			this.listeners.add(listener);
	}
	
	public void removeActionListener(ActionListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void fireActionPerformed(String command) {
		for (ActionListener listener : this.listeners)
			listener.actionPerformed(new ActionEvent(
					this,
					ActionEvent.ACTION_PERFORMED,
					command));
	}
	
	private void initialize() {
		this.listeners = new ArrayList<ActionListener>();
		this.searcherTree = new SearcherTree();
		this.searcherTree.addTreeSelectionListener(this);
		
		this.setLayout(new BorderLayout());
		
		this.filterTitle = new JTextField();
		
		this.filterTitle.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				SearcherPanel.this.fireActionPerformed(ACT_SEARCHER_SELECTED);
			}
			
		});
		
		JPanel panel = new JPanel(new BorderLayout(5, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		panel.add(
				new JLabel(Images.getResourceImage("search.png", 16, 16)),
				BorderLayout.WEST);
		panel.add(this.filterTitle, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.NORTH);
		
		this.add(new JScrollPane(this.searcherTree), BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonsPanel);
	}
	
	private void initializeButtons(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					TaskSearcherFactory.getInstance().create(
							Translations.getString("searcher.default.title"),
							new TaskFilter(),
							new TaskSorter());
				} else if (event.getActionCommand().equals("REMOVE")) {
					TaskSearcher searcher = SearcherPanel.this.getSelectedTaskSearcher();
					TaskSearcherFactory.getInstance().unregister(searcher);
				} else if (event.getActionCommand().equals("EDIT")) {
					TaskSearcher searcher = SearcherPanel.this.getSelectedTaskSearcher();
					new ActionEditSearcher().editSearcher(searcher);
				}
			}
			
		};
		
		this.addButton = new JButton(Images.getResourceImage("add.png", 16, 16));
		this.addButton.setActionCommand("ADD");
		this.addButton.addActionListener(listener);
		buttonsPanel.add(this.addButton);
		
		this.removeButton = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		buttonsPanel.add(this.removeButton);
		
		this.editButton = new JButton(Images.getResourceImage(
				"edit.png",
				16,
				16));
		this.editButton.setActionCommand("EDIT");
		this.editButton.addActionListener(listener);
		this.editButton.setEnabled(false);
		buttonsPanel.add(this.editButton);
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		boolean personalSearcher = TaskSearcherFactory.getInstance().contains(
				this.searcherTree.getSelectedTaskSearcher());
		
		this.filterTitle.setText("");
		this.removeButton.setEnabled(personalSearcher);
		this.editButton.setEnabled(personalSearcher);
		
		this.fireActionPerformed(ACT_SEARCHER_SELECTED);
	}
	
}
