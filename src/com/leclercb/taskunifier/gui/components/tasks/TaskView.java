package com.leclercb.taskunifier.gui.components.tasks;

import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.util.List;

import com.leclercb.taskunifier.api.models.Task;

public interface TaskView {
	
	public abstract void showColumn(TaskColumn taskColumn, boolean show);
	
	public abstract List<Task> getSelectedTasks();
	
	public abstract void setSelectedTasks(List<Task> tasks);
	
	public abstract void refreshTasks();
	
	public abstract void printTasks() throws HeadlessException,
			PrinterException;
	
	public abstract int getTaskCount();
	
	public abstract int getDisplayedTaskCount();
	
}
