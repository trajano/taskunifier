package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;

public class TaskRepeatFromModel extends DefaultComboBoxModel {

	public TaskRepeatFromModel() {
		super(TaskRepeatFrom.values());
	}

}
