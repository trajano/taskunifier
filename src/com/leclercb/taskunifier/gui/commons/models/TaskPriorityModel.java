package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.taskunifier.api.models.enums.TaskPriority;

public class TaskPriorityModel extends DefaultComboBoxModel {

	public TaskPriorityModel() {
		super(TaskPriority.values());
	}

}
