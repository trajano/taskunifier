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
