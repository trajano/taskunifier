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
package com.leclercb.taskunifier.gui.components.searchertree.nodes;

import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TaskUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TagItem extends DefaultMutableTreeNode implements SearcherNode {
	
	private TaskSearcher searcher;
	
	public TagItem(String tag) {
		super(tag);
		
		CheckUtils.isNotNull(tag, "Tag cannot be null");
		
		this.initializeTaskSearcher();
	}
	
	public String getTag() {
		return (String) this.getUserObject();
	}
	
	private void initializeTaskSearcher() {
		final Template template = new Template("TagTemplate");
		template.setTaskTags(this.getTag());
		
		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				TaskColumn.TAGS,
				StringCondition.CONTAINS,
				this.getTag()));
		
		this.searcher = new TaskSearcher(
				TaskSearcherType.TAG,
				0,
				this.getTag(),
				null,
				filter,
				Constants.getDefaultSorter(),
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
		return this.getTag();
	}
	
	@Override
	public BadgeCount getBadgeCount() {
		Boolean showBadges = Main.SETTINGS.getBooleanProperty("searcher.show_badges");
		if (showBadges == null || !showBadges)
			return null;
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		int count = 0;
		int countOverdue = 0;
		for (Task task : tasks) {
			if (TaskUtils.showTask(task, searcher.getFilter())) {
				count++;
				
				if (!task.isCompleted() && task.isOverDue())
					countOverdue++;
			}
		}
		
		return new BadgeCount(count, countOverdue);
	}
	
	@Override
	public boolean getAllowsChildren() {
		return false;
	}
	
}
