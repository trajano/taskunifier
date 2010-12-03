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

import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.images.Images;

public class StarRenderer extends DefaultRenderer {

	public StarRenderer() {

	}

	@Override
	protected void setValue(Object value) {
		if (value == null || !(value instanceof Boolean)) {
			setText("");
			return;
		}

		if ((Boolean) value)
			setIcon(Images.getImage("checkbox_star_selected.gif"));
		else
			setIcon(Images.getImage("checkbox_star.gif"));

		setHorizontalAlignment(SwingConstants.CENTER);
		setText("");
	}

}
