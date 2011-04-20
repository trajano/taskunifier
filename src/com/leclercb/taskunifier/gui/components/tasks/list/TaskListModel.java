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
package com.leclercb.taskunifier.gui.components.tasks.list;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskListModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {
	
	public TaskListModel() {
		TaskFactory.getInstance().addListChangeListener(this);
		TaskFactory.getInstance().addPropertyChangeListener(this);
		
		Synchronizing.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!(Boolean) evt.getNewValue())
					TaskListModel.this.fireTableDataChanged();
			}
			
		});
	}
	
	public Task getTask(int row) {
		return TaskFactory.getInstance().get(row);
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public int getRowCount() {
		return TaskFactory.getInstance().size();
	}
	
	@Override
	public String getColumnName(int col) {
		return Translations.getString("general.tasks");
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return Task.class;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		return this.getTask(row);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {

	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (Synchronizing.isSynchronizing())
			return;
		
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.fireTableRowsInserted(event.getIndex(), event.getIndex());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.fireTableRowsDeleted(event.getIndex(), event.getIndex());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (Synchronizing.isSynchronizing())
			return;
		
		if (event.getPropertyName().equals(Model.PROP_MODEL_STATUS)
				|| event.getPropertyName().equals(Task.PROP_PARENT)) {
			this.fireTableDataChanged();
		} else {
			int index = TaskFactory.getInstance().getIndexOf(
					(Task) event.getSource());
			this.fireTableRowsUpdated(index, index);
		}
	}
	
}
