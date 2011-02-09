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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListItem;
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
import com.leclercb.taskunifier.gui.swing.macwidgets.SourceListControlBar;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class SearcherPanel extends JPanel implements SearcherView, TaskSearcherSelectionListener {
	
	private TaskSearcherSelectionChangeSupport taskSearcherSelectionChangeSupport;
	
	private JTextField filterTitle;
	private SearcherList searcherView;
	
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
		
		this.searcherView = new SearcherList();
		
		this.add(ComponentFactory.createJScrollPane(
				this.searcherView.getSourceList().getComponent(),
				true), BorderLayout.CENTER);
		
		this.searcherView.addTaskSearcherSelectionChangeListener(this);
		
		this.searcherView.getSourceList().addSourceListClickListener(
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
		
		this.initializeButtons();
	}
	
	private void initializeButtons() {
		SourceListControlBar controlBar = new SourceListControlBar();
		controlBar.hideResizeHandle();
		this.searcherView.getSourceList().installSourceListControlBar(
				controlBar);
		
		this.addButton = controlBar.createAndAddButton(
				Images.getResourceImage("add.png", 16, 16),
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						TaskSearcherFactory.getInstance().create(
								Translations.getString("searcher.default.title"),
								new TaskFilter(),
								new TaskSorter());
						
						SearcherPanel.this.openTaskSearcherEdit();
					}
					
				});
		
		this.removeButton = controlBar.createAndAddButton(
				Images.getResourceImage("remove.png", 16, 16),
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						TaskSearcher searcher = SearcherPanel.this.getSelectedTaskSearcher();
						TaskSearcherFactory.getInstance().unregister(searcher);
					}
					
				});
		
		this.removeButton.setEnabled(false);
		
		this.editButton = controlBar.createAndAddButton(
				Images.getResourceImage("edit.png", 16, 16),
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						SearcherPanel.this.openTaskSearcherEdit();
					}
					
				});
		
		this.editButton.setEnabled(false);
	}
	
	private void openTaskSearcherEdit() {
		TaskSearcher searcher = SearcherPanel.this.searcherView.getSelectedTaskSearcher();
		
		if (searcher != null
				&& TaskSearcherFactory.getInstance().contains(searcher.getId())) {
			new ActionEditSearcher().editSearcher(searcher);
			this.searcherView.updateBadges();
		}
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
		this.addButton.setEnabled(true);
		this.removeButton.setEnabled(personalSearcher);
		this.editButton.setEnabled(personalSearcher);
		
		this.taskSearcherSelectionChangeSupport.fireTaskSearcherSelectionChange(this.getSelectedTaskSearcher());
	}
	
}
