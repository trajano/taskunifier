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
	}
	
	private void initialize() {
		JMenuItem item = null;
		
		item = new JMenuItem(
				Translations.getString("action.name.edit_task"),
				Images.getResourceImage("edit.png", 16, 16));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TaskEditDialog dialog = new TaskEditDialog(
						TaskTableMenu.this.getTaskToEdit(),
						MainFrame.getInstance().getFrame(),
						true);
				dialog.setVisible(true);
			}
			
		});
		
		this.add(item);
		
		this.addSeparator();
		
		item = new JMenuItem(
				Translations.getString("action.name.copy"),
				Images.getResourceImage("copy.png", 16, 16));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TaskTableMenu.this.taskTable.setSelectedTasks(new Task[] { TaskTableMenu.this.getTaskToEdit() });
				TransferHandler.getCopyAction().actionPerformed(
						new ActionEvent(
								TaskTableMenu.this.taskTable,
								ActionEvent.ACTION_PERFORMED,
								null));
			}
			
		});
		
		this.add(item);
		
		item = new JMenuItem(
				Translations.getString("action.name.paste"),
				Images.getResourceImage("paste.png", 16, 16));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TransferHandler.getPasteAction().actionPerformed(
						new ActionEvent(
								TaskTableMenu.this.taskTable,
								ActionEvent.ACTION_PERFORMED,
								null));
			}
			
		});
		
		this.add(item);
	}
	
}
