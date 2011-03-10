package com.leclercb.taskunifier.gui.components.searcherlist.items;

import java.util.List;

import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.components.searcherlist.TaskSearcherElement;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

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
		this.updateBadgeCount();
	}
	
	public void updateBadgeCount() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		int count = 0;
		for (Task task : tasks)
			if (TaskUtils.showTask(task, searcher.getFilter()))
				count++;
		
		this.setCounterValue(count);
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return this.searcher;
	}
	
}
