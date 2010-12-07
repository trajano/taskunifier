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
package com.leclercb.taskunifier.gui.searchers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskFilter {

	public static enum Link {

		AND, 
		OR;

	}

	public static interface Condition<ValueType, TaskValueType> {

		public abstract Class<?> getValueType();
		public abstract Class<?> getTaskValueType();
		public abstract String name();
		public abstract boolean include(ValueType value, TaskValueType taskValue);

	}

	public static enum CalendarCondition implements Condition<Calendar, Calendar> {

		AFTER, BEFORE, EQUALS;

		@Override
		public Class<?> getValueType() {
			return Calendar.class;
		}

		@Override
		public Class<?> getTaskValueType() {
			return Calendar.class;
		}

		@Override
		public boolean include(Calendar value, Calendar taskValue) {
			if (this == AFTER) {
				return taskValue.compareTo(value) > 0;
			}

			if (this == BEFORE) {
				return taskValue.compareTo(value) < 0;
			}

			if (this == EQUALS) {
				return taskValue.equals(value);
			}

			return false;
		}

	}

	public static enum DaysCondition implements Condition<Integer, Calendar> {

		MORE_THAN, MORE_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS, EQUALS;

		@Override
		public Class<?> getValueType() {
			return Integer.class;
		}

		@Override
		public Class<?> getTaskValueType() {
			return Calendar.class;
		}

		@Override
		public boolean include(Integer value, Calendar taskValue) {
			long milliSeconds1 = taskValue.getTimeInMillis();
			long milliSeconds2 = GregorianCalendar.getInstance().getTimeInMillis();
			long diff = milliSeconds1 - milliSeconds2;
			double diffDays = diff / (24 * 60 * 60 * 1000.0);

			if (this == MORE_THAN) {
				return diffDays > value;
			}

			if (this == MORE_THAN_OR_EQUALS) {
				return diffDays >= value;
			}

			if (this == LESS_THAN) {
				return diffDays < value;
			}

			if (this == LESS_THAN_OR_EQUALS) {
				return diffDays <= value;
			}

			if (this == EQUALS) {
				return diffDays == value;
			}

			return false;
		}

	}

	public static enum StringCondition implements Condition<String, Object> {

		EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, NOT_EQUALS;

		@Override
		public Class<?> getValueType() {
			return String.class;
		}

		@Override
		public Class<?> getTaskValueType() {
			return Object.class;
		}

		@Override
		public boolean include(String value, Object taskValue) {
			if (this == EQUALS) {
				return taskValue.toString().equalsIgnoreCase(value);
			}

			if (this == CONTAINS) {
				return taskValue.toString().toLowerCase().contains(value.toLowerCase());
			}

			if (this == STARTS_WITH) {
				return taskValue.toString().toLowerCase().startsWith(value.toLowerCase());
			}

			if (this == ENDS_WITH) {
				return taskValue.toString().toLowerCase().endsWith(value.toLowerCase());
			}

			if (this == NOT_EQUALS) {
				return !(taskValue.toString().equalsIgnoreCase(value));
			}

			return false;
		}

	}

	public static enum EnumCondition implements Condition<Enum<?>, Enum<?>> {

		EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS;

		@Override
		public Class<?> getValueType() {
			return Enum.class;
		}

		@Override
		public Class<?> getTaskValueType() {
			return Enum.class;
		}

		@Override
		public boolean include(Enum<?> value, Enum<?> taskValue) {
			if (this == EQUALS) {
				return taskValue.equals(value);
			}

			if (this == NOT_EQUALS) {
				return !(taskValue.equals(value));
			}

			if (this == GREATER_THAN) {
				return taskValue.ordinal() > value.ordinal();
			}

			if (this == GREATER_THAN_OR_EQUALS) {
				return taskValue.ordinal() >= value.ordinal();
			}

			if (this == LESS_THAN) {
				return taskValue.ordinal() < value.ordinal();
			}

			if (this == LESS_THAN_OR_EQUALS) {
				return taskValue.ordinal() <= value.ordinal();
			}

			return false;
		}

	}

	public static enum ModelCondition implements Condition<Model, Model> {

		EQUALS, NOT_EQUALS;

		@Override
		public Class<?> getValueType() {
			return Model.class;
		}

		@Override
		public Class<?> getTaskValueType() {
			return Model.class;
		}

		@Override
		public boolean include(Model value, Model taskValue) {
			if (this == EQUALS) {
				return taskValue.equals(value);
			}

			if (this == NOT_EQUALS) {
				return !(taskValue.equals(value));
			}

			return false;
		}

	}

	public static class TaskFilterElement {

		private TaskColumn column;
		private Condition<?, ?> condition;
		private Object value;

		public TaskFilterElement(TaskColumn column, CalendarCondition condition, Calendar value) {
			this.initialize(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, DaysCondition condition, Integer value) {
			this.initialize(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, StringCondition condition, String value) {
			this.initialize(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, EnumCondition condition, Enum<?> value) {
			this.initialize(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, ModelCondition condition, Model value) {
			this.initialize(column, condition, value);
		}

		private void initialize(TaskColumn column, Condition<?, ?> condition, Object value) {
			this.setColumn(column);
			this.setCondition(condition);
			this.setValue(value);

			if (value != null && !condition.getValueType().isInstance(value))
				throw new IllegalArgumentException("Value is not an instance of " + condition.getValueType());

			if (!condition.getTaskValueType().isAssignableFrom(column.getType()))
				throw new IllegalArgumentException("The task column is incompatible with this condition");
		}

		public TaskColumn getColumn() {
			return column;
		}

		private void setColumn(TaskColumn column) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			this.column = column;
		}

		public Condition<?, ?> getCondition() {
			return condition;
		}

		private void setCondition(Condition<?, ?> condition) {
			CheckUtils.isNotNull(condition, "Condition cannot be null");
			this.condition = condition;
		}

		public Object getValue() {
			return value;
		}

		private void setValue(Object value) {
			this.value = value;
		}

		public boolean include(Task task) {
			Object taskValue = this.column.getValue(task);

			if (value == null && taskValue == null)
				return true;

			if (value == null || taskValue == null)
				return false;

			if (condition instanceof CalendarCondition) {
				CalendarCondition c = (CalendarCondition) condition;
				return c.include((Calendar) value, (Calendar) taskValue);
			} else if (condition instanceof DaysCondition) {
				DaysCondition c = (DaysCondition) condition;
				return c.include((Integer) value, (Calendar) taskValue);
			} else if (condition instanceof StringCondition) {
				StringCondition c = (StringCondition) condition;
				return c.include((String) value, (Object) taskValue);
			} else if (condition instanceof EnumCondition) {
				EnumCondition c = (EnumCondition) condition;
				return c.include((Enum<?>) value, (Enum<?>) taskValue);
			} else if (condition instanceof ModelCondition) {
				ModelCondition c = (ModelCondition) condition;
				return c.include((Model) value, (Model) taskValue);
			}

			return false;
		}

	}

	private Link link;
	private List<TaskFilter> filters;
	private List<TaskFilterElement> elements;

	public TaskFilter() {
		this.setLink(Link.AND);

		this.filters = new ArrayList<TaskFilter>();
		this.elements = new ArrayList<TaskFilterElement>();
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		CheckUtils.isNotNull(link, "Link cannot be null");
		this.link = link;
	}

	public List<TaskFilterElement> getElements() {
		return Collections.unmodifiableList(elements);
	}

	public void addElement(TaskFilterElement element) {
		this.elements.add(element);
	}

	public void removeElement(TaskFilterElement element) {
		this.elements.remove(element);
	}

	public List<TaskFilter> getFilters() {
		return Collections.unmodifiableList(filters);
	}

	public void addFilter(TaskFilter filter) {
		this.filters.add(filter);
	}

	public void removeFilter(TaskFilter filter) {
		this.filters.remove(filter);
	}

	public boolean include(Task task) {
		if (link == Link.AND) {
			for (TaskFilterElement element : elements) {
				if (!element.include(task))
					return false;
			}

			for (TaskFilter filter : filters) {
				if (!filter.include(task))
					return false;
			}

			return true;
		} else {
			for (TaskFilterElement element : elements) {
				if (element.include(task))
					return true;
			}

			for (TaskFilter filter : filters) {
				if (filter.include(task))
					return true;
			}

			return false;
		}
	}

	public String toDetailedString(String before) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(before + "Link: " + link + "\n");

		int i = 0;
		for (TaskFilterElement element : elements) {
			buffer.append(before + "Element: " + (i++) + "\n");
			buffer.append(before + "\tColumn: " + element.getColumn() + "\n");
			buffer.append(before + "\tCondition: " + element.getCondition() + "\n");
			buffer.append(before + "\tValue: " + element.getValue() + "\n");
		}

		i = 0;
		for (TaskFilter filter : filters) {
			buffer.append(before + "Filter: " + (i++) + "\n");
			buffer.append(filter.toDetailedString(before + "\t"));
		}

		return buffer.toString();
	}

}
