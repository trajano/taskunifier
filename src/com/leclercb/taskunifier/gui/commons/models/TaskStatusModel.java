package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.taskunifier.api.models.enums.TaskStatus;

public class TaskStatusModel extends DefaultComboBoxModel {

	public TaskStatusModel() {
		super(TaskStatus.values());
	}

}
