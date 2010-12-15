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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.leclercb.taskunifier.api.event.ListenerList;
import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.event.listchange.ListChangeModel;
import com.leclercb.taskunifier.api.event.propertychange.AbstractPropertyChangeModel;
import com.leclercb.taskunifier.api.event.propertychange.PropertyChangeModel;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskFilter implements PropertyChangeListener, ListChangeModel, PropertyChangeModel, Serializable {

	public static enum Link {

		AND, OR;

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

		GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS, EQUALS;

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
			long milliSeconds2 = Calendar.getInstance().getTimeInMillis();
			long diff = milliSeconds1 - milliSeconds2;
			double diffDays = diff / (24 * 60 * 60 * 1000.0);

			if (this == GREATER_THAN) {
				return diffDays > value;
			}

			if (this == GREATER_THAN_OR_EQUALS) {
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

	public static enum NumberCondition implements Condition<Number, Number> {

		GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS, EQUALS, NOT_EQUALS;

		@Override
		public Class<?> getValueType() {
			return Number.class;
		}

		@Override
		public Class<?> getTaskValueType() {
			return Number.class;
		}

		@Override
		public boolean include(Number value, Number taskValue) {
			if (this == GREATER_THAN) {
				return taskValue.doubleValue() > value.doubleValue();
			}

			if (this == GREATER_THAN_OR_EQUALS) {
				return taskValue.doubleValue() >= value.doubleValue();
			}

			if (this == LESS_THAN) {
				return taskValue.doubleValue() < value.doubleValue();
			}

			if (this == LESS_THAN_OR_EQUALS) {
				return taskValue.doubleValue() <= value.doubleValue();
			}

			if (this == EQUALS) {
				return taskValue.doubleValue() == value.doubleValue();
			}

			if (this == NOT_EQUALS) {
				return taskValue.doubleValue() != value.doubleValue();
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

	public static class TaskFilterElement extends AbstractPropertyChangeModel {

		public static final String PROP_COLUMN = "FILTER_ELEMENT_COLUMN";
		public static final String PROP_CONDITION = "FILTER_ELEMENT_CONDITION";
		public static final String PROP_VALUE = "FILTER_ELEMENT_VALUE";

		private TaskFilter parent;
		private TaskColumn column;
		private Condition<?, ?> condition;
		private Object value;

		public TaskFilterElement(TaskColumn column, Condition<?, ?> condition, Object value) {
			this.checkAndSet(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, CalendarCondition condition, Calendar value) {
			this.checkAndSet(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, DaysCondition condition, Integer value) {
			this.checkAndSet(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, StringCondition condition, String value) {
			this.checkAndSet(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, NumberCondition condition, Number value) {
			this.checkAndSet(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, EnumCondition condition, Enum<?> value) {
			this.checkAndSet(column, condition, value);
		}

		public TaskFilterElement(TaskColumn column, ModelCondition condition, Model value) {
			this.checkAndSet(column, condition, value);
		}

		public void set(TaskColumn column, CalendarCondition condition, Calendar value) {
			this.checkAndSet(column, condition, value);
		}

		public void set(TaskColumn column, DaysCondition condition, Integer value) {
			this.checkAndSet(column, condition, value);
		}

		public void set(TaskColumn column, StringCondition condition, String value) {
			this.checkAndSet(column, condition, value);
		}

		public void set(TaskColumn column, NumberCondition condition, Number value) {
			this.checkAndSet(column, condition, value);
		}

		public void set(TaskColumn column, EnumCondition condition, Enum<?> value) {
			this.checkAndSet(column, condition, value);
		}

		public void set(TaskColumn column, ModelCondition condition, Model value) {
			this.checkAndSet(column, condition, value);
		}

		public void checkAndSet(TaskColumn column, Condition<?, ?> condition, Object value) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			CheckUtils.isNotNull(condition, "Condition cannot be null");

			this.check(column, condition, value);

			this.setColumn(column);
			this.setCondition(condition);
			this.setValue(value);
		}

		public TaskFilter getParent() {
			return this.parent;
		}

		private void setParent(TaskFilter parent) {
			this.parent = parent;
		}

		public TaskColumn getColumn() {
			return this.column;
		}

		private void setColumn(TaskColumn column) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			TaskColumn oldColumn = this.column;
			this.column = column;
			this.firePropertyChange(PROP_COLUMN, oldColumn, column);
		}

		public Condition<?, ?> getCondition() {
			return this.condition;
		}

		private void setCondition(Condition<?, ?> condition) {
			CheckUtils.isNotNull(condition, "Condition cannot be null");
			Condition<?, ?> oldCondition = this.condition;
			this.condition = condition;
			this.firePropertyChange(PROP_CONDITION, oldCondition, condition);
		}

		public Object getValue() {
			return this.value;
		}

		private void setValue(Object value) {
			Object oldValue = this.value;
			this.value = value;
			this.firePropertyChange(PROP_VALUE, oldValue, value);
		}

		private void check(TaskColumn column, Condition<?, ?> condition, Object value) {
			if (value != null && !condition.getValueType().isInstance(value))
				throw new IllegalArgumentException("Value is not an instance of " + condition.getValueType());

			if (!condition.getTaskValueType().isAssignableFrom(column.getType()))
				throw new IllegalArgumentException("The task column is incompatible with this condition");
		}

		public boolean include(Task task) {
			Object taskValue = this.column.getValue(task);

			if (this.value == null && taskValue == null)
				return true;

			if (this.value == null || taskValue == null)
				return false;

			if (this.condition instanceof CalendarCondition) {
				CalendarCondition c = (CalendarCondition) this.condition;
				return c.include((Calendar) this.value, (Calendar) taskValue);
			} else if (this.condition instanceof DaysCondition) {
				DaysCondition c = (DaysCondition) this.condition;
				return c.include((Integer) this.value, (Calendar) taskValue);
			} else if (this.condition instanceof StringCondition) {
				StringCondition c = (StringCondition) this.condition;
				return c.include((String) this.value, taskValue);
			} else if (this.condition instanceof EnumCondition) {
				EnumCondition c = (EnumCondition) this.condition;
				return c.include((Enum<?>) this.value, (Enum<?>) taskValue);
			} else if (this.condition instanceof ModelCondition) {
				ModelCondition c = (ModelCondition) this.condition;
				return c.include((Model) this.value, (Model) taskValue);
			}

			return false;
		}

	}

	public static final String PROP_LINK = "FILTER_LINK";

	private ListenerList<ListChangeListener> listChangeListenerList;
	private ListenerList<PropertyChangeListener> propertyChangeListenerList;

	private TaskFilter parent;
	private Link link;
	private List<TaskFilter> filters;
	private List<TaskFilterElement> elements;

	public TaskFilter() {
		this.listChangeListenerList = new ListenerList<ListChangeListener>();
		this.propertyChangeListenerList = new ListenerList<PropertyChangeListener>();

		this.setParent(null);
		this.setLink(Link.AND);

		this.filters = new ArrayList<TaskFilter>();
		this.elements = new ArrayList<TaskFilterElement>();
	}

	public TaskFilter getParent() {
		return this.parent;
	}

	private void setParent(TaskFilter parent) {
		this.parent = parent;
	}

	public Link getLink() {
		return this.link;
	}

	public void setLink(Link link) {
		CheckUtils.isNotNull(link, "Link cannot be null");
		Link oldLink = this.link;
		this.link = link;
		this.firePropertyChange(PROP_LINK, oldLink, link);
	}

	public int getIndexOf(TaskFilterElement element) {
		return this.elements.indexOf(element);
	}

	public int getElementCount() {
		return this.elements.size();
	}

	public TaskFilterElement getElement(int index) {
		return this.elements.get(index);
	}

	public List<TaskFilterElement> getElements() {
		return Collections.unmodifiableList(this.elements);
	}

	public void addElement(TaskFilterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		this.elements.add(element);
		element.setParent(this);
		element.addPropertyChangeListener(this);
		int index = this.elements.indexOf(element);
		this.fireListChange(ListChangeEvent.VALUE_ADDED, index, element);
	}

	public void removeElement(TaskFilterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");

		int index = this.elements.indexOf(element);
		if (this.elements.remove(element)) {
			element.setParent(null);
			element.removePropertyChangeListener(this);
			this.fireListChange(ListChangeEvent.VALUE_REMOVED, index, element);
		}
	}

	public int getIndexOf(TaskFilter filter) {
		return this.filters.indexOf(filter);
	}

	public int getFilterCount() {
		return this.filters.size();
	}

	public TaskFilter getFilter(int index) {
		return this.filters.get(index);
	}

	public List<TaskFilter> getFilters() {
		return Collections.unmodifiableList(this.filters);
	}

	public void addFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		this.filters.add(filter);
		filter.setParent(this);
		filter.addPropertyChangeListener(this);
		int index = this.filters.indexOf(filter);
		this.fireListChange(ListChangeEvent.VALUE_ADDED, index, filter);
	}

	public void removeFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");

		int index = this.filters.indexOf(filter);
		if (this.filters.remove(filter)) {
			filter.setParent(null);
			filter.removePropertyChangeListener(this);
			this.fireListChange(ListChangeEvent.VALUE_REMOVED, index, filter);
		}
	}

	public boolean include(Task task) {
		if (this.link == Link.AND) {
			for (TaskFilterElement element : this.elements) {
				if (!element.include(task))
					return false;
			}

			for (TaskFilter filter : this.filters) {
				if (!filter.include(task))
					return false;
			}

			return true;
		} else {
			for (TaskFilterElement element : this.elements) {
				if (element.include(task))
					return true;
			}

			for (TaskFilter filter : this.filters) {
				if (filter.include(task))
					return true;
			}

			return false;
		}
	}

	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeListenerList.addListener(listener);
	}

	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeListenerList.removeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeListenerList.addListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeListenerList.removeListener(listener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.firePropertyChange(event);
	}

	protected void fireListChange(ListChangeEvent event) {
		if (this.getParent() != null)
			this.getParent().fireListChange(event);

		for (ListChangeListener listener : this.listChangeListenerList)
			listener.listChange(event);
	}

	protected void fireListChange(int changeType, int index, Object value) {
		this.fireListChange(new ListChangeEvent(this, changeType, index, value));
	}

	protected void firePropertyChange(PropertyChangeEvent evt) {
		if (this.getParent() != null)
			this.getParent().firePropertyChange(evt);

		for (PropertyChangeListener listener : this.propertyChangeListenerList)
			listener.propertyChange(evt);
	}

	protected void firePropertyChange(String property, Object oldValue, Object newValue) {
		this.firePropertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
	}

	public String toDetailedString(String before) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(before + "Link: " + this.link + "\n");

		int i = 0;
		for (TaskFilterElement element : this.elements) {
			buffer.append(before + "Element: " + (i++) + "\n");
			buffer.append(before + "\tColumn: " + element.getColumn() + "\n");
			buffer.append(before + "\tCondition: " + element.getCondition() + "\n");
			buffer.append(before + "\tValue: " + element.getValue() + "\n");
		}

		i = 0;
		for (TaskFilter filter : this.filters) {
			buffer.append(before + "Filter: " + (i++) + "\n");
			buffer.append(filter.toDetailedString(before + "\t"));
		}

		return buffer.toString();
	}

}
