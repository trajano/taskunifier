package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskStatusModel extends DefaultComboBoxModel {
	
	public TaskStatusModel(boolean firstNull) {
		super(ArrayUtils.concat(
				(firstNull ? new TaskStatus[] { null } : new TaskStatus[0]),
				TaskStatus.values()));
	}
	
}
