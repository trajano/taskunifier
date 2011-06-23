package com.leclercb.taskunifier.gui.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.leclercb.commons.api.utils.IgnoreCaseString;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;

public final class TagList {
	
	private Set<IgnoreCaseString> tags;
	
	private TagList() {
		this.tags = new HashSet<IgnoreCaseString>();
		this.initialize();
	}
	
	private void initialize() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			if (!task.getModelStatus().equals(ModelStatus.LOADED)
					&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				continue;
			}
			
			this.tags.addAll(Arrays.asList(IgnoreCaseString.as(task.getTags())));
		}
	}
	
}
