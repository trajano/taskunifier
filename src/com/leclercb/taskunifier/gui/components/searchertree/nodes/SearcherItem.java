package com.leclercb.taskunifier.gui.components.searchertree.nodes;

import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class SearcherItem extends DefaultMutableTreeNode implements SearcherNode {
	
	private Icon icon;
	
	public SearcherItem(TaskSearcher searcher) {
		super(searcher);
		
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
		
		if (searcher.getIcon() == null)
			this.icon = null;
		else
			this.icon = Images.getImage(searcher.getIcon(), 16, 16);
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return (TaskSearcher) this.getUserObject();
	}
	
	@Override
	public Icon getIcon() {
		return this.icon;
	}
	
	@Override
	public String getText() {
		return this.getTaskSearcher().getTitle();
	}
	
	@Override
	public String getBadge() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		int count = 0;
		for (Task task : tasks)
			if (TaskUtils.showTask(task, searcher.getFilter()))
				count++;
		
		return "<html>" + count + "</html>";
	}
	
}
