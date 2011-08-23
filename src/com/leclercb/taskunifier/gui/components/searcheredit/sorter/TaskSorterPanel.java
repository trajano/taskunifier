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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskSorterPanel extends JPanel {
	
	private TaskSorter sorter;
	private TaskSorterTable table;
	
	private JButton addButton;
	private JButton removeButton;
	
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
					TaskSorterElement element = new TaskSorterElement(
							TaskColumn.TITLE,
							SortOrder.ASCENDING);
					
					TaskSorterPanel.this.sorter.addElement(element);
				} else {
					if (TaskSorterPanel.this.table.getCellEditor() != null)
						TaskSorterPanel.this.table.getCellEditor().stopCellEditing();
					
					int[] selectedRows = TaskSorterPanel.this.table.getSelectedRows();
					
					for (int selectedRow : selectedRows) {
						TaskSorterElement element = TaskSorterPanel.this.table.getTaskSorterElement(selectedRow);
						if (element != null)
							TaskSorterPanel.this.sorter.removeElement(element);
					}
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
	}
	
}
