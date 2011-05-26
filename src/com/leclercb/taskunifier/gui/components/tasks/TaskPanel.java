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
package com.leclercb.taskunifier.gui.components.tasks;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JPanel;
import javax.swing.JTable.PrintMode;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TaskPanel extends JPanel implements TaskView, TaskSearcherSelectionListener {
	
	private ModelSelectionChangeSupport modelSelectionChangeSupport;
	
	private TaskTable taskTable;
	
	public TaskPanel() {
		this.modelSelectionChangeSupport = new ModelSelectionChangeSupport(this);
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.taskTable = new TaskTable();
		this.taskTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						TaskPanel.this.modelSelectionChangeSupport.fireModelSelectionChange(TaskPanel.this.getSelectedTasks());
					}
					
				});
		
		this.add(
				ComponentFactory.createJScrollPane(this.taskTable, false),
				BorderLayout.CENTER);
	}
	
	@Override
	public Task[] getSelectedTasks() {
		return this.taskTable.getSelectedTasks();
	}
	
	@Override
	public void setSelectedTaskAndStartEdit(Task task) {
		this.taskTable.setSelectedTaskAndStartEdit(task);
	}
	
	@Override
	public void setSelectedTasks(Task[] tasks) {
		this.taskTable.setSelectedTasks(tasks);
	}
	
	@Override
	public void refreshTasks() {
		this.taskTable.refreshTasks();
	}
	
	@Override
	public void printTasks() throws HeadlessException, PrinterException {
		this.taskTable.print(
				PrintMode.FIT_WIDTH,
				new MessageFormat(Constants.TITLE
						+ " - "
						+ this.taskTable.getTaskSearcher().getTitle()),
						new MessageFormat(this.taskTable.getRowCount()
								+ " tasks | Page - {0}"),
								true,
								null,
								true);
	}
	
	@Override
	public void addModelSelectionChangeListener(ModelSelectionListener listener) {
		this.modelSelectionChangeSupport.addModelSelectionChangeListener(listener);
	}
	
	@Override
	public void removeModelSelectionChangeListener(
			ModelSelectionListener listener) {
		this.modelSelectionChangeSupport.removeModelSelectionChangeListener(listener);
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		if (event.getSelectedTaskSearcher() != null)
			this.taskTable.setTaskSearcher(event.getSelectedTaskSearcher());
	}
	
}
