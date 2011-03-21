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
package com.leclercb.taskunifier.gui.commons.converters;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;

public class ModelConverter extends AbstractConverter {
	
	private ModelType type;
	
	public ModelConverter(ModelType type, ValueModel subject) {
		super(subject);
		this.type = type;
	}
	
	@Override
	public void setValue(Object model) {
		this.subject.setValue((model == null ? null : ((Model) model).getModelId()));
	}
	
	@Override
	public Object convertFromSubject(Object value) {
		switch (this.type) {
			case CONTEXT:
				return ContextFactory.getInstance().get((ModelId) value);
			case FOLDER:
				return FolderFactory.getInstance().get((ModelId) value);
			case GOAL:
				return GoalFactory.getInstance().get((ModelId) value);
			case LOCATION:
				return LocationFactory.getInstance().get((ModelId) value);
			default:
				return null;
		}
	}
	
}
