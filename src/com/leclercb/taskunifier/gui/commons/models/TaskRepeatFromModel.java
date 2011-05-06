package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;

public class TaskRepeatFromModel extends DefaultComboBoxModel {

	public TaskRepeatFromModel(boolean firstNull) {
		super(ArrayUtils.concat(
				(firstNull? new TaskRepeatFrom[] { null } : new TaskRepeatFrom[0]), 
				TaskRepeatFrom.values()));
	}

}
