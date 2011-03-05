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
package com.leclercb.taskunifier.gui.components.tasks.table.renderers;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.GuiModel;
import com.leclercb.taskunifier.gui.swing.ColorBadgeIcon;

public class ModelRenderer extends DefaultRenderer {
	
	public ModelRenderer() {
		
	}
	
	@Override
	public void setValue(Object value) {
		if (!(value instanceof Model)) {
			this.setText("");
			this.setIcon(new ColorBadgeIcon(null, 12, 12));
			return;
		}
		
		this.setText(((Model) value).getTitle());
		
		if (value instanceof GuiModel)
			this.setIcon(new ColorBadgeIcon(
					((GuiModel) value).getColor(),
					12,
					12));
		else
			this.setIcon(new ColorBadgeIcon(null, 12, 12));
	}
	
}
