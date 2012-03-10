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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionAddTaskSearcherSelectedTasks extends AbstractViewAction {
	
	public ActionAddTaskSearcherSelectedTasks() {
		this(32, 32);
	}
	
	public ActionAddTaskSearcherSelectedTasks(int width, int height) {
		super(
				Translations.getString("action.add_task_searcher_selected_tasks"),
				ImageUtils.getResourceImage("add.png", width, height),
				ViewType.CALENDAR,
				ViewType.TASKS);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_task_searcher_selected_tasks"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		TaskFilter filter = new TaskFilter();
		filter.setLink(FilterLink.OR);
		
		Task[] tasks = ViewUtils.getSelectedTasks();
		for (Task task : tasks) {
			filter.addElement(new TaskFilterElement(
					TaskColumn.MODEL,
					ModelCondition.EQUALS,
					task));
		}
		
		TaskSearcher searcher = TaskSearcherFactory.getInstance().create(
				TaskSearcherType.PERSONAL,
				Integer.MAX_VALUE,
				Translations.getString("searcher.default.title"),
				filter,
				Constants.getDefaultTaskSorter());
		
		ViewType type = ViewUtils.getCurrentViewType();
		
		switch (type) {
			case TASKS:
				ViewUtils.getCurrentTaskView().getTaskSearcherView().selectTaskSearcher(
						searcher);
			case CALENDAR:
				ViewUtils.getCurrentCalendarView().getTaskSearcherView().selectTaskSearcher(
						searcher);
		}
		
		ActionEditTaskSearcher.editTaskSearcher(searcher);
	}
	
}
