package com.leclercb.taskunifier.gui.communicator.messages;

import java.io.Serializable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionAddQuickTask;

public class AddQuickTaskMessage implements Executor, Serializable {
	
	private String task;
	
	public AddQuickTaskMessage(String task) {
		this.setTask(task);
	}
	
	public String getTask() {
		return this.task;
	}
	
	public void setTask(String task) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		this.task = task;
	}
	
	@Override
	public void execute() {
		ActionAddQuickTask.addQuickTask(this.task, false);
	}
	
}
