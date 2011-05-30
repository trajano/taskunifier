package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskReminderModel extends DefaultComboBoxModel {
	
	public TaskReminderModel() {
		super(new Integer[] { 0, 5, 15, 30, 60, 60 * 24, 60 * 24 * 7 });
	}
	
}
