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
package com.leclercb.taskunifier.gui.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelStatus;

abstract class AbstractModelListModel extends DefaultListModel implements ModelListModel, ListChangeListener, PropertyChangeListener {

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
		if (!((Model) event.getSource()).getModelStatus().equals(ModelStatus.LOADED)
				&& !((Model) event.getSource()).getModelStatus().equals(ModelStatus.TO_UPDATE)) {
			this.removeElement(event.getSource());
		} else {
			int index = this.indexOf(event.getSource());

			if (index == -1)
				this.addElement(event.getSource());
			else
				this.fireContentsChanged(this, index, index);
		}
	}

}
