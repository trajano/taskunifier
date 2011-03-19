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
package com.leclercb.taskunifier.gui.commons.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.gui.swing.models.DefaultSortedComboBoxModel;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;

abstract class AbstractModelSortedModel extends DefaultSortedComboBoxModel implements ModelListModel, ListChangeListener, PropertyChangeListener {
	
	public AbstractModelSortedModel() {
		super(new ModelComparator());
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.addElement(event.getValue());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.removeElement(event.getValue());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (!((Model) event.getSource()).getModelStatus().equals(
				ModelStatus.LOADED)
				&& !((Model) event.getSource()).getModelStatus().equals(
						ModelStatus.TO_UPDATE)) {
			this.removeElement(event.getSource());
		} else {
			int index = this.getIndexOf(event.getSource());
			
			if (index == -1)
				this.addElement(event.getSource());
			else
				this.fireContentsChanged(this, index, index);
		}
	}
	
}
