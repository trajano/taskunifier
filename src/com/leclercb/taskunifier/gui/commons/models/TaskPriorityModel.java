package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskPriorityModel extends DefaultComboBoxModel {
	
	public TaskPriorityModel(boolean firstNull) {
		super(
				ArrayUtils.concat(
						(firstNull ? new TaskPriority[] { null } : new TaskPriority[0]),
						TaskPriority.values()));
	}
	
}
