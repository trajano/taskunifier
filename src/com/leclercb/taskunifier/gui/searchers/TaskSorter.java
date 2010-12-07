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
import java.util.Collections;
import java.util.List;

import javax.swing.SortOrder;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskSorter {

	public static class TaskSorterElement {

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
			this.order = order;
		}

		public TaskColumn getColumn() {
			return column;
		}

		public void setColumn(TaskColumn column) {
			CheckUtils.isNotNull(column, "Column cannot be null");
			this.column = column;
		}

		public SortOrder getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(SortOrder sortOrder) {
			CheckUtils.isNotNull(sortOrder, "Sort order cannot be null");
			this.sortOrder = sortOrder;
		}

	}

	private List<TaskSorterElement> elements;

	public TaskSorter() {
		this.elements = new ArrayList<TaskSorterElement>();
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
		this.elements.add(element);
	}

	public void removeElement(TaskSorterElement element) {
		this.elements.remove(element);
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
