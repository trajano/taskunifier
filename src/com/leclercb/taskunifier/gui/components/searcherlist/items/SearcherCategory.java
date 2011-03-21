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
package com.leclercb.taskunifier.gui.components.searcherlist.items;

import com.explodingpixels.macwidgets.SourceListCategory;
import com.leclercb.commons.api.utils.CheckUtils;

public class SearcherCategory extends SourceListCategory {
	
	private String expandedPropetyName;
	
	public SearcherCategory(String title, String expandedPropetyName) {
		super(title);
		
		CheckUtils.isNotNull(title, "Title cannot be null");
		
		this.expandedPropetyName = expandedPropetyName;
	}
	
	public String getExpandedPropetyName() {
		return this.expandedPropetyName;
	}
	
}
