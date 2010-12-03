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

import java.util.List;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.ModelStatus;

public class ContextListModel extends AbstractModelListModel {

	public ContextListModel() {
		this.addElement(null);

		List<Context> contexts = ContextFactory.getInstance().getList();
		for (Context context : contexts)
			if (context.getModelStatus().equals(ModelStatus.LOADED) ||
					context.getModelStatus().equals(ModelStatus.TO_UPDATE))
				this.addElement(context);

		ContextFactory.getInstance().addListChangeListener(this);
		ContextFactory.getInstance().addPropertyChangeListener(this);
	}

}
