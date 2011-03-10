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
package com.leclercb.taskunifier.gui.commons.transfer;

import java.io.Serializable;
import java.util.Arrays;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;

public class ModelTransferData implements Serializable {
	
	private ModelType type;
	private ModelId[] ids;
	
	public ModelTransferData(ModelType type, ModelId id) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		CheckUtils.isNotNull(id, "ID cannot be null");
		
		this.type = type;
		this.ids = new ModelId[] { id };
	}
	
	public ModelTransferData(ModelType type, ModelId[] ids) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		CheckUtils.isNotNull(ids, "IDs cannot be null");
		
		this.type = type;
		this.ids = Arrays.copyOf(ids, ids.length);
	}
	
	public ModelType getType() {
		return this.type;
	}
	
	public ModelId[] getIds() {
		return this.ids;
	}
	
}
