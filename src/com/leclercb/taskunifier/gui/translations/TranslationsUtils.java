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
package com.leclercb.taskunifier.gui.translations;

import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.CalendarCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.Condition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.DaysCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.EnumCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.NumberCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public final class TranslationsUtils {
	
	private TranslationsUtils() {
		
	}
	
	public static String translateModelType(ModelType type, boolean plurial) {
		if (plurial) {
			switch (type) {
				case CONTACT:
					return Translations.getString("general.contacts");
				case CONTEXT:
					return Translations.getString("general.contexts");
				case FOLDER:
					return Translations.getString("general.folders");
				case GOAL:
					return Translations.getString("general.goals");
				case LOCATION:
					return Translations.getString("general.locations");
				case NOTE:
					return Translations.getString("general.notes");
				case TASK:
					return Translations.getString("general.tasks");
			}
		}
		
		switch (type) {
			case CONTACT:
				return Translations.getString("general.contact");
			case CONTEXT:
				return Translations.getString("general.context");
			case FOLDER:
				return Translations.getString("general.folder");
			case GOAL:
				return Translations.getString("general.goal");
			case LOCATION:
				return Translations.getString("general.location");
			case NOTE:
				return Translations.getString("general.note");
			case TASK:
				return Translations.getString("general.task");
		}
		
		return "Missing translation";
	}
	
	public static String translateSynchronizerChoice(SynchronizerChoice choice) {
		switch (choice) {
			case KEEP_APPLICATION:
				return Translations.getString("general.synchronizer.choice.keep_application");
			case KEEP_LAST_UPDATED:
				return Translations.getString("general.synchronizer.choice.keep_last_updated");
			case KEEP_API:
				return Translations.getString(
						"general.synchronizer.choice.keep_api",
						SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName());
		}
		
		return "Missing translation";
	}
	
	public static String translateGoalLevel(GoalLevel level) {
		switch (level) {
			case LIFE_TIME:
				return Translations.getString("general.goal.level.life_time");
			case LONG_TERM:
				return Translations.getString("general.goal.level.long_term");
			case SHORT_TERM:
				return Translations.getString("general.goal.level.short_term");
		}
		
		return "Missing translation";
	}
	
	public static String translateTaskPriority(TaskPriority priority) {
		switch (priority) {
			case NEGATIVE:
				return Translations.getString("general.task.priority.negative");
			case LOW:
				return Translations.getString("general.task.priority.low");
			case MEDIUM:
				return Translations.getString("general.task.priority.medium");
			case HIGH:
				return Translations.getString("general.task.priority.high");
			case TOP:
				return Translations.getString("general.task.priority.top");
		}
		
		return "Missing translation";
	}
	
	public static String translateTaskRepeatFrom(TaskRepeatFrom repeatFrom) {
		switch (repeatFrom) {
			case DUE_DATE:
				return Translations.getString("general.task.repeat_from.due_date");
			case COMPLETION_DATE:
				return Translations.getString("general.task.repeat_from.completion_date");
		}
		
		return "Missing translation";
	}
	
	public static String translateTaskStatus(TaskStatus status) {
		switch (status) {
			case NONE:
				return Translations.getString("general.task.status.none");
			case NEXT_ACTION:
				return Translations.getString("general.task.status.next_action");
			case ACTIVE:
				return Translations.getString("general.task.status.active");
			case PLANNING:
				return Translations.getString("general.task.status.planning");
			case DELEGATED:
				return Translations.getString("general.task.status.delegated");
			case WAITING:
				return Translations.getString("general.task.status.waiting");
			case HOLD:
				return Translations.getString("general.task.status.hold");
			case POSTPONED:
				return Translations.getString("general.task.status.postponed");
			case SOMEDAY:
				return Translations.getString("general.task.status.someday");
			case CANCELED:
				return Translations.getString("general.task.status.canceled");
			case REFERENCE:
				return Translations.getString("general.task.status.reference");
		}
		
		return "Missing translation";
	}
	
	public static String translateBoolean(Boolean bool) {
		if (bool != null && bool)
			return Translations.getString("general.yes");
		
		return Translations.getString("general.no");
	}
	
	public static String translateFilterLink(FilterLink link) {
		switch (link) {
			case AND:
				return Translations.getString("general.and");
			case OR:
				return Translations.getString("general.or");
		}
		
		return "Missing translation";
	}
	
	public static String translateSortOrder(SortOrder sortOrder) {
		switch (sortOrder) {
			case ASCENDING:
				return Translations.getString("general.sort_order.ascending");
			case DESCENDING:
				return Translations.getString("general.sort_order.descending");
			case UNSORTED:
				return Translations.getString("general.sort_order.unsorted");
		}
		
		return "Missing translation";
	}
	
	public static String translateFilterCondition(Condition<?, ?> condition) {
		if (condition instanceof CalendarCondition) {
			switch ((CalendarCondition) condition) {
				case AFTER:
					return Translations.getString("task_filter_condition.after");
				case BEFORE:
					return Translations.getString("task_filter_condition.before");
				case EQUALS:
					return Translations.getString("task_filter_condition.equals");
			}
		}
		
		if (condition instanceof DaysCondition) {
			switch ((DaysCondition) condition) {
				case EQUALS:
					return Translations.getString("task_filter_condition.equals");
				case LESS_THAN:
					return Translations.getString("task_filter_condition.before");
				case LESS_THAN_OR_EQUALS:
					return Translations.getString("task_filter_condition.before_or_equals");
				case LESS_THAN_USING_TIME:
					return Translations.getString("task_filter_condition.before_using_time");
				case GREATER_THAN:
					return Translations.getString("task_filter_condition.after");
				case GREATER_THAN_OR_EQUALS:
					return Translations.getString("task_filter_condition.after_or_equals");
				case GREATER_THAN_USING_TIME:
					return Translations.getString("task_filter_condition.after_using_time");
			}
		}
		
		if (condition instanceof EnumCondition) {
			switch ((EnumCondition) condition) {
				case EQUALS:
					return Translations.getString("task_filter_condition.equals");
				case LESS_THAN:
					return Translations.getString("task_filter_condition.less_than");
				case LESS_THAN_OR_EQUALS:
					return Translations.getString("task_filter_condition.less_than_or_equals");
				case GREATER_THAN:
					return Translations.getString("task_filter_condition.greater_than");
				case GREATER_THAN_OR_EQUALS:
					return Translations.getString("task_filter_condition.greater_than_or_equals");
				case NOT_EQUALS:
					return Translations.getString("task_filter_condition.not_equals");
			}
		}
		
		if (condition instanceof ModelCondition) {
			switch ((ModelCondition) condition) {
				case EQUALS:
					return Translations.getString("task_filter_condition.equals");
				case NOT_EQUALS:
					return Translations.getString("task_filter_condition.not_equals");
			}
		}
		
		if (condition instanceof NumberCondition) {
			switch ((NumberCondition) condition) {
				case EQUALS:
					return Translations.getString("task_filter_condition.equals");
				case LESS_THAN:
					return Translations.getString("task_filter_condition.less_than");
				case LESS_THAN_OR_EQUALS:
					return Translations.getString("task_filter_condition.less_than_or_equals");
				case GREATER_THAN:
					return Translations.getString("task_filter_condition.greater_than");
				case GREATER_THAN_OR_EQUALS:
					return Translations.getString("task_filter_condition.greater_than_or_equals");
				case NOT_EQUALS:
					return Translations.getString("task_filter_condition.not_equals");
			}
		}
		
		if (condition instanceof StringCondition) {
			switch ((StringCondition) condition) {
				case CONTAINS:
					return Translations.getString("task_filter_condition.contains");
				case DOES_NOT_CONTAIN:
					return Translations.getString("task_filter_condition.does_not_contain");
				case EQUALS:
					return Translations.getString("task_filter_condition.equals");
				case ENDS_WITH:
					return Translations.getString("task_filter_condition.ends_with");
				case NOT_EQUALS:
					return Translations.getString("task_filter_condition.not_equals");
				case STARTS_WITH:
					return Translations.getString("task_filter_condition.starts_with");
			}
		}
		
		return "Missing translation";
	}
	
}
