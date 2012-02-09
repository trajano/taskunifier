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
package com.leclercb.taskunifier.gui.components.tasktasks;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskGroup.TaskItem;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.tasktasks.table.TaskTasksTable;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TaskTasksPanel extends JPanel implements TaskTasksView, ModelSelectionListener {
	
	private boolean locked;
	
	private Task task;
	
	private TaskTasksTable table;
	private JToolBar toolBar;
	
	private JLabel label;
	
	private Action addAction;
	private Action removeAction;
	private Action lockedAction;
	
	public TaskTasksPanel() {
		this.initialize();
		
		this.task = null;
		this.setLocked(false);
	}
	
	public boolean isLocked() {
		return this.locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
		
		if (locked) {
			this.lockedAction.putValue(
					Action.SMALL_ICON,
					ImageUtils.getResourceImage("locked.png", 16, 16));
			this.lockedAction.putValue(
					Action.NAME,
					Translations.getString("general.locked", this.task));
			
			this.label.setText(Translations.getString(
					"general.locked",
					this.task.getTitle()));
		} else {
			this.lockedAction.putValue(
					Action.SMALL_ICON,
					ImageUtils.getResourceImage("unlocked.png", 16, 16));
			this.lockedAction.putValue(
					Action.NAME,
					Translations.getString("general.unlocked"));
			
			if (this.task == null)
				this.label.setText("");
			else
				this.label.setText(this.task.getTitle());
		}
	}
	
	private void initialize() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		
		this.label = new JLabel();
		this.label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		this.table = new TaskTasksTable();
		
		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setOpaque(false);
		this.toolBar.setFloatable(false);
		
		this.initializeActions();
		
		this.toolBar.add(this.addAction);
		this.toolBar.add(this.removeAction);
		this.toolBar.add(this.lockedAction);
		this.toolBar.add(Help.getHelpButton("task_tasks"));
		
		this.add(this.label, BorderLayout.NORTH);
		this.add(
				ComponentFactory.createJScrollPane(this.table, false),
				BorderLayout.CENTER);
		this.add(this.toolBar, BorderLayout.SOUTH);
		
		this.table.setTaskGroup(null);
		this.addAction.setEnabled(false);
		this.removeAction.setEnabled(false);
		this.lockedAction.setEnabled(false);
	}
	
	private void initializeActions() {
		this.addAction = new AbstractAction("", ImageUtils.getResourceImage(
				"add.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				TaskTasksPanel.this.table.getTaskGroup().add(
						new TaskItem(null, null));
			}
			
		};
		
		this.removeAction = new AbstractAction("", ImageUtils.getResourceImage(
				"remove.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				TaskItem[] items = TaskTasksPanel.this.table.getSelectedTaskItems();
				
				for (TaskItem item : items)
					TaskTasksPanel.this.table.getTaskGroup().remove(item);
			}
			
		};
		
		this.lockedAction = new AbstractAction(
				Translations.getString("general.unlocked"),
				ImageUtils.getResourceImage("unlocked.png", 16, 16)) {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				TaskTasksPanel.this.setLocked(!TaskTasksPanel.this.isLocked());
			}
			
		};
	}
	
	@Override
	public void modelSelectionChange(ModelSelectionChangeEvent event) {
		if (this.isLocked())
			return;
		
		Model[] models = event.getSelectedModels();
		
		if (models.length != 1 || !(models[0] instanceof Task)) {
			this.task = null;
			this.setLocked(false);
			this.table.setTaskGroup(null);
			this.addAction.setEnabled(false);
			this.removeAction.setEnabled(false);
			this.lockedAction.setEnabled(false);
			return;
		}
		
		Task task = (Task) models[0];
		
		this.task = task;
		this.setLocked(false);
		this.table.setTaskGroup(task.getTasks());
		this.addAction.setEnabled(true);
		this.removeAction.setEnabled(true);
		this.lockedAction.setEnabled(true);
	}
	
}
