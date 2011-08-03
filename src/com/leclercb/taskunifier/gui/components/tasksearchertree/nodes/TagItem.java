/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.tasksearchertree.nodes;

import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TagItem extends DefaultMutableTreeNode implements SearcherNode {
	
	private TaskSearcher searcher;
	private BadgeCount badgeCount;
	
	public TagItem(Tag tag) {
		super(tag);
		
		CheckUtils.isNotNull(tag, "Tag cannot be null");
		
		this.initializeTaskSearcher();
		this.updateBadgeCount();
	}
	
	public Tag getTag() {
		return (Tag) this.getUserObject();
	}
	
	private void initializeTaskSearcher() {
		final TaskTemplate template = new TaskTemplate("TagTemplate");
		template.setTaskTags(this.getTag().toString());
		
		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.TAGS,
				StringCondition.CONTAINS,
				this.getTag().toString()));
		
		this.searcher = new TaskSearcher(
				TaskSearcherType.TAG,
				0,
				this.getTag().toString(),
				null,
				filter,
				Constants.getDefaultTaskSorter(),
				template);
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return this.searcher;
	}
	
	@Override
	public Icon getIcon() {
		return Images.getResourceImage("transparent.png", 16, 16);
	}
	
	@Override
	public String getText() {
		return this.getTag().toString();
	}
	
	@Override
	public void updateBadgeCount() {
		if (!Main.SETTINGS.getBooleanProperty("tasksearcher.show_badges")) {
			this.badgeCount = null;
			return;
		}
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		boolean useDueTime = Main.SETTINGS.getBooleanProperty("date.use_due_time");
		
		int count = 0;
		int countOverdue = 0;
		for (Task task : tasks) {
			if (TaskUtils.badgeTask(task, searcher.getFilter())) {
				count++;
				
				if (!task.isCompleted() && task.isOverDue(!useDueTime))
					countOverdue++;
			}
		}
		
		this.badgeCount = new BadgeCount(count, countOverdue);
	}
	
	@Override
	public BadgeCount getBadgeCount() {
		return this.badgeCount;
	}
	
	@Override
	public boolean getAllowsChildren() {
		return false;
	}
	
}
