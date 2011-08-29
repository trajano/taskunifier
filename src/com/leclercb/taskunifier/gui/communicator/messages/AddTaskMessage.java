package com.leclercb.taskunifier.gui.communicator.messages;

import java.io.Serializable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.beans.TaskBean;

public class AddTaskMessage implements Executor, Serializable {
	
	private TaskBean task;
	
	public AddTaskMessage(TaskBean task) {
		this.setTask(task);
	}
	
	public TaskBean getTask() {
		return this.task;
	}
	
	public void setTask(TaskBean task) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		this.task = task;
	}
	
	@Override
	public void execute() {
		TaskFactory.getInstance().create(this.task);
	}
	
}
