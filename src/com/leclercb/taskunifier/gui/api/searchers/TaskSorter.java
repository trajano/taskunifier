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
package com.leclercb.taskunifier.gui.api.searchers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.SortOrder;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.ListUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class TaskSorter implements PropertyChangeListener, ListChangeSupported, PropertyChangeSupported, Serializable, Cloneable {
	
	public static class TaskSorterElement implements Cloneable, PropertyChangeSupported {
		
		public static final String PROP_ORDER = "order";
		public static final String PROP_COLUMN = "column";
		public static final String PROP_SORT_ORDER = "sortOrder";
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private int order;
		private TaskColumn column;
		private SortOrder sortOrder;
		
		public TaskSorterElement(
				int order,
				TaskColumn column,
				SortOrder sortOrder) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setOrder(order);
			this.setColumn(column);
			this.setSortOrder(sortOrder);
		}
		
		@Override
		public TaskSorterElement clone() {
			return new TaskSorterElement(
					this.order,
					this.column,
					this.sortOrder);
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
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(listener);
		}
		
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
		
	}
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<TaskSorterElement> elements;
	
	public TaskSorter() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.elements = new ArrayList<TaskSorterElement>();
	}
	
	@Override
	public TaskSorter clone() {
		TaskSorter sorter = new TaskSorter();
		
		for (TaskSorterElement e : this.elements)
			sorter.addElement(e.clone());
		
		return sorter;
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
		List<TaskSorterElement> sortElements = new ArrayList<TaskSorterElement>(
				this.elements);
		
		Collections.sort(sortElements, new Comparator<TaskSorterElement>() {
			
			@Override
			public int compare(TaskSorterElement o1, TaskSorterElement o2) {
				return new Integer(o1.getOrder()).compareTo(o2.getOrder());
			}
			
		});
		
		return Collections.unmodifiableList(sortElements);
	}
	
	public void addElement(TaskSorterElement element) {
		CheckUtils.isNotNull(element, "Element cannot be null");
		this.elements.add(element);
		element.addPropertyChangeListener(this);
		int index = this.elements.indexOf(element);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				element);
	}
	
	public void removeElement(TaskSorterElement element) {
		CheckUtils.isNotNull(element, "Searcher cannot be null");
		
		int index = this.elements.indexOf(element);
		if (this.elements.remove(element)) {
			element.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					element);
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
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public String toString() {
		return Translations.getString("general.sort")
				+ ": "
				+ ListUtils.listToString(this.elements, ", ");
	}
	
}
