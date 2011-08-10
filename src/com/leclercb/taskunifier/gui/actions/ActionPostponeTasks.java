package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionPostponeTasks extends AbstractViewAction {
	
	private TaskTableView taskTableView;
	
	private int field;
	private int amount;
	
	public ActionPostponeTasks(
			TaskTableView taskTableView,
			String title,
			int field,
			int amount) {
		this(taskTableView, title, field, amount, 32, 32);
	}
	
	public ActionPostponeTasks(
			TaskTableView taskTableView,
			String title,
			int field,
			int amount,
			int width,
			int height) {
		super(
				title,
				Images.getResourceImage("calendar.png", width, height),
				ViewType.TASKS);
		
		CheckUtils.isNotNull(taskTableView, "Task table view cannot be null");
		this.taskTableView = taskTableView;
		
		this.putValue(SHORT_DESCRIPTION, title);
		
		this.field = field;
		this.amount = amount;
		
		this.setEnabled(this.shouldBeEnabled());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		postponeTasks(
				this.taskTableView.getSelectedTasks(),
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
	
	public static ActionPostponeTasks[] createDefaultActions(
			TaskTableView taskTableView,
			int width,
			int height) {
		List<ActionPostponeTasks> actions = new ArrayList<ActionPostponeTasks>();
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.1_day"),
				Calendar.DAY_OF_MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.x_days", 2),
				Calendar.DAY_OF_MONTH,
				2,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.x_days", 3),
				Calendar.DAY_OF_MONTH,
				3,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.1_week"),
				Calendar.WEEK_OF_YEAR,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.x_weeks", 2),
				Calendar.WEEK_OF_YEAR,
				2,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.x_weeks", 3),
				Calendar.WEEK_OF_YEAR,
				3,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.1_month"),
				Calendar.MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.x_months", 2),
				Calendar.MONTH,
				2,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.x_months", 3),
				Calendar.MONTH,
				3,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				taskTableView,
				Translations.getString("postpone.1_year"),
				Calendar.YEAR,
				1,
				width,
				height));
		
		return actions.toArray(new ActionPostponeTasks[0]);
	}
	
}
