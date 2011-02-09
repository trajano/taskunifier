package com.leclercb.taskunifier.gui.components.searcherlist.items;

import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.taskunifier.gui.components.searcherlist.TaskSearcherElement;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class SearcherItem extends SourceListItem implements TaskSearcherElement {
	
	private TaskSearcher searcher;
	
	public SearcherItem(TaskSearcher searcher) {
		super(
				searcher.getTitle(),
				(searcher.getIcon() == null ? null : Images.getImage(
						searcher.getIcon(),
						16,
						16)));
		this.searcher = searcher;
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return this.searcher;
	}
	
}
