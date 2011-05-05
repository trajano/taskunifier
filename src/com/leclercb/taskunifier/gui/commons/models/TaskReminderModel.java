package com.leclercb.taskunifier.gui.commons.models;

import javax.swing.DefaultComboBoxModel;

public class TaskReminderModel extends DefaultComboBoxModel {

	public TaskReminderModel() {
		super(new Integer[] {
				0,
				5,
				15,
				30,
				60,
				60 * 24,
				60 * 24 * 7 });
	}

}
