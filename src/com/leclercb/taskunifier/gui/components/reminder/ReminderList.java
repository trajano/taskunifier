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
package com.leclercb.taskunifier.gui.components.reminder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskDescription;
import com.leclercb.taskunifier.gui.components.models.lists.ModelRowFilter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskAlternateHighlighter;
import com.leclercb.taskunifier.gui.components.views.interfaces.TaskSelectionView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class ReminderList extends JPanel implements TaskSelectionView {
	
	private JXSearchField searchField;
	
	private JXList modelList;
	private ModelRowFilter rowFilter;
	
	public ReminderList() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 3));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.modelList = new JXList();
		
		this.modelList.setModel(new DefaultListModel());
		this.modelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.modelList.setCellRenderer(new DefaultListRenderer(
				StringValueTaskDescription.INSTANCE,
				IconValueTaskPriority.INSTANCE));
		
		this.modelList.setAutoCreateRowSorter(true);
		this.modelList.setComparator(ReminderComparator.INSTANCE);
		this.modelList.setSortOrder(SortOrder.ASCENDING);
		this.modelList.setSortsOnUpdates(true);
		
		this.rowFilter = new ModelRowFilter();
		this.modelList.setRowFilter(this.rowFilter);
		
		this.modelList.setHighlighters(new TaskAlternateHighlighter());
		
		this.add(
				ComponentFactory.createJScrollPane(this.modelList, true),
				BorderLayout.CENTER);
		
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ReminderList.this.rowFilter.setTitle(e.getActionCommand());
			}
			
		});
		
		this.rowFilter.addPropertyChangeListener(
				ModelRowFilter.PROP_TITLE,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ReminderList.this.searchField.setText((String) evt.getNewValue());
						ReminderList.this.modelList.setRowFilter((ModelRowFilter) evt.getSource());
					}
					
				});
		
		this.add(this.searchField, BorderLayout.NORTH);
		
		this.modelList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1
						&& event.getClickCount() == 2) {
					int index = ReminderList.this.modelList.locationToIndex(event.getPoint());
					
					if (index == -1)
						return;
					
					index = ReminderList.this.modelList.convertIndexToModel(index);
					Object item = ReminderList.this.modelList.getModel().getElementAt(
							index);
					ReminderList.this.modelList.ensureIndexIsVisible(index);
					
					ActionEditTasks.editTasks(new Task[] { (Task) item });
				}
			}
			
		});
	}
	
	public void addTask(Task task) {
		DefaultListModel model = (DefaultListModel) this.modelList.getModel();
		
		if (model.contains(task))
			return;
		
		model.addElement(task);
	}
	
	public void removeTask(Task task) {
		DefaultListModel model = (DefaultListModel) this.modelList.getModel();
		model.removeElement(task);
	}
	
	public Task[] getTasks() {
		DefaultListModel model = (DefaultListModel) this.modelList.getModel();
		List<Task> tasks = new ArrayList<Task>();
		
		for (int i = 0; i < model.getSize(); i++) {
			tasks.add((Task) model.getElementAt(i));
		}
		
		return tasks.toArray(new Task[0]);
	}
	
	@Override
	public Task[] getSelectedTasks() {
		Object[] values = this.modelList.getSelectedValues();
		List<Task> tasks = new ArrayList<Task>();
		
		for (Object value : values) {
			tasks.add((Task) value);
		}
		
		return tasks.toArray(new Task[0]);
	}
	
	@Override
	public void setSelectedTasks(Task[] tasks) {
		DefaultListModel model = (DefaultListModel) this.modelList.getModel();
		
		this.modelList.getSelectionModel().setValueIsAdjusting(true);
		this.modelList.getSelectionModel().clearSelection();
		
		for (Task task : tasks) {
			int index = model.indexOf(task);
			if (index != -1)
				this.modelList.getSelectionModel().addSelectionInterval(
						index,
						index);
		}
		
		this.modelList.getSelectionModel().setValueIsAdjusting(false);
	}
	
}
