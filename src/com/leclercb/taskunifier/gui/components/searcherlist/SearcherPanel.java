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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionEditSearcher;
import com.leclercb.taskunifier.gui.components.searcherlist.models.ContextTaskSearcherListModel;
import com.leclercb.taskunifier.gui.components.searcherlist.models.FolderTaskSearcherListModel;
import com.leclercb.taskunifier.gui.components.searcherlist.models.GeneralTaskSearcherListModel;
import com.leclercb.taskunifier.gui.components.searcherlist.models.GoalTaskSearcherListModel;
import com.leclercb.taskunifier.gui.components.searcherlist.models.LocationTaskSearcherListModel;
import com.leclercb.taskunifier.gui.components.searcherlist.models.PersonalTaskSearcherListModel;
import com.leclercb.taskunifier.gui.components.searcherlist.models.TaskSearcherListModel;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.swing.JCollapsiblePanel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SearcherPanel extends JPanel implements ListSelectionListener {

	public static final String ACT_SEARCHER_SELECTED = "SEARCHER_SELECTED";

	private List<ActionListener> listeners;

	private ArrayList<JList> lists;
	private JList personalList;

	private JButton addButton;
	private JButton removeButton;

	public SearcherPanel() {
		this.initialize();
	}

	public void selectDefaultTaskSearcher() {
		this.lists.get(0).setSelectedIndex(0);
	}

	public TaskSearcher getSelectedTaskSearcher() {
		for (JList list : this.lists) {
			if (list.getSelectedIndex() != -1) {
				return ((TaskSearcherListModel) list.getModel()).getTaskSearcher(list.getSelectedIndex());
			}
		}

		return null;
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
			listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command));
	}

	private void initialize() {
		this.listeners = new ArrayList<ActionListener>();
		this.lists = new ArrayList<JList>();

		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JList list;

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new GeneralTaskSearcherListModel());
		this.lists.add(list);
		this.initializeList(Translations.getString("searcherlist.general"), list, panel);

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new FolderTaskSearcherListModel());
		this.lists.add(list);
		this.initializeList(Translations.getString("general.folders"), list, panel);

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new ContextTaskSearcherListModel());
		this.lists.add(list);
		this.initializeList(Translations.getString("general.contexts"), list, panel);

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new GoalTaskSearcherListModel());
		this.lists.add(list);
		this.initializeList(Translations.getString("general.goals"), list, panel);

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new LocationTaskSearcherListModel());
		this.lists.add(list);
		this.initializeList(Translations.getString("general.locations"), list, panel);

		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new PersonalTaskSearcherListModel());
		this.lists.add(list);
		this.initializeList(Translations.getString("searcherlist.personal"), list, panel);

		this.personalList = list;
		this.personalList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = SearcherPanel.this.personalList.locationToIndex(e.getPoint());
					SearcherPanel.this.personalList.ensureIndexIsVisible(index);

					Object item = SearcherPanel.this.personalList.getModel().getElementAt(index);
					new ActionEditSearcher().actionPerformed((TaskSearcher) item);
				}
			}

		});

		this.add(panel, BorderLayout.NORTH);

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
					TaskSearcherFactory.getInstance().create(Translations.getString("searcher.default.title"),
							new TaskFilter(),
							new TaskSorter());
				} else {
					TaskSearcher searcher = (TaskSearcher) SearcherPanel.this.personalList.getSelectedValue();
					TaskSearcherFactory.getInstance().unregister(searcher);
				}
			}

		};

		this.addButton = new JButton(Images.getResourceImage("add.png", 16, 16));
		this.addButton.setActionCommand("ADD");
		this.addButton.addActionListener(listener);
		buttonsPanel.add(this.addButton);

		this.removeButton = new JButton(Images.getResourceImage("remove.png", 16, 16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		buttonsPanel.add(this.removeButton);
	}

	private void initializeList(String title, JList list, JPanel panel) {
		HeaderPanel headerPanel = new HeaderPanel(title);

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new LineBorder(Color.GRAY));
		contentPanel.setLayout(new BorderLayout());

		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setCellRenderer(new SearcherListRenderer());
		list.getSelectionModel().addListSelectionListener(this);

		contentPanel.add(list, BorderLayout.CENTER);

		final JCollapsiblePanel collapsiblePanel = new JCollapsiblePanel(headerPanel, contentPanel);
		collapsiblePanel.toggleSelection();

		headerPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				collapsiblePanel.toggleSelection();
			}
		});

		panel.add(collapsiblePanel);
		panel.add(Box.createVerticalStrut(10));
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;

		if (e.getSource().equals(this.personalList.getSelectionModel()))
			this.removeButton.setEnabled(this.personalList.getSelectedIndex() != -1);

		for (JList list : this.lists) {
			if (e.getSource().equals(list.getSelectionModel())) {
				if (list.getSelectedIndex() == -1)
					return;

				this.removeSelection(list);
				this.fireActionPerformed(ACT_SEARCHER_SELECTED);
			}
		}
	}

	private void removeSelection(JList exception) {
		for (JList list : this.lists)
			if (exception != list)
				list.removeSelectionInterval(0, list.getModel().getSize());
	}

}
