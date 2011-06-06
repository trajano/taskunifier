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
package com.leclercb.taskunifier.gui.components.tasks.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.table.TableColumnExt;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.commons.values.BooleanValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.IconValueCompleted;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueStar;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModelId;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskLength;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeat;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.ContextEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.DateEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.FolderEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.GoalEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LengthEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LocationEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.PriorityEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.ReminderEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.RepeatEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.RepeatFromEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.StatusEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.ShowChildrenRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TitleRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowComparator;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskTableColumn extends TableColumnExt {
	
	private static final TableCellRenderer CALENDAR_RENDERER;
	private static final TableCellRenderer COMPLETED_RENDERER;
	private static final TableCellRenderer LENGTH_RENDERER;
	private static final TableCellRenderer MODEL_ID_RENDERER;
	private static final TableCellRenderer MODEL_RENDERER;
	private static final TableCellRenderer REMINDER_RENDERER;
	private static final TableCellRenderer REPEAT_RENDERER;
	private static final TableCellRenderer SHOW_CHILDREN_RENDERER;
	private static final TableCellRenderer STAR_RENDERER;
	private static final TableCellRenderer TASK_PRIORITY_RENDERER;
	private static final TableCellRenderer TASK_REPEAT_FROM_RENDERER;
	private static final TableCellRenderer TASK_STATUS_RENDERER;
	private static final TableCellRenderer TITLE_RENDERER;
	
	private static final TableCellEditor BOOLEAN_EDITOR;
	private static final TableCellEditor CONTEXT_EDITOR;
	private static final TableCellEditor DATE_EDITOR;
	private static final TableCellEditor FOLDER_EDITOR;
	private static final TableCellEditor GENERIC_EDITOR;
	private static final TableCellEditor GOAL_EDITOR;
	private static final TableCellEditor LENGTH_EDITOR;
	private static final TableCellEditor LOCATION_EDITOR;
	private static final TableCellEditor REMINDER_EDITOR;
	private static final TableCellEditor REPEAT_EDITOR;
	private static final TableCellEditor TASK_PRIORITY_EDITOR;
	private static final TableCellEditor TASK_REPEAT_FROM_EDITOR;
	private static final TableCellEditor TASK_STATUS_EDITOR;
	
	static {
		CALENDAR_RENDERER = new DefaultTableRenderer(new StringValueCalendar());
		
		COMPLETED_RENDERER = new DefaultTableRenderer(new MappedValue(
				null,
				new IconValueCompleted(),
				new BooleanValueBoolean()), SwingConstants.CENTER);
		
		LENGTH_RENDERER = new DefaultTableRenderer(new StringValueTaskLength());
		
		MODEL_ID_RENDERER = new DefaultTableRenderer(new StringValueModelId());
		
		MODEL_RENDERER = new DefaultTableRenderer(new MappedValue(
				new StringValueModel(),
				new IconValueModel()));
		
		REMINDER_RENDERER = new DefaultTableRenderer(
				new StringValueTaskReminder());
		
		REPEAT_RENDERER = new DefaultTableRenderer(new StringValueTaskRepeat());
		
		SHOW_CHILDREN_RENDERER = new ShowChildrenRenderer();
		
		STAR_RENDERER = new DefaultTableRenderer(new MappedValue(
				null,
				new IconValueStar(),
				new BooleanValueBoolean()), SwingConstants.CENTER);
		
		TASK_PRIORITY_RENDERER = new DefaultTableRenderer(new MappedValue(
				new StringValueTaskPriority(),
				new IconValueTaskPriority(),
				null));
		
		TASK_REPEAT_FROM_RENDERER = new DefaultTableRenderer(
				new StringValueTaskRepeatFrom());
		
		TASK_STATUS_RENDERER = new DefaultTableRenderer(
				new StringValueTaskStatus());
		
		TITLE_RENDERER = new TitleRenderer();
		
		BOOLEAN_EDITOR = new JXTable.BooleanEditor();
		CONTEXT_EDITOR = new ContextEditor();
		DATE_EDITOR = new DateEditor();
		FOLDER_EDITOR = new FolderEditor();
		GENERIC_EDITOR = new JXTable.GenericEditor();
		GOAL_EDITOR = new GoalEditor();
		LENGTH_EDITOR = new LengthEditor();
		LOCATION_EDITOR = new LocationEditor();
		REMINDER_EDITOR = new ReminderEditor();
		REPEAT_EDITOR = new RepeatEditor();
		TASK_PRIORITY_EDITOR = new PriorityEditor();
		TASK_REPEAT_FROM_EDITOR = new RepeatFromEditor();
		TASK_STATUS_EDITOR = new StatusEditor();
	}
	
	private TaskColumn taskColumn;
	
	public TaskTableColumn(TaskColumn taskColumn) {
		super(taskColumn.ordinal());
		
		CheckUtils.isNotNull(taskColumn, "Task column cannot be null");
		
		this.taskColumn = taskColumn;
		
		this.setIdentifier(taskColumn);
		this.setHeaderValue(taskColumn.getLabel());
		this.setPreferredWidth(taskColumn.getWidth());
		this.setVisible(taskColumn.isVisible());
		
		this.taskColumn.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(TaskColumn.PROP_VISIBLE)) {
					TaskTableColumn.this.setVisible((Boolean) evt.getNewValue());
				}
				
				if (evt.getPropertyName().equals(TaskColumn.PROP_WIDTH)) {
					TaskTableColumn.this.setPreferredWidth((Integer) evt.getNewValue());
				}
			}
			
		});
	}
	
	@Override
	public Comparator<?> getComparator() {
		if (this.taskColumn == TaskColumn.MODEL)
			return TaskRowComparator.getInstance();
		
		return super.getComparator();
	}
	
	@Override
	public boolean isSortable() {
		if (this.taskColumn == TaskColumn.MODEL)
			return true;
		
		return false;
	}
	
	@Override
	public void setPreferredWidth(int preferredWidth) {
		this.taskColumn.setWidth(preferredWidth);
		super.setPreferredWidth(preferredWidth);
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.taskColumn.setVisible(visible);
		super.setVisible(visible);
	}
	
	@Override
	public TableCellRenderer getCellRenderer() {
		switch (this.taskColumn) {
			case MODEL:
				return MODEL_ID_RENDERER;
			case SHOW_CHILDREN:
				return SHOW_CHILDREN_RENDERER;
			case TITLE:
				return TITLE_RENDERER;
			case COMPLETED:
				return COMPLETED_RENDERER;
			case CONTEXT:
			case FOLDER:
			case GOAL:
			case LOCATION:
				return MODEL_RENDERER;
			case COMPLETED_ON:
			case DUE_DATE:
			case START_DATE:
				return CALENDAR_RENDERER;
			case REMINDER:
				return REMINDER_RENDERER;
			case LENGTH:
				return LENGTH_RENDERER;
			case STAR:
				return STAR_RENDERER;
			case PRIORITY:
				return TASK_PRIORITY_RENDERER;
			case REPEAT:
				return REPEAT_RENDERER;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_RENDERER;
			case STATUS:
				return TASK_STATUS_RENDERER;
			default:
				return super.getCellRenderer();
		}
	}
	
	@Override
	public TableCellEditor getCellEditor() {
		switch (this.taskColumn) {
			case SHOW_CHILDREN:
				return BOOLEAN_EDITOR;
			case TITLE:
				return GENERIC_EDITOR;
			case TAGS:
				return GENERIC_EDITOR;
			case FOLDER:
				return FOLDER_EDITOR;
			case CONTEXT:
				return CONTEXT_EDITOR;
			case GOAL:
				return GOAL_EDITOR;
			case LOCATION:
				return LOCATION_EDITOR;
			case COMPLETED:
				return BOOLEAN_EDITOR;
			case DUE_DATE:
				return DATE_EDITOR;
			case START_DATE:
				return DATE_EDITOR;
			case REPEAT:
				return REPEAT_EDITOR;
			case REMINDER:
				return REMINDER_EDITOR;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_EDITOR;
			case STATUS:
				return TASK_STATUS_EDITOR;
			case LENGTH:
				return LENGTH_EDITOR;
			case PRIORITY:
				return TASK_PRIORITY_EDITOR;
			case STAR:
				return BOOLEAN_EDITOR;
			default:
				return super.getCellEditor();
		}
	}
	
}
