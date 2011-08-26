package com.leclercb.taskunifier.gui.threads.communicator;

import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.communicator.messages.AddTaskMessage;

public class AddTaskMessageExecutor {
	
	public static void execute(AddTaskMessage message) {
		TaskFactory.getInstance().create(message.getTask());
	}
	
}
