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
package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Component;
import java.util.Calendar;

import javax.swing.JComponent;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.ToolTipHighlighter;

import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.Timer;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskLength;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskProgress;
import com.leclercb.taskunifier.gui.commons.values.StringValueTimer;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskTooltipHighlighter extends ToolTipHighlighter {
	
	public TaskTooltipHighlighter(HighlightPredicate predicate) {
		super(predicate);
	}
	
	@Override
	protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
		TaskColumn column = (TaskColumn) adapter.getColumnIdentifierAt(adapter.convertColumnIndexToModel(adapter.column));
		
		switch (column) {
			case CONTACTS:
				return this.doHighlightContacts(renderer, adapter);
			case TASKS:
				return this.doHighlightTasks(renderer, adapter);
			case FILES:
				return this.doHighlightFiles(renderer, adapter);
			case PROGRESS:
				return this.doHighlightProgress(renderer, adapter);
			case LENGTH:
				return this.doHighlightLength(renderer, adapter);
			case TIMER:
				return this.doHighlightTimer(renderer, adapter);
			case START_DATE:
				return this.doHighlightDate(renderer, adapter, column);
			case DUE_DATE:
				return this.doHighlightDate(renderer, adapter, column);
			default:
				return super.doHighlight(renderer, adapter);
		}
	}
	
	protected Component doHighlightContacts(
			Component renderer,
			ComponentAdapter adapter) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return renderer;
		
		final Task task = (Task) value;
		
		if (task.getContacts().size() != 0)
			((JComponent) renderer).setToolTipText(task.getContacts().toString());
		
		return renderer;
	}
	
	protected Component doHighlightTasks(
			Component renderer,
			ComponentAdapter adapter) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return renderer;
		
		final Task task = (Task) value;
		
		if (task.getTasks().size() != 0)
			((JComponent) renderer).setToolTipText(task.getTasks().toString());
		
		return renderer;
	}
	
	protected Component doHighlightFiles(
			Component renderer,
			ComponentAdapter adapter) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return renderer;
		
		final Task task = (Task) value;
		
		if (task.getFiles().size() != 0)
			((JComponent) renderer).setToolTipText(task.getFiles().toString());
		
		return renderer;
	}
	
	protected Component doHighlightProgress(
			Component renderer,
			ComponentAdapter adapter) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return renderer;
		
		final Task task = (Task) value;
		
		int nbChildren = 0;
		double progress = 0;
		for (Task child : task.getAllChildren()) {
			if (!child.getModelStatus().isEndUserStatus())
				continue;
			
			nbChildren++;
			
			if (child.isCompleted())
				progress += 1;
			else
				progress += child.getProgress();
		}
		
		String tooltip = null;
		
		if (nbChildren > 0)
			tooltip = String.format(
					"%1s (%2s: %3s)",
					StringValueTaskProgress.INSTANCE.getString(task.getProgress()),
					Translations.getString("general.subtasks"),
					StringValueTaskProgress.INSTANCE.getString(progress
							/ nbChildren));
		else
			tooltip = StringValueTaskProgress.INSTANCE.getString(task.getProgress());
		
		((JComponent) renderer).setToolTipText(tooltip);
		
		return renderer;
	}
	
	protected Component doHighlightLength(
			Component renderer,
			ComponentAdapter adapter) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return renderer;
		
		final Task task = (Task) value;
		
		boolean atLeastOneChild = false;
		int length = task.getLength();
		for (Task child : task.getAllChildren()) {
			if (!child.getModelStatus().isEndUserStatus())
				continue;
			
			if (child.isCompleted())
				continue;
			
			atLeastOneChild = true;
			length += child.getLength();
		}
		
		String tooltip = null;
		
		if (atLeastOneChild)
			tooltip = String.format(
					"%1s (%2s: %3s)",
					StringValueTaskLength.INSTANCE.getString(task.getLength()),
					Translations.getString("general.total"),
					StringValueTaskLength.INSTANCE.getString(length));
		else
			tooltip = StringValueTaskLength.INSTANCE.getString(task.getLength());
		
		((JComponent) renderer).setToolTipText(tooltip);
		
		return renderer;
	}
	
	protected Component doHighlightTimer(
			Component renderer,
			ComponentAdapter adapter) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return renderer;
		
		final Task task = (Task) value;
		
		boolean hasChildren = false;
		long timer = task.getTimer().getTimerValue();
		for (Task child : task.getAllChildren()) {
			if (!child.getModelStatus().isEndUserStatus())
				continue;
			
			if (child.isCompleted())
				continue;
			
			hasChildren = true;
			timer += child.getTimer().getTimerValue();
		}
		
		String tooltip = null;
		
		if (hasChildren)
			tooltip = String.format(
					"%1s (%2s: %3s)",
					StringValueTimer.INSTANCE.getString(task.getTimer()),
					Translations.getString("general.total"),
					StringValueTimer.INSTANCE.getString(new Timer(timer)));
		else
			tooltip = StringValueTimer.INSTANCE.getString(task.getTimer());
		
		((JComponent) renderer).setToolTipText(tooltip);
		
		return renderer;
	}
	
	protected Component doHighlightDate(
			Component renderer,
			ComponentAdapter adapter,
			TaskColumn column) {
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.convertColumnIndexToModel(adapter.column));
		if (value == null || !(value instanceof Calendar))
			return renderer;
		
		String toolTip = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
		
		if (column == TaskColumn.START_DATE)
			if (!Main.getSettings().getBooleanProperty("date.use_start_time"))
				toolTip = StringValueCalendar.INSTANCE_DATE.getString(value);
		
		if (column == TaskColumn.DUE_DATE)
			if (!Main.getSettings().getBooleanProperty("date.use_due_time"))
				toolTip = StringValueCalendar.INSTANCE_DATE.getString(value);
		
		toolTip = String.format("%1s (%2s)", toolTip, Translations.getString(
				"date.x_days",
				Math.abs((int) DateUtils.getDiffInDays(
						(Calendar) value,
						Calendar.getInstance(),
						false))));
		
		((JComponent) renderer).setToolTipText(toolTip);
		
		return renderer;
	}
	
}
