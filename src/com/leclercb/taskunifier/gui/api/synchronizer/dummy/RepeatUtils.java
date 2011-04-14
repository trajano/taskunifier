package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;

public class RepeatUtils {
	
	public static boolean isValidRepeatValue(String repeat) {
		if (repeat == null || repeat.length() == 0)
			return false;
		
		String regex = null;
		
		repeat = repeat.toLowerCase();
		
		if (repeat.equals("with parent"))
			return true;
		
		regex = "^(daily|weekly|biweekly|monthly|bimonthly|quarterly|semiannually|yearly)$".toLowerCase();
		if (repeat.matches(regex))
			return true;
		
		regex = "^(every [0-9]+ (day|days|week|weeks|month|months|year|years))$".toLowerCase();
		if (repeat.matches(regex))
			return true;
		
		regex = "^((on )?the ([1-4]|1st|first|2nd|second|3rd|third|4th|fourth|last) (mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday) of each month)$".toLowerCase();
		if (repeat.matches(regex))
			return true;
		
		regex = "^(every (mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|weekday))$".toLowerCase();
		if (repeat.matches(regex))
			return true;
		
		return false;
	}
	
	public static void createRepeatTask(Task task) {
		createRepeatTask(task, task.getRepeat());
	}
	
	public static Task createRepeatTask(Task task, String repeat) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		
		Calendar startDate = getNextRepeatDate(repeat, task.getStartDate());
		
		Calendar dueDate = null;
		
		switch (task.getRepeatFrom()) {
			case COMPLETION_DATE:
				dueDate = task.getCompletedOn();
				break;
			case DUE_DATE:
				dueDate = task.getDueDate();
				break;
		}
		
		dueDate = getNextRepeatDate(
				repeat,
				(dueDate == null ? task.getCompletedOn() : dueDate));
		
		if (startDate == null && dueDate == null)
			return null;
		
		Task newTask = TaskFactory.getInstance().create(task);
		newTask.setStartDate(startDate);
		newTask.setDueDate(dueDate);
		newTask.setRepeat(task.getRepeat());
		
		List<Task> tasks = TaskFactory.getInstance().getChildren(task);
		for (Task subtask : tasks) {
			if ("With Parent".equalsIgnoreCase(subtask.getRepeat())) {
				if (!subtask.isCompleted())
					subtask.setCompleted(true);
				
				Task newSubtask = createRepeatTask(subtask, newTask.getRepeat());
				newSubtask.setCompleted(false);
				newSubtask.setParent(newTask);
				continue;
			}
			
			if (!subtask.isCompleted()
					&& isValidRepeatValue(subtask.getRepeat())) {
				subtask.setParent(newTask);
				continue;
			}
			
			if (!subtask.isCompleted()) {
				subtask.setCompleted(true);
				continue;
			}
		}
		
		task.setRepeat(null);
		
