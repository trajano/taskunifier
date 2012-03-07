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
package com.leclercb.taskunifier.gui.components.tasktasks;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskList.TaskItem;
import com.leclercb.taskunifier.gui.swing.table.TUColumn;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum TaskTasksColumn implements TUColumn<TaskItem> {
	
	LINK(String.class, Translations.getString("general.task.link"), true),
	TASK(Task.class, Translations.getString("general.task"), true),
	EDIT(Void.class, Translations.getString("general.edit"), false),
	SELECT(Void.class, Translations.getString("general.select"), false);
	
	private Class<?> type;
	private String label;
	private boolean editable;
	
	private TaskTasksColumn(Class<?> type, String label, boolean editable) {
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
	}
	
	@Override
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	@Override
	public Object getProperty(TaskItem item) {
		if (item == null)
			return null;
		
		switch (this) {
			case LINK:
				return item.getLink();
			case TASK:
				return item.getTask();
			case EDIT:
				return null;
			case SELECT:
				return null;
			default:
				return null;
		}
	}
	
	@Override
	public void setProperty(TaskItem item, Object value) {
		if (item == null)
			return;
		
		switch (this) {
			case LINK:
				item.setLink((String) value);
				break;
			case TASK:
				item.setTask((Task) value);
				break;
			case EDIT:
				break;
			case SELECT:
				break;
		}
	}
	
}
