/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskUndoableEdit extends AbstractUndoableEdit {

	private Task task;
	private TaskColumn column;
	private Object newValue;
	private Object oldValue;

	public TaskUndoableEdit(Task task, TaskColumn column, Object newValue,
			Object oldValue) {
		CheckUtils.isNotNull(task, "Task cannot be null");
		CheckUtils.isNotNull(column, "Column cannot be null");

		this.task = task;
		this.column = column;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	@Override
	public String getPresentationName() {
		return "Cell Edit";
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();

		if (this.column.equals(TaskColumn.TITLE))
			this.task.setTitle((String) this.oldValue);
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();

		if (this.column.equals(TaskColumn.TITLE))
			this.task.setTitle((String) this.newValue);
	}

}
