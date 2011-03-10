package com.leclercb.taskunifier.gui.utils;

import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;

public final class TaskUtils {
	
	private TaskUtils() {

	}
	
	public static int getImportance(Task task) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		
		int importance = 2;
		
		switch (task.getPriority()) {
			case NEGATIVE:
				importance += -1;
				break;
			case LOW:
				importance += 0;
				break;
			case MEDIUM:
				importance += 1;
				break;
			case HIGH:
				importance += 2;
				break;
			case TOP:
				importance += 3;
				break;
		}
		
		importance += (task.isStar() ? 1 : 0);
		
		if (task.getDueDate() != null) {
			long milliSeconds1 = task.getDueDate().getTimeInMillis();
			long milliSeconds2 = Calendar.getInstance().getTimeInMillis();
			long diff = milliSeconds1 - milliSeconds2;
			double diffDays = diff / (24 * 60 * 60 * 1000.0);
			
			if (diffDays > 14)
				importance += 0;
			else if (diffDays >= 7)
				importance += 1;
			else if (diffDays >= 2)
				importance += 2;
			else if (diffDays == 1)
				importance += 3;
			else if (diffDays == 0)
				importance += 5;
			else
				importance += 6;
		}
		
		return importance;
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
