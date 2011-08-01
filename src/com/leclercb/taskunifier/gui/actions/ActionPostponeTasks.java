package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionPostponeTasks extends AbstractAction {
	
	private int field;
	private int amount;
	
	public ActionPostponeTasks(String title, int field, int amount) {
		this(title, field, amount, 32, 32);
	}
	
	public ActionPostponeTasks(
			String title,
			int field,
			int amount,
			int width,
			int height) {
		super(title, Images.getResourceImage("calendar.png", width, height));
		
		this.putValue(SHORT_DESCRIPTION, title);
		
		this.field = field;
		this.amount = amount;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		postponeTasks(
				MainFrame.getInstance().getTaskView().getSelectedTasks(),
				this.field,
				this.amount);
	}
	
	public static void postponeTasks(Task[] tasks, int field, int amount) {
		for (Task task : tasks) {
			Calendar dueDate = task.getDueDate();
			
			if (dueDate == null)
				continue;
			
			dueDate.add(field, amount);
			task.setDueDate(dueDate);
		}
	}
	
}
