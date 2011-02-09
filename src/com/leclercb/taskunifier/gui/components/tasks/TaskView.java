package com.leclercb.taskunifier.gui.components.tasks;

import java.awt.HeadlessException;
import java.awt.print.PrinterException;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.events.TaskSelectionChangeSupported;

public interface TaskView extends TaskSelectionChangeSupported, TaskSearcherSelectionListener {
	
	public abstract Task[] getSelectedTasks();
	
	public abstract void setSelectedTasks(Task[] tasks);
	
	public abstract void refreshTasks();
	
	public abstract void printTasks() throws HeadlessException,
			PrinterException;
	
}
