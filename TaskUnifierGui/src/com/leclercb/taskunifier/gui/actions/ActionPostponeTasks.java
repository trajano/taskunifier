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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.components.views.interfaces.TaskSelectionView;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.TaskPostponeList.PostponeItem;

public class ActionPostponeTasks extends AbstractViewAction {
	
	private TaskSelectionView view;
	private PostponeType type;
	private PostponeItem item;
	
	public ActionPostponeTasks(
			int width,
			int height,
			TaskSelectionView view,
			PostponeType type,
			PostponeItem item) {
		super(item.toString(), ImageUtils.getResourceImage(
				"calendar.png",
				width,
				height), generateViewTypes(view));
		
		CheckUtils.isNotNull(type);
		CheckUtils.isNotNull(item);
		
		this.view = view;
		this.type = type;
		this.item = item;
		
		this.putValue(SHORT_DESCRIPTION, item.toString());
	}
	
	public PostponeType getType() {
		return this.type;
	}
	
	public PostponeItem getItem() {
		return this.item;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Task[] tasks = null;
		
		if (this.view != null)
			tasks = this.view.getSelectedTasks();
		else
			tasks = ViewUtils.getSelectedTasks();
		
		postponeTasks(tasks, this.type, this.item);
	}
	
	public static void postponeTasks(
			Task[] tasks,
			PostponeType type,
			PostponeItem item) {
		CheckUtils.isNotNull(tasks);
		CheckUtils.isNotNull(type);
		CheckUtils.isNotNull(item);
		
		boolean fromCurrentDate = Main.getSettings().getBooleanProperty(
				"task.postpone_from_current_date");
		
		if (type == PostponeType.BOTH)
			fromCurrentDate = false;
		
		if (type == PostponeType.START_DATE || type == PostponeType.BOTH) {
			for (Task task : tasks) {
				Calendar newStartDate = task.getStartDate();
				
				if (newStartDate == null)
					newStartDate = Calendar.getInstance();
				
				if (fromCurrentDate
						|| (item.getField() == Calendar.DAY_OF_MONTH && item.getAmount() == 0)) {
					Calendar now = Calendar.getInstance();
					newStartDate.set(
							now.get(Calendar.YEAR),
							now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH));
				}
				
				newStartDate.add(item.getField(), item.getAmount());
				
				task.setStartDate(newStartDate);
			}
		}
		
		if (type == PostponeType.DUE_DATE || type == PostponeType.BOTH) {
			for (Task task : tasks) {
				Calendar newDueDate = task.getDueDate();
				
				if (newDueDate == null)
					newDueDate = Calendar.getInstance();
				
				if (fromCurrentDate
						|| (item.getField() == Calendar.DAY_OF_MONTH && item.getAmount() == 0)) {
					Calendar now = Calendar.getInstance();
					newDueDate.set(
							now.get(Calendar.YEAR),
							now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH));
				}
				
				newDueDate.add(item.getField(), item.getAmount());
				
				task.setDueDate(newDueDate);
			}
		}
		
		ViewUtils.refreshTasks();
	}
	
	public static ActionPostponeTasks[] createDefaultActions(
			int width,
			int height,
			TaskSelectionView view,
			PostponeType type) {
		List<ActionPostponeTasks> actions = new ArrayList<ActionPostponeTasks>();
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.DAY_OF_MONTH, 0)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.DAY_OF_MONTH, 1)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.DAY_OF_MONTH, 2)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.DAY_OF_MONTH, 3)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.WEEK_OF_YEAR, 1)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.WEEK_OF_YEAR, 2)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.WEEK_OF_YEAR, 3)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.MONTH, 1)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.MONTH, 2)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.MONTH, 3)));
		
		actions.add(new ActionPostponeTasks(
				width,
				height,
				view,
				type,
				new PostponeItem(Calendar.YEAR, 1)));
		
		return actions.toArray(new ActionPostponeTasks[0]);
	}
	
	private static ViewType[] generateViewTypes(TaskSelectionView view) {
		if (view != null)
			return new ViewType[0];
		
		return new ViewType[] { ViewType.TASKS, ViewType.CALENDAR };
	}
	
}
