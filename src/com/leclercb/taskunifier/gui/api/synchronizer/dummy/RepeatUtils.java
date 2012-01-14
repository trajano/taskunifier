/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;

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
		
		String daysRegex = "(mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|weekday)";
		regex = "^(every ("
				+ daysRegex
				+ "(, ?"
				+ daysRegex
				+ ")*))$".toLowerCase();
		if (repeat.matches(regex))
			return true;
		
		return false;
	}
	
	public static void createRepeatTask(Task task) {
		createRepeatTask(task, task.getRepeat());
	}
	
	public static Task createRepeatTask(Task task, String repeat) {
		CheckUtils.isNotNull(task);
		
		Calendar startDate = null;
		Calendar dueDate = null;
		
		if (task.getRepeatFrom() == TaskRepeatFrom.COMPLETION_DATE) {
			if (task.getDueDate() != null) {
				dueDate = DateUtils.cloneCalendar(task.getDueDate());
				Calendar completedOn = DateUtils.cloneCalendar(task.getCompletedOn());
				completedOn.set(
						completedOn.get(Calendar.YEAR),
						completedOn.get(Calendar.MONTH),
						completedOn.get(Calendar.DAY_OF_MONTH),
						dueDate.get(Calendar.HOUR_OF_DAY),
						dueDate.get(Calendar.MINUTE),
						dueDate.get(Calendar.SECOND));
				
				dueDate = getNextRepeatDate(repeat, completedOn);
			}
			
			if (task.getStartDate() != null && task.getDueDate() == null) {
				startDate = DateUtils.cloneCalendar(task.getStartDate());
				Calendar completedOn = DateUtils.cloneCalendar(task.getCompletedOn());
				completedOn.set(
						completedOn.get(Calendar.YEAR),
						completedOn.get(Calendar.MONTH),
						completedOn.get(Calendar.DAY_OF_MONTH),
						startDate.get(Calendar.HOUR_OF_DAY),
						startDate.get(Calendar.MINUTE),
						startDate.get(Calendar.SECOND));
				
				startDate = getNextRepeatDate(repeat, completedOn);
			}
			
			if (task.getStartDate() != null && task.getDueDate() != null) {
				startDate = Calendar.getInstance();
				startDate.setTimeInMillis(dueDate.getTimeInMillis()
						- (task.getDueDate().getTimeInMillis() - task.getStartDate().getTimeInMillis()));
			}
			
			if (task.getStartDate() == null && task.getDueDate() == null) {
				Calendar completedOn = DateUtils.cloneCalendar(task.getCompletedOn());
				dueDate = getNextRepeatDate(repeat, completedOn);
			}
		}
		
		if (task.getRepeatFrom() == TaskRepeatFrom.DUE_DATE) {
			if (task.getStartDate() != null)
				startDate = getNextRepeatDate(repeat, task.getStartDate());
			
			if (task.getDueDate() != null)
				dueDate = getNextRepeatDate(repeat, task.getDueDate());
		}
		
		if (startDate == null && dueDate == null)
			return null;
		
		Task newTask = TaskFactory.getInstance().create(task);
		newTask.setCompleted(false);
		newTask.setStartDate(startDate);
		newTask.setDueDate(dueDate);
		newTask.setRepeat(task.getRepeat());
		
		Task[] tasks = task.getChildren();
		for (Task subtask : tasks) {
			if ("With Parent".equalsIgnoreCase(subtask.getRepeat())) {
				if (!subtask.isCompleted())
					subtask.setCompleted(true);
				
				String newRepeat = newTask.getRepeat();
				if ("With Parent".equalsIgnoreCase(newRepeat))
					newRepeat = repeat;
				
				Task newSubtask = createRepeatTask(subtask, newRepeat);
				
				if (newSubtask != null) {
					newSubtask.setCompleted(false);
					newSubtask.setParent(newTask);
				} else {
					subtask.setCompleted(false);
					subtask.setParent(newTask);
				}
				
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
		
		String daysRegex = "(mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|weekday)";
		regex = "^(every ("
				+ daysRegex
				+ "(, ?"
				+ daysRegex
				+ ")*))$".toLowerCase();
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
		String daysRegex = "(mon|tue|wed|thu|fri|sat|sun|monday|tuesday|wednesday|thursday|friday|saturday|sunday|weekend|weekday)";
		String regex = "^(every ("
				+ daysRegex
				+ "(, ?"
				+ daysRegex
				+ ")*))$".toLowerCase();
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(repeat);
		
		if (!matcher.find())
			return null;
		
		String[] strFields = matcher.group(2).split(",");
		Set<Integer> fields = new HashSet<Integer>();
		
		for (String strField : strFields) {
			strField = strField.trim();
			
			if (strField.startsWith("mon")) {
				fields.add(Calendar.MONDAY);
			} else if (strField.startsWith("tue")) {
				fields.add(Calendar.TUESDAY);
			} else if (strField.startsWith("wed")) {
				fields.add(Calendar.WEDNESDAY);
			} else if (strField.startsWith("thu")) {
				fields.add(Calendar.THURSDAY);
			} else if (strField.startsWith("fri")) {
				fields.add(Calendar.FRIDAY);
			} else if (strField.startsWith("sat")) {
				fields.add(Calendar.SATURDAY);
			} else if (strField.startsWith("sun")) {
				fields.add(Calendar.SUNDAY);
			} else if (strField.equals("weekend")) {
				fields.add(Calendar.SATURDAY);
				fields.add(Calendar.SUNDAY);
			} else if (strField.equals("weekday")) {
				fields.add(Calendar.MONDAY);
				fields.add(Calendar.TUESDAY);
				fields.add(Calendar.WEDNESDAY);
				fields.add(Calendar.THURSDAY);
				fields.add(Calendar.FRIDAY);
			}
		}
		
		if (fields.size() == 0)
			return null;
		
		int currentDay = date.get(Calendar.DAY_OF_WEEK);
		int nextDay = currentDay;
		
		for (int i = 0; i < 7; i++) {
			nextDay = (nextDay % 7) + 1;
			
			if (fields.contains(nextDay))
				break;
		}
		
		Calendar clone = DateUtils.cloneCalendar(date);
		date.set(Calendar.DAY_OF_WEEK, nextDay);
		if (date.compareTo(clone) <= 0)
			date.add(Calendar.WEEK_OF_YEAR, 1);
		
		return date;
	}
	
}
