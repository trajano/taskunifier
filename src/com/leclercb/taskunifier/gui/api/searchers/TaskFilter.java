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
package com.leclercb.taskunifier.gui.api.searchers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.ListUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class TaskFilter implements ListChangeListener, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported, Serializable, Cloneable {
	
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
		
		AFTER,
		BEFORE,
		EQUALS;
		
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
		
		GREATER_THAN,
		GREATER_THAN_OR_EQUALS,
		LESS_THAN,
		LESS_THAN_OR_EQUALS,
		EQUALS;
		
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
	
	public static enum EnumCondition implements Condition<Enum<?>, Enum<?>> {
		
		EQUALS,
		NOT_EQUALS,
		GREATER_THAN,
		GREATER_THAN_OR_EQUALS,
		LESS_THAN,
		LESS_THAN_OR_EQUALS;
		
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
		
		EQUALS,
		NOT_EQUALS;
		
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
	
	public static enum NumberCondition implements Condition<Number, Number> {
		
		GREATER_THAN,
		GREATER_THAN_OR_EQUALS,
		LESS_THAN,
		LESS_THAN_OR_EQUALS,
		EQUALS,
		NOT_EQUALS;
		
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
	
	public static enum StringCondition implements Condition<String, Object> {
		
		EQUALS,
		CONTAINS,
		STARTS_WITH,
		ENDS_WITH,
		NOT_EQUALS;
		
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
				return taskValue.toString().toLowerCase().contains(
						value.toLowerCase());
			}
			
			if (this == STARTS_WITH) {
				return taskValue.toString().toLowerCase().startsWith(
						value.toLowerCase());
			}
			
			if (this == ENDS_WITH) {
				return taskValue.toString().toLowerCase().endsWith(
						value.toLowerCase());
			}
			
			if (this == NOT_EQUALS) {
				return !(taskValue.toString().equalsIgnoreCase(value));
			}
			
			return false;
		}
		
	}
	
	public static class TaskFilterElement implements Cloneable, PropertyChangeSupported {
		
		public static final String PROP_COLUMN = "column";
		public static final String PROP_CONDITION = "condition";
		public static final String PROP_VALUE = "value";
		
		private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
				this);
		
		private TaskFilter parent;
		private TaskColumn column;
		private Condition<?, ?> condition;
		private Object value;
		
		public TaskFilterElement(
				TaskColumn column,
				Condition<?, ?> condition,
				Object value) {
			this.checkAndSet(column, condition, value);
		}
		
		public TaskFilterElement(
				TaskColumn column,
				CalendarCondition condition,
				Calendar value) {
			this.checkAndSet(column, condition, value);
		}
		
		public TaskFilterElement(
				TaskColumn column,
				DaysCondition condition,
				Integer value) {
			this.checkAndSet(column, condition, value);
		}
		
		public TaskFilterElement(
				TaskColumn column,
				StringCondition condition,
				String value) {
			this.checkAndSet(column, condition, value);
		}
		
		public TaskFilterElement(
				TaskColumn column,
				NumberCondition condition,
				Number value) {
			this.checkAndSet(column, condition, value);
		}
		
		public TaskFilterElement(
				TaskColumn column,
				EnumCondition condition,
				Enum<?> value) {
			this.checkAndSet(column, condition, value);
		}
		
		public TaskFilterElement(
				TaskColumn column,
				ModelCondition condition,
				Model value) {
			this.checkAndSet(column, condition, value);
		}
		
		@Override
		public TaskFilterElement clone() {
			return new TaskFilterElement(
					this.column,
					this.condition,
					this.value);
		}
		
		public void set(
				TaskColumn column,
				CalendarCondition condition,
				Calendar value) {
			this.checkAndSet(column, condition, value);
		}
		
		public void set(
				TaskColumn column,
				DaysCondition condition,
				Integer value) {
			this.checkAndSet(column, condition, value);
		}
		
		public void set(
				TaskColumn column,
				StringCondition condition,
				String value) {
			this.checkAndSet(column, condition, value);
		}
		
		public void set(
				TaskColumn column,
				NumberCondition condition,
				Number value) {
			this.checkAndSet(column, condition, value);
		}
		
		public void set(
				TaskColumn column,
				EnumCondition condition,
				Enum<?> value) {
			this.checkAndSet(column, condition, value);
		}
		
		public void set(TaskColumn column, ModelCondition condition, Model value) {
			this.checkAndSet(column, condition, value);
		}
		
		public void checkAndSet(
				TaskColumn column,
				Condition<?, ?> condition,
				Object value) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			CheckUtils.isNotNull(condition, "Condition cannot be null");
			
			this.check(column, condition, value);
			
			List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();
			
			events.add(this.setColumn(column));
			events.add(this.setCondition(condition));
			events.add(this.setValue(value));
			
			for (PropertyChangeEvent event : events)
				this.propertyChangeSupport.firePropertyChange(event);
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
		
		private PropertyChangeEvent setColumn(TaskColumn column) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			TaskColumn oldColumn = this.column;
			this.column = column;
			return new PropertyChangeEvent(this, PROP_COLUMN, oldColumn, column);
		}
		
		public Condition<?, ?> getCondition() {
			return this.condition;
		}
		
		private PropertyChangeEvent setCondition(Condition<?, ?> condition) {
			CheckUtils.isNotNull(condition, "Condition cannot be null");
			Condition<?, ?> oldCondition = this.condition;
			this.condition = condition;
			return new PropertyChangeEvent(
					this,
					PROP_CONDITION,
					oldCondition,
					condition);
		}
		
		public Object getValue() {
			return this.value;
		}
		
		private PropertyChangeEvent setValue(Object value) {
			Object oldValue = this.value;
			this.value = value;
			return new PropertyChangeEvent(this, PROP_VALUE, oldValue, value);
		}
		
		private void check(
				TaskColumn column,
				Condition<?, ?> condition,
				Object value) {
			if (value != null && !condition.getValueType().isInstance(value))
				throw new IllegalArgumentException(
						"Value is not an instance of "
								+ condition.getValueType());
			
			if (!condition.getTaskValueType().isAssignableFrom(column.getType()))
				throw new IllegalArgumentException(
						"The task column is incompatible with this condition");
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
			} else if (this.condition instanceof EnumCondition) {
				EnumCondition c = (EnumCondition) this.condition;
				return c.include((Enum<?>) this.value, (Enum<?>) taskValue);
			} else if (this.condition instanceof ModelCondition) {
				ModelCondition c = (ModelCondition) this.condition;
				return c.include((Model) this.value, (Model) taskValue);
			} else if (this.condition instanceof NumberCondition) {
				NumberCondition c = (NumberCondition) this.condition;
				return c.include((Number) this.value, (Number) taskValue);
			} else if (this.condition instanceof StringCondition) {
				StringCondition c = (StringCondition) this.condition;
				return c.include((String) this.value, taskValue);
			}
			
			return false;
		}
		
		@Override
		public String toString() {
			String str = this.getColumn()
					+ " "
					+ TranslationsUtils.translateTaskFilterCondition(this.getCondition())
					+ " \"";
			
			switch (this.getColumn()) {
				case COMPLETED:
				case STAR:
					str += TranslationsUtils.translateBoolean(Boolean.parseBoolean(this.getValue().toString()));
					break;
				case PRIORITY:
					str += TranslationsUtils.translateTaskPriority((TaskPriority) this.getValue());
					break;
				case REPEAT_FROM:
					str += TranslationsUtils.translateTaskRepeatFrom((TaskRepeatFrom) this.getValue());
					break;
				case STATUS:
					str += TranslationsUtils.translateTaskStatus((TaskStatus) this.getValue());
					break;
				default:
					str += (this.getValue() == null ? "" : this.getValue());
					break;
			}
			
			return str + "\"";
		}
		
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(listener);
		}
		
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
		
	}
	
	public static final String PROP_LINK = "link";
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private TaskFilter parent;
	private Link link;
	private List<TaskFilter> filters;
	private List<TaskFilterElement> elements;
	
	public TaskFilter() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setParent(null);
		this.setLink(Link.AND);
		
		this.filters = new ArrayList<TaskFilter>();
		this.elements = new ArrayList<TaskFilterElement>();
	}
	
	@Override
	public TaskFilter clone() {
		TaskFilter filter = new TaskFilter();
		filter.setLink(this.link);
		
		for (TaskFilterElement e : this.elements)
			filter.addElement(e.clone());
		
		for (TaskFilter f : this.filters)
			filter.addFilter(f.clone());
		
		return filter;
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
		this.propertyChangeSupport.firePropertyChange(PROP_LINK, oldLink, link);
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
		
		if (element.getParent() != null) {
			element.getParent().removeElement(element);
		}
		
		element.setParent(this);
		element.addPropertyChangeListener(this);
		int index = this.elements.indexOf(element);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				element);
	}
	
	public void removeElement(TaskFilterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		
		int index = this.elements.indexOf(element);
		if (this.elements.remove(element)) {
			element.setParent(null);
			element.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					element);
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
		
		if (filter.getParent() != null) {
			filter.getParent().removeFilter(filter);
		}
		
		filter.setParent(this);
		filter.addListChangeListener(this);
		filter.addPropertyChangeListener(this);
		int index = this.filters.indexOf(filter);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				filter);
	}
	
	public void removeFilter(TaskFilter filter) {
		CheckUtils.isNotNull(filter, "Filter cannot be null");
		
		int index = this.filters.indexOf(filter);
		if (this.filters.remove(filter)) {
			filter.setParent(null);
			filter.removeListChangeListener(this);
			filter.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					filter);
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
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		this.listChangeSupport.fireListChange(event);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public String toString() {
		if (this.elements.size() == 0 && this.filters.size() == 0)
			return "";
		
		StringBuffer buffer = new StringBuffer(
				Translations.getString("general.filter") + ": ((");
		
		List<Object> list = new ArrayList<Object>();
		list.addAll(this.elements);
		list.addAll(this.filters);
		
		buffer.append(ListUtils.listToString(
				list,
				") "
						+ TranslationsUtils.translateTaskFilterLink(this.link)
						+ " ("));
		
		buffer.append("))");
		
		return buffer.toString();
	}
	
}
