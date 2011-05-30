package com.leclercb.taskunifier.gui.api.searchers.filters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.CalendarCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.Condition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.DaysCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.EnumCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.NumberCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskFilterElement implements Cloneable, PropertyChangeSupported {
	
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
		return new TaskFilterElement(this.column, this.condition, this.value);
	}
	
	public void set(
			TaskColumn column,
			CalendarCondition condition,
			Calendar value) {
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
	
	protected void setParent(TaskFilter parent) {
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
			throw new IllegalArgumentException("Value is not an instance of "
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
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
