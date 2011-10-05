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
package com.leclercb.taskunifier.gui.components.searcheredit.sorter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TaskSorterPanel extends JPanel {
	
	private TaskSorter sorter;
	private TaskSorterTable table;
	
	private JButton addButton;
	private JButton removeButton;
	private JCheckBox allowManualOrdering;
	
	public TaskSorterPanel(TaskSorter sorter) {
		this.sorter = sorter;
		
		this.initialize();
	}
	
	public TaskSorter getSorter() {
		return this.sorter;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.table = new TaskSorterTable(this.sorter);
		this.table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent event) {
						if (event.getValueIsAdjusting())
							return;
						
						if (TaskSorterPanel.this.table.getSelectedRow() == -1) {
							TaskSorterPanel.this.removeButton.setEnabled(false);
						} else {
							TaskSorterPanel.this.removeButton.setEnabled(true);
						}
					}
					
				});
		
		tablePanel.add(this.table.getTableHeader(), BorderLayout.NORTH);
		tablePanel.add(this.table, BorderLayout.CENTER);
		
		this.add(tablePanel, BorderLayout.CENTER);
		
		this.initializeButtons();
	}
	
	private void initializeButtons() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					TaskSorterElement element = new TaskSorterElement(
							TaskColumn.TITLE,
							SortOrder.ASCENDING);
					
					TaskSorterPanel.this.sorter.addElement(element);
				} else {
					if (TaskSorterPanel.this.table.getCellEditor() != null)
						TaskSorterPanel.this.table.getCellEditor().stopCellEditing();
					
					int[] selectedRows = TaskSorterPanel.this.table.getSelectedRows();
					
					List<TaskSorterElement> elements = new ArrayList<TaskSorterElement>();
					for (int selectedRow : selectedRows) {
						TaskSorterElement element = TaskSorterPanel.this.table.getTaskSorterElement(selectedRow);
						if (element != null)
							elements.add(element);
					}
					
					for (TaskSorterElement element : elements) {
						TaskSorterPanel.this.sorter.removeElement(element);
					}
				}
			}
			
		};
		
		this.addButton = new JButton(Images.getResourceImage("add.png", 16, 16));
		this.addButton.setActionCommand("ADD");
		this.addButton.addActionListener(listener);
		
		this.removeButton = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		
		this.allowManualOrdering = new JCheckBox(
				Translations.getString("searcheredit.allow_manual_ordering"));
		this.allowManualOrdering.setSelected(TaskUtils.isSortByOrder(this.sorter));
		this.allowManualOrdering.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskSorterPanel.this.allowManualOrdering.isSelected()) {
					if (TaskUtils.isSortByOrder(TaskSorterPanel.this.sorter))
						return;
					
					TaskSorterPanel.this.sorter.insertElement(
							new TaskSorterElement(
									TaskColumn.ORDER,
									SortOrder.ASCENDING),
							0);
				} else {
					if (!TaskUtils.isSortByOrder(TaskSorterPanel.this.sorter))
						return;
					
					TaskSorterPanel.this.sorter.removeElement(TaskSorterPanel.this.sorter.getElement(0));
				}
			}
			
		});
		
		this.sorter.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				TaskSorterPanel.this.allowManualOrdering.setSelected(TaskUtils.isSortByOrder(TaskSorterPanel.this.sorter));
			}
			
		});
		
		this.sorter.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				TaskSorterPanel.this.allowManualOrdering.setSelected(TaskUtils.isSortByOrder(TaskSorterPanel.this.sorter));
			}
			
		});
		
		JPanel buttonsPanel = new JPanel(new BorderLayout(3, 3));
		buttonsPanel.add(this.allowManualOrdering, BorderLayout.NORTH);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				this.addButton,
				this.removeButton);
		
		buttonsPanel.add(panel, BorderLayout.CENTER);
		
		this.add(buttonsPanel, BorderLayout.SOUTH);
	}
	
}
