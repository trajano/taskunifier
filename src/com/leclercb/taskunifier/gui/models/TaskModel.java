/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.models;

import java.util.List;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;

public class TaskModel extends AbstractModelSortedModel {
	
	private Task hiddenTask;
	
	public TaskModel(boolean firstNull) {
		this(firstNull, null);
	}
	
	public TaskModel(boolean firstNull, Task hiddenTask) {
		this.hiddenTask = hiddenTask;
		this.initialize(firstNull);
	}
	
	@Override
	public void addElement(Object element) {
		if (this.hiddenTask != null
				&& EqualsUtils.equals(this.hiddenTask, element))
			return;
		
		super.addElement(element);
	}
	
	private void initialize(boolean firstNull) {
		if (firstNull)
			this.addElement(null);
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks)
			if (task.getModelStatus().equals(ModelStatus.LOADED)
					|| task.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.addElement(task);
		
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
	}
	
}
