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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.commons.api.utils.OsUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.actions.ActionEditSearcher;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.Link;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class SearcherPanel extends JPanel implements SearcherView, TaskSearcherSelectionListener {
	
	private TaskSearcherSelectionChangeSupport taskSearcherSelectionChangeSupport;
	
	private JTextField filterTitle;
	private SearcherView searcherView;
	
	private JButton addButton;
	private JButton removeButton;
	private JButton editButton;
	
	public SearcherPanel() {
		this.taskSearcherSelectionChangeSupport = new TaskSearcherSelectionChangeSupport(
				this);
		
		this.initialize();
	}
	
	@Override
	public void selectDefaultTaskSearcher() {
		this.searcherView.selectDefaultTaskSearcher();
	}
	
	@Override
	public TaskSearcher getSelectedTaskSearcher() {
		TaskSearcher searcher = this.searcherView.getSelectedTaskSearcher();
		
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
	
	@Override
	public void refreshTaskSearcher() {
		this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(this.getSelectedTaskSearcher());
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.filterTitle = new JTextField();
		
		this.filterTitle.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				SearcherPanel.this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(SearcherPanel.this.getSelectedTaskSearcher());
			}
			
		});
		
		JPanel searchField = ComponentFactory.createSearchField(this.filterTitle);
		searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		this.add(searchField, BorderLayout.NORTH);
		
		if (OsUtils.isMacOSX() && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			this.searcherView = new SearcherList();
			
			this.add(
					new JScrollPane(
							((SearcherList) this.searcherView).getSourceList().getComponent()),
					BorderLayout.CENTER);
			
			this.searcherView.addTaskSearcherSelectionChangeListener(this);
			
			((SearcherList) this.searcherView).getSourceList().addSourceListClickListener(
					new SourceListClickListener() {
						
						@Override
						public void sourceListCategoryClicked(
								SourceListCategory category,
								Button button,
								int clickCount) {

						}
						
						@Override
						public void sourceListItemClicked(
								SourceListItem category,
								Button button,
								int clickCount) {
							if (clickCount == 2)
								SearcherPanel.this.openTaskSearcherEdit();
						}
						
					});
		} else {
			this.searcherView = new SearcherTree();
			
			this.add(
					new JScrollPane(((SearcherTree) this.searcherView)),
					BorderLayout.CENTER);
			
			this.searcherView.addTaskSearcherSelectionChangeListener(this);
			
			((SearcherTree) this.searcherView).addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2)
						SearcherPanel.this.openTaskSearcherEdit();
				}
				
			});
		}
		
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
					TaskSearcher searcher = TaskSearcherFactory.getInstance().create(
							Translations.getString("searcher.default.title"),
							new TaskFilter(),
							new TaskSorter());
					
					new ActionEditSearcher().editSearcher(searcher);
				} else if (event.getActionCommand().equals("REMOVE")) {
					TaskSearcher searcher = SearcherPanel.this.getSelectedTaskSearcher();
					TaskSearcherFactory.getInstance().unregister(searcher);
				} else if (event.getActionCommand().equals("EDIT")) {
					TaskSearcher searcher = SearcherPanel.this.searcherView.getSelectedTaskSearcher();
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
	
	private void openTaskSearcherEdit() {
		TaskSearcher searcher = SearcherPanel.this.searcherView.getSelectedTaskSearcher();
		
		if (searcher != null
				&& TaskSearcherFactory.getInstance().contains(searcher.getId()))
			new ActionEditSearcher().editSearcher(searcher);
	}
	
	@Override
	public void addTaskSearcherSelectionChangeListener(
			TaskSearcherSelectionListener listener) {
		this.taskSearcherSelectionChangeSupport.addTaskSearcherSelectionChangeListener(listener);
	}
	
	@Override
	public void removeTaskSearcherSelectionChangeListener(
			TaskSearcherSelectionListener listener) {
		this.taskSearcherSelectionChangeSupport.removeTaskSearcherSelectionChangeListener(listener);
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		boolean personalSearcher = false;
		
		if (event.getSelectedTaskSearcher() != null)
			personalSearcher = TaskSearcherFactory.getInstance().contains(
					event.getSelectedTaskSearcher().getId());
		
		this.filterTitle.setText("");
		this.removeButton.setEnabled(personalSearcher);
		this.editButton.setEnabled(personalSearcher);
		
		this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(this.getSelectedTaskSearcher());
	}
	
}
