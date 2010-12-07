/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.translations;

import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeat;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;

public final class TranslationsUtils {

	private TranslationsUtils() {

	}

	public static String translateSynchronizerChoice(SynchronizerChoice choice) {
		switch (choice) {
		case KEEP_APPLICATION: return Translations.getString("general.synchronizer.choice.keep_application");
		case KEEP_LAST_UPDATED: return Translations.getString("general.synchronizer.choice.keep_last_updated");
		case KEEP_TOODLEDO: return Translations.getString("general.synchronizer.choice.keep_toodledo");
		}

		return "Missing translation";
	}

	public static String translateGoalLevel(GoalLevel level) {
		switch (level) {
		case LIFE_TIME: return Translations.getString("general.goal.level.life_time");
		case LONG_TERM: return Translations.getString("general.goal.level.long_term");
		case SHORT_TERM: return Translations.getString("general.goal.level.short_term");
		}

		return "Missing translation";
	}

	public static String translateTaskPriority(TaskPriority priority) {
		switch (priority) {
		case NEGATIVE: return Translations.getString("general.task.priority.negative");
		case LOW: return Translations.getString("general.task.priority.low");
		case MEDIUM: return Translations.getString("general.task.priority.medium");
		case HIGH: return Translations.getString("general.task.priority.high");
		case TOP: return Translations.getString("general.task.priority.top");
		}

		return "Missing translation";
	}

	public static String translateTaskRepeat(TaskRepeat repeat) {
		switch (repeat) {
		case NO_REPEAT: return Translations.getString("general.task.repeat.no_repeat");
		case WEEKLY: return Translations.getString("general.task.repeat.weekly");
		case MONTHLY: return Translations.getString("general.task.repeat.monthly");
		case YEARLY: return Translations.getString("general.task.repeat.yearly");
		case DAILY: return Translations.getString("general.task.repeat.daily");
		case BIWEEKLY: return Translations.getString("general.task.repeat.biweekly");
		case BIMONTHLY: return Translations.getString("general.task.repeat.bimonthly");
		case SEMIANNUALLY: return Translations.getString("general.task.repeat.semiannualy");
		case QUARTERLY: return Translations.getString("general.task.repeat.quarterly");
		case WITH_PARENT: return Translations.getString("general.task.repeat.with_parent");
		case ADVANCED: return Translations.getString("general.task.repeat.advanced");
		}

		return "Missing translation";
	}

	public static String translateTaskStatus(TaskStatus status) {
		switch (status) {
		case NONE: return Translations.getString("general.task.status.none");
		case NEXT_ACTION: return Translations.getString("general.task.status.next_action");
		case ACTIVE: return Translations.getString("general.task.status.active");
		case PLANNING: return Translations.getString("general.task.status.planning");
		case DELEGATED: return Translations.getString("general.task.status.delegated");
		case WAITING: return Translations.getString("general.task.status.waiting");
		case HOLD: return Translations.getString("general.task.status.hold");
		case POSTPONED: return Translations.getString("general.task.status.postponed");
		case SOMEDAY: return Translations.getString("general.task.status.someday");
		case CANCELED: return Translations.getString("general.task.status.canceled");
		case REFERENCE: return Translations.getString("general.task.status.reference");
		}

		return "Missing translation";
	}

}
