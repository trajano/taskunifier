package com.leclercb.taskunifier.gui.api.searchers.sorters;

import java.beans.PropertyChangeListener;

import javax.swing.SortOrder;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskSorterElement implements Cloneable, Comparable<TaskSorterElement>, PropertyChangeSupported {
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_COLUMN = "column";
	public static final String PROP_SORT_ORDER = "sortOrder";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private int order;
	private TaskColumn column;
	private SortOrder sortOrder;
	
	public TaskSorterElement(int order, TaskColumn column, SortOrder sortOrder) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setOrder(order);
		this.setColumn(column);
		this.setSortOrder(sortOrder);
	}
	
	@Override
	public TaskSorterElement clone() {
		return new TaskSorterElement(this.order, this.column, this.sortOrder);
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		int oldOrder = this.order;
		this.order = order;
		this.propertyChangeSupport.firePropertyChange(
				PROP_ORDER,
				oldOrder,
				order);
	}
	
	public TaskColumn getColumn() {
		return this.column;
	}
	
	public void setColumn(TaskColumn column) {
		CheckUtils.isNotNull(column, "Column cannot be null");
		TaskColumn oldColumn = this.column;
		this.column = column;
		this.propertyChangeSupport.firePropertyChange(
				PROP_COLUMN,
				oldColumn,
				column);
	}
	
	public SortOrder getSortOrder() {
		return this.sortOrder;
	}
	
	public void setSortOrder(SortOrder sortOrder) {
		CheckUtils.isNotNull(sortOrder, "Sort order cannot be null");
		SortOrder oldSortOrder = this.sortOrder;
		this.sortOrder = sortOrder;
		this.propertyChangeSupport.firePropertyChange(
				PROP_SORT_ORDER,
				oldSortOrder,
				sortOrder);
	}
	
	@Override
	public String toString() {
		return this.column
				+ " ("
				+ TranslationsUtils.translateSortOrder(this.sortOrder)
				+ ")";
	}
	
	@Override
	public int compareTo(TaskSorterElement element) {
		if (element == null)
			return 1;
		
		return new Integer(this.order).compareTo(element.order);
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
