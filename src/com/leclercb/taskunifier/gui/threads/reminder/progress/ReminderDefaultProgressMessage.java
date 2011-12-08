package com.leclercb.taskunifier.gui.threads.reminder.progress;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;

public class ReminderDefaultProgressMessage implements ReminderProgressMessage {
	
	private Task task;
	
	public ReminderDefaultProgressMessage(Task task) {
		CheckUtils.isNotNull(task);
		this.task = task;
	}
	
	public Task getTask() {
		return this.task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
}
