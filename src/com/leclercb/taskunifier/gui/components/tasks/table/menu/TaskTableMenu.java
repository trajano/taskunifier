package com.leclercb.taskunifier.gui.components.tasks.table.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskTableMenu extends JPopupMenu {
	
	private TaskTable taskTable;
	private Task taskToEdit;
	
	private JMenuItem itemEditTask;
	private JMenuItem itemDuplicateTasks;
	
	public TaskTableMenu(TaskTable taskTable) {
		super(Translations.getString("general.task"));
		
		CheckUtils.isNotNull(taskTable, "Task table cannot be null");
		
		this.taskTable = taskTable;
		this.initialize();
	}
	
	public Task getTaskToEdit() {
		return this.taskToEdit;
	}
	
	public void setTaskToEdit(Task taskToEdit) {
		this.taskToEdit = taskToEdit;
		this.itemEditTask.setEnabled(taskToEdit != null);
	}
	
	private void initialize() {
		this.itemEditTask = new JMenuItem(
				Translations.getString("action.name.edit_task"),
				Images.getResourceImage("edit.png", 16, 16));
		this.itemEditTask.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskTableMenu.this.getTaskToEdit() == null)
					return;
				
				TaskEditDialog dialog = new TaskEditDialog(
						TaskTableMenu.this.getTaskToEdit(),
						MainFrame.getInstance().getFrame(),
						true);
				dialog.setVisible(true);
			}
			
		});
		
		this.itemEditTask.setEnabled(false);
		
		this.add(this.itemEditTask);
		
		this.itemDuplicateTasks = new JMenuItem(
				Translations.getString("general.duplicate_tasks"),
				Images.getResourceImage("paste.png", 16, 16));
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
	
}
