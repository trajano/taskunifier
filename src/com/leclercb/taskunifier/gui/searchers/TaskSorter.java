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
import java.util.Collections;
import java.util.List;

import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.event.listchange.ListChangeModel;
import com.leclercb.taskunifier.api.event.propertychange.AbstractPropertyChangeModel;
import com.leclercb.taskunifier.api.event.propertychange.PropertyChangeModel;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskSorter implements PropertyChangeListener, ListChangeModel, PropertyChangeModel, Serializable {

	public static class TaskSorterElement extends AbstractPropertyChangeModel {

		public static final String PROP_ORDER = "SORTER_ELEMENT_ORDER";
		public static final String PROP_COLUMN = "SORTER_ELEMENT_COLUMN";
		public static final String PROP_SORT_ORDER = "SORTER_ELEMENT_SORT_ORDER";

		private int order;
		private TaskColumn column;
		private SortOrder sortOrder;

		public TaskSorterElement(int order, TaskColumn column, SortOrder sortOrder) {
			this.setOrder(order);
			this.setColumn(column);
			this.setSortOrder(sortOrder);
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			int oldOrder = this.order;
			this.order = order;
			this.firePropertyChange(PROP_ORDER, oldOrder, order);
		}

		public TaskColumn getColumn() {
			return column;
		}

		public void setColumn(TaskColumn column) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			TaskColumn oldColumn = this.column;
			this.column = column;
			this.firePropertyChange(PROP_COLUMN, oldColumn, column);
		}

		public SortOrder getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(SortOrder sortOrder) {
			CheckUtils.isNotNull(sortOrder, "Sort order cannot be null");
			SortOrder oldSortOrder = this.sortOrder;
			this.sortOrder = sortOrder;
			this.firePropertyChange(PROP_SORT_ORDER, oldSortOrder, sortOrder);
		}

	}

	private List<ListChangeListener> listChangelisteners;
	private List<PropertyChangeListener> propertyChangelisteners;

	private List<TaskSorterElement> elements;

	public TaskSorter() {
		listChangelisteners = new ArrayList<ListChangeListener>();
		propertyChangelisteners = new ArrayList<PropertyChangeListener>();

		this.elements = new ArrayList<TaskSorterElement>();
	}

	public int getIndexOf(TaskSorterElement element) {
		return this.elements.indexOf(element);
	}

	public int getElementCount() {
		return this.elements.size();
	}

	public TaskSorterElement getElement(int index) {
		return this.elements.get(index);
	}

	public List<TaskSorterElement> getElements() {
		return Collections.unmodifiableList(elements);
	}

	public void addElement(TaskSorterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		elements.add(element);
		element.addPropertyChangeListener(this);
		int index = elements.indexOf(element);
		fireListChange(ListChangeEvent.VALUE_ADDED, index, element);
	}

	public void removeElement(TaskSorterElement element) {
		CheckUtils.isNotNull(element, "Searcher cannot be null");

		int index = elements.indexOf(element);
		if (elements.remove(element)) {
			element.removePropertyChangeListener(this);
			fireListChange(ListChangeEvent.VALUE_REMOVED, index, element);
		}
	}

	@Override
	public synchronized void addListChangeListener(ListChangeListener listener) {
		CheckUtils.isNotNull(listener, "Listener cannot be null");

		if (!listChangelisteners.contains(listener))
			listChangelisteners.add(listener);
	}

	@Override
	public synchronized void removeListChangeListener(ListChangeListener listener) {
		listChangelisteners.remove(listener);
	}

	@Override
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		CheckUtils.isNotNull(listener, "Listener cannot be null");

		if (!propertyChangelisteners.contains(listener))
			propertyChangelisteners.add(listener);
	}

	@Override
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangelisteners.remove(listener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		firePropertyChange(event);
	}

	protected synchronized void fireListChange(ListChangeEvent event) {
		for (ListChangeListener listener : listChangelisteners)
			listener.listChange(event);
	}

	protected void fireListChange(int changeType, int index, Object value) {
		fireListChange(new ListChangeEvent(this, changeType, index, value));
	}

	protected synchronized void firePropertyChange(PropertyChangeEvent evt) {
		for (PropertyChangeListener listener : propertyChangelisteners)
			listener.propertyChange(evt);
	}

	protected void firePropertyChange(String property, Object oldValue, Object newValue) {
		firePropertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
	}

	public String toDetailedString(String before) {
		StringBuffer buffer = new StringBuffer();

		for (TaskSorterElement element : elements) {
			buffer.append(before + "Order: " + element.getOrder() + "\n");
			buffer.append(before + "Column: " + element.getColumn() + "\n");
			buffer.append(before + "Sort Order: " + element.getSortOrder() + "\n");
		}

		return buffer.toString(); 
	}

}
