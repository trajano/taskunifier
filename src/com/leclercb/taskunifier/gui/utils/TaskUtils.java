package com.leclercb.taskunifier.gui.utils;

import java.util.List;

import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;

public final class TaskUtils {
	
	private TaskUtils() {

	}
	
	public static boolean showTask(Task task, TaskFilter filter) {
		if (!task.getModelStatus().equals(ModelStatus.LOADED)
				&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			return false;
		}
		
		// If a filtered parent task has non filtered children, it must be
		// displayed
		if (task.getParent() == null) {
			List<Task> tasks = TaskFactory.getInstance().getList();
			for (Task t : tasks) {
				if (task.equals(t.getParent()))
					if (filter.include(t))
						return true;
			}
		}
		
		return filter.include(task);
	}
	
}
