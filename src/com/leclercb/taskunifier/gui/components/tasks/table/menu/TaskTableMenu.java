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
package com.leclercb.taskunifier.gui.components.tasks.table.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionCollapseAll;
import com.leclercb.taskunifier.gui.actions.ActionEditTask;
import com.leclercb.taskunifier.gui.actions.ActionExpandAll;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskTableMenu extends JPopupMenu {
	
	private TaskTable taskTable;
	private Task focussedTask;
	
	private JMenuItem itemAddSubTask;
	private JMenuItem itemEditTask;
	private JMenuItem itemDuplicateTasks;
	private JMenuItem itemSortTasks;
	
	public TaskTableMenu(TaskTable taskTable) {
		super(Translations.getString("general.task"));
		
		CheckUtils.isNotNull(taskTable, "Task table cannot be null");
		
		this.taskTable = taskTable;
		this.initialize();
	}
	
	public Task getFocussedTask() {
		return this.focussedTask;
	}
	
	public void setFocussedTask(Task focussedTask) {
		this.focussedTask = focussedTask;
		this.itemAddSubTask.setEnabled(focussedTask != null
				&& focussedTask.getParent() == null);
		this.itemEditTask.setEnabled(focussedTask != null);
	}
	
	private void initialize() {
		this.initializeItemAddSubTask();
		this.initializeItemEditTask();
		this.initializeItemDuplicateTasks();
		this.addSeparator();
		this.initializeItemSortTasks();
		this.addSeparator();
		this.initializeCollapseExpandAll();
	}
	
	private void initializeItemAddSubTask() {
		this.itemAddSubTask = new JMenuItem(
				Translations.getString("action.name.add_subtask"),
				Images.getResourceImage("document.png", 16, 16));
		
		this.itemAddSubTask.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskTableMenu.this.getFocussedTask() == null)
					return;
				
				if (TaskTableMenu.this.getFocussedTask().getParent() != null)
					return;
				
				ActionAddSubTask.addSubTask(
						TemplateFactory.getInstance().getDefaultTemplate(),
						TaskTableMenu.this.getFocussedTask());
			}
			
		});
		
		this.itemAddSubTask.setEnabled(false);
		
		this.add(this.itemAddSubTask);
	}
	
	private void initializeItemEditTask() {
		this.itemEditTask = new JMenuItem(
				Translations.getString("action.name.edit_task"),
				Images.getResourceImage("edit.png", 16, 16));
		
		this.itemEditTask.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskTableMenu.this.getFocussedTask() == null)
					return;
				
				ActionEditTask.editTask(
						TaskTableMenu.this.getFocussedTask(),
						false);
			}
			
		});
		
		this.itemEditTask.setEnabled(false);
		
		this.add(this.itemEditTask);
	}
	
	private void initializeItemDuplicateTasks() {
		this.itemDuplicateTasks = new JMenuItem(
				Translations.getString("general.duplicate_tasks"),
				Images.getResourceImage("duplicate.png", 16, 16));
		
		this.itemDuplicateTasks.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TransferHandler.getCopyAction().actionPerformed(
						new ActionEvent(
								TaskTableMenu.this.taskTable,
								ActionEvent.ACTION_PERFORMED,
								null));
				
				TransferHandler.getPasteAction().actionPerformed(
						new ActionEvent(
								TaskTableMenu.this.taskTable,
								ActionEvent.ACTION_PERFORMED,
								null));
			}
			
		});
		
		this.add(this.itemDuplicateTasks);
	}
	
	private void initializeItemSortTasks() {
		this.itemSortTasks = new JMenuItem(
				Translations.getString("general.sort"),
				Images.getResourceImage("synchronize.png", 16, 16));
		
		this.itemSortTasks.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				MainFrame.getInstance().getTaskView().refreshTasks();
			}
			
		});
		
		this.add(this.itemSortTasks);
	}
	
	private void initializeCollapseExpandAll() {
		this.add(new JMenuItem(new ActionCollapseAll()));
		this.add(new JMenuItem(new ActionExpandAll()));
	}
	
}
