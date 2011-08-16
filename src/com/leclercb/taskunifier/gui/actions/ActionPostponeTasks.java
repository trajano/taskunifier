package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionPostponeTasks extends AbstractViewAction {
	
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
		super(
				title,
				Images.getResourceImage("calendar.png", width, height),
				ViewType.TASKS);
		
		this.putValue(SHORT_DESCRIPTION, title);
		
		this.field = field;
		this.amount = amount;
		
		this.setEnabled(this.shouldBeEnabled());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		postponeTasks(
				ViewType.getTaskView().getTaskTableView().getSelectedTasks(),
				this.field,
				this.amount);
	}
	
	public static void postponeTasks(Task[] tasks, int field, int amount) {
		for (Task task : tasks) {
			boolean fromCurrentDate = Main.SETTINGS.getBooleanProperty("task.postpone_from_current_date");
			Calendar newDueDate = task.getDueDate();
			
			if (newDueDate == null)
				continue;
			
			if (fromCurrentDate
					|| (field == Calendar.DAY_OF_MONTH && amount == 0)) {
				Calendar now = Calendar.getInstance();
				newDueDate.set(
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH));
			}
			
			newDueDate.add(field, amount);
			
			task.setDueDate(newDueDate);
		}
	}
	
	public static ActionPostponeTasks[] createDefaultActions(
			int width,
			int height) {
		List<ActionPostponeTasks> actions = new ArrayList<ActionPostponeTasks>();
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.today"),
				Calendar.DAY_OF_MONTH,
				0,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_day"),
				Calendar.DAY_OF_MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_days",
				2), Calendar.DAY_OF_MONTH, 2, width, height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_days",
				3), Calendar.DAY_OF_MONTH, 3, width, height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_week"),
				Calendar.WEEK_OF_YEAR,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_weeks",
				2), Calendar.WEEK_OF_YEAR, 2, width, height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_weeks",
				3), Calendar.WEEK_OF_YEAR, 3, width, height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_month"),
				Calendar.MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_months",
				2), Calendar.MONTH, 2, width, height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_months",
				3), Calendar.MONTH, 3, width, height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_year"),
				Calendar.YEAR,
				1,
				width,
				height));
		
		return actions.toArray(new ActionPostponeTasks[0]);
	}
	
}