		return newTask;
	}
	
	private static Calendar getNextRepeatDate(String repeat, Calendar date) {
		if (repeat == null || repeat.length() == 0 || date == null)
			return null;
		
		String regex = null;
		
		repeat = repeat.toLowerCase();
		date = DateUtils.cloneCalendar(date);
		
		if (repeat.equals("with parent"))
			return getNextDueDate1(repeat, date);
		
		regex = "^(daily|weekly|biweekly|monthly|bimonthly|quarterly|semiannually|yearly)$".toLowerCase();
		if (repeat.matches(regex))
			return getNextDueDate2(repeat, date);
		
		regex = "^(every ([0-9]+) (day|days|week|weeks|month|months|year|years))$".toLowerCase();
		if (repeat.matches(regex))
			return getNextDueDate3(repeat, date);
		
		regex = "^((on )?the ([1-4]|1st|first|2nd|second|3rd|third|4th|fourth|last) (mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday) of each month)$".toLowerCase();
		if (repeat.matches(regex))
			return getNextDueDate4(repeat, date);
		
		regex = "^(every (mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|weekday))$".toLowerCase();
		if (repeat.matches(regex))
			return getNextDueDate5(repeat, date);
		
		return null;
	}
	
	private static Calendar getNextDueDate1(String repeat, Calendar date) {
		return null;
	}
	
	private static Calendar getNextDueDate2(String repeat, Calendar date) {
		if (repeat.equals("daily"))
			date.add(Calendar.DAY_OF_MONTH, 1);
		else if (repeat.equals("weekly"))
			date.add(Calendar.WEEK_OF_YEAR, 1);
		else if (repeat.equals("biweekly"))
			date.add(Calendar.WEEK_OF_YEAR, 2);
		else if (repeat.equals("monthly"))
			date.add(Calendar.MONTH, 1);
		else if (repeat.equals("bimonthly"))
			date.add(Calendar.MONTH, 2);
		else if (repeat.equals("quarterly"))
			date.add(Calendar.MONTH, 3);
		else if (repeat.equals("semiannually"))
			date.add(Calendar.MONTH, 6);
		else if (repeat.equals("yearly"))
			date.add(Calendar.YEAR, 1);
		
		return date;
	}
	
	private static Calendar getNextDueDate3(String repeat, Calendar date) {
		String regex = "^(every ([0-9]+) (day|days|week|weeks|month|months|year|years))$".toLowerCase();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(repeat);
		
		if (!matcher.find())
			return null;
		
		int amount = Integer.parseInt(matcher.group(2));
		String strField = matcher.group(3);
		int field = Calendar.DAY_OF_MONTH;
		
		if (strField.startsWith("day"))
			field = Calendar.DAY_OF_MONTH;
		else if (strField.startsWith("week"))
			field = Calendar.WEEK_OF_YEAR;
		else if (strField.startsWith("month"))
			field = Calendar.MONTH;
		else if (strField.startsWith("year"))
			field = Calendar.YEAR;
		
		date.add(field, amount);
		
		return date;
	}
	
	private static Calendar getNextDueDate4(String repeat, Calendar date) {
		String regex = "^((on )?the ([1-4]|1st|first|2nd|second|3rd|third|4th|fourth|last) (mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday) of each month)$".toLowerCase();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(repeat);
		
		if (!matcher.find())
			return null;
		
		String strAmount = matcher.group(3);
		Integer amount = null;
		String strField = matcher.group(4);
		Integer field = null;
		
		if (strAmount.matches("^(1|1st|first)$")) {
			amount = 1;
		} else if (strAmount.matches("^(2|2nd|second)$")) {
			amount = 2;
		} else if (strAmount.matches("^(3|3rd|third)$")) {
			amount = 3;
		} else if (strAmount.matches("^(4|4th|fourth)$")) {
			amount = 4;
		} else if (strAmount.equals("last")) {
			amount = 5;
		}
		
		if (strField.startsWith("mon")) {
			field = Calendar.MONDAY;
		} else if (strField.startsWith("tue")) {
			field = Calendar.TUESDAY;
		} else if (strField.startsWith("wed")) {
			field = Calendar.WEDNESDAY;
		} else if (strField.startsWith("thu")) {
			field = Calendar.THURSDAY;
		} else if (strField.startsWith("fri")) {
			field = Calendar.FRIDAY;
		} else if (strField.startsWith("sat")) {
			field = Calendar.SATURDAY;
		} else if (strField.startsWith("sun")) {
			field = Calendar.SUNDAY;
		}
		
		if (amount == null || field == null)
			return null;
		
		Calendar clone = DateUtils.cloneCalendar(date);
		
		date.set(Calendar.DAY_OF_WEEK, field);
		
		int realAmount = amount;
		if (amount > date.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH))
			realAmount = date.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH);
		
		date.set(Calendar.DAY_OF_WEEK_IN_MONTH, realAmount);
		
		if (date.compareTo(clone) <= 0) {
			date.add(Calendar.MONTH, 1);
			
			date.set(Calendar.DAY_OF_WEEK, field);
			
			realAmount = amount;
			if (amount > date.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH))
				realAmount = date.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH);
			
			date.set(Calendar.DAY_OF_WEEK_IN_MONTH, realAmount);
		}
		
		return date;
	}
	
	private static Calendar getNextDueDate5(String repeat, Calendar date) {
		String regex = "^(every (mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|weekday))$".toLowerCase();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(repeat);
		
		if (!matcher.find())
			return null;
		
		String strField = matcher.group(2);
		Integer field = null;
		
		if (strField.startsWith("mon")) {
			field = Calendar.MONDAY;
		} else if (strField.startsWith("tue")) {
			field = Calendar.TUESDAY;
		} else if (strField.startsWith("wed")) {
			field = Calendar.WEDNESDAY;
		} else if (strField.startsWith("thu")) {
			field = Calendar.THURSDAY;
		} else if (strField.startsWith("fri")) {
			field = Calendar.FRIDAY;
		} else if (strField.startsWith("sat")) {
			field = Calendar.SATURDAY;
		} else if (strField.startsWith("sun")) {
			field = Calendar.SUNDAY;
		} else if (strField.equals("weekend")) {
			int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
				case Calendar.SATURDAY:
					field = Calendar.SUNDAY;
					break;
				default:
					field = Calendar.SATURDAY;
					break;
			}
		} else if (strField.equals("weekday")) {
			int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
				case Calendar.FRIDAY:
				case Calendar.SATURDAY:
				case Calendar.SUNDAY:
					field = Calendar.MONDAY;
					break;
				default:
					field = dayOfWeek + 1;
					break;
			}
		}
		
		if (field == null)
			return null;
		
		Calendar clone = DateUtils.cloneCalendar(date);
		date.set(Calendar.DAY_OF_WEEK, field);
		if (date.compareTo(clone) <= 0)
			date.add(Calendar.WEEK_OF_YEAR, 1);
		
		return date;
	}
	
}
