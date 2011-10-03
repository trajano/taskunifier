package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionPostponeTasks extends AbstractViewAction {
	
	private PostponeType type;
	private int field;
	private int amount;
	
	public ActionPostponeTasks(
			String title,
			PostponeType type,
			int field,
			int amount) {
		this(title, type, field, amount, 32, 32);
	}
	
	public ActionPostponeTasks(
			String title,
			PostponeType type,
			int field,
			int amount,
			int width,
			int height) {
		super(
				title,
				Images.getResourceImage("calendar.png", width, height),
				ViewType.TASKS,
				ViewType.CALENDAR);
		
		this.putValue(SHORT_DESCRIPTION, title);
		
		CheckUtils.isNotNull(type, "Postpone type cannot be null");
		
		this.type = type;
		this.field = field;
		this.amount = amount;
		
		this.setEnabled(this.shouldBeEnabled());
	}
	
	public PostponeType getType() {
		return this.type;
	}
	
	public int getField() {
		return this.field;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		postponeTasks(
				ViewType.getSelectedTasks(),
				this.type,
				this.field,
				this.amount);
	}
	
	public static void postponeTasks(
			Task[] tasks,
			PostponeType type,
			int field,
			int amount) {
		CheckUtils.isNotNull(type, "Postpone type cannot be null");
		
		if (tasks == null)
			return;
		
		boolean fromCurrentDate = Main.SETTINGS.getBooleanProperty("task.postpone_from_current_date");
		
		if (type == PostponeType.BOTH)
			fromCurrentDate = false;
		
		if (type == PostponeType.START_DATE || type == PostponeType.BOTH) {
			for (Task task : tasks) {
				Calendar newStartDate = task.getStartDate();
				
				if (newStartDate == null)
					newStartDate = Calendar.getInstance();
				
				if (fromCurrentDate
						|| (field == Calendar.DAY_OF_MONTH && amount == 0)) {
					Calendar now = Calendar.getInstance();
					newStartDate.set(
							now.get(Calendar.YEAR),
							now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH));
				}
				
				newStartDate.add(field, amount);
				
				task.setStartDate(newStartDate);
			}
		}
		
		if (type == PostponeType.DUE_DATE || type == PostponeType.BOTH) {
			for (Task task : tasks) {
				Calendar newDueDate = task.getDueDate();
				
				if (newDueDate == null)
					newDueDate = Calendar.getInstance();
				
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
		
		ViewType.refreshTasks();
	}
	
	public static ActionPostponeTasks[] createDefaultActions(
			PostponeType type,
			int width,
			int height) {
		List<ActionPostponeTasks> actions = new ArrayList<ActionPostponeTasks>();
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.today"),
				type,
				Calendar.DAY_OF_MONTH,
				0,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_day"),
				type,
				Calendar.DAY_OF_MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_days",
				2), type, Calendar.DAY_OF_MONTH, 2, width, height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_days",
				3), type, Calendar.DAY_OF_MONTH, 3, width, height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_week"),
				type,
				Calendar.WEEK_OF_YEAR,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_weeks",
				2), type, Calendar.WEEK_OF_YEAR, 2, width, height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_weeks",
				3), type, Calendar.WEEK_OF_YEAR, 3, width, height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_month"),
				type,
				Calendar.MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_months",
				2), type, Calendar.MONTH, 2, width, height));
		
		actions.add(new ActionPostponeTasks(Translations.getString(
				"postpone.x_months",
				3), type, Calendar.MONTH, 3, width, height));
		
		actions.add(new ActionPostponeTasks(
				Translations.getString("postpone.1_year"),
				type,
				Calendar.YEAR,
				1,
				width,
				height));
		
		return actions.toArray(new ActionPostponeTasks[0]);
	}
	
}
