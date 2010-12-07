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
package com.leclercb.taskunifier.gui.components.searcherlist.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;

public class PersonalTaskSearcherListModel extends AbstractListModel implements TaskSearcherListModel, ListChangeListener, PropertyChangeListener {

	public PersonalTaskSearcherListModel() {
		TaskSearcherFactory.getInstance().addListChangeListener(this);
		TaskSearcherFactory.getInstance().addPropertyChangeListener(this);
	}

	@Override
	public TaskSearcher getTaskSearcher(int index) {
		return (TaskSearcher) getElementAt(index);
	}

	@Override
	public Object getElementAt(int index) {
		return TaskSearcherFactory.getInstance().get(index);
	}

	@Override
	public int getSize() {
		return TaskSearcherFactory.getInstance().size();
	}

	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.fireIntervalAdded(this, event.getIndex(), event.getIndex());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.fireIntervalRemoved(this, event.getIndex(), event.getIndex());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(TaskSearcher.PROP_TITLE) ||
				event.getPropertyName().equals(TaskSearcher.PROP_ICON)) {
			int index = TaskSearcherFactory.getInstance().getIndexOf((TaskSearcher) event.getSource());
			this.fireContentsChanged(this, index, index);
		}
	}

}
