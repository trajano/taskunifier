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
package com.leclercb.taskunifier.gui.components.taskfiles.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.FileGroup;
import com.leclercb.taskunifier.api.models.FileGroup.FileItem;
import com.leclercb.taskunifier.gui.components.taskfiles.TaskFilesColumn;

public class TaskFilesTableModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {
	
	private FileGroup files;
	
	public TaskFilesTableModel() {
		this.files = new FileGroup();
	}
	
	public FileGroup getFileGroup() {
		return this.files;
	}
	
	public void setFileGroup(FileGroup files) {
		if (EqualsUtils.equals(this.files, files))
			return;
		
		if (this.files != null) {
			this.files.removeListChangeListener(this);
			this.files.removePropertyChangeListener(this);
		}
		
		this.files = files;
		
		if (this.files != null) {
			this.files.addListChangeListener(this);
			this.files.addPropertyChangeListener(this);
		}
		
		this.fireTableDataChanged();
	}
	
	public FileItem getFileItem(int row) {
		return this.files.get(row);
	}
	
	public TaskFilesColumn getTaskFilesColumn(int col) {
		return TaskFilesColumn.values()[col];
	}
	
	@Override
	public int getColumnCount() {
		return TaskFilesColumn.values().length;
	}
	
	@Override
	public int getRowCount() {
		if (this.files == null)
			return 0;
		
		return this.files.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return TaskFilesColumn.values()[col].getLabel();
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return TaskFilesColumn.values()[col].getType();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		FileItem item = this.files.get(row);
		return TaskFilesColumn.values()[col].getProperty(item);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return TaskFilesColumn.values()[col].isEditable();
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		FileItem item = this.files.get(row);
		TaskFilesColumn column = TaskFilesColumn.values()[col];
		
		Object oldValue = column.getProperty(item);
		
		if (!EqualsUtils.equals(oldValue, value)) {
			column.setProperty(item, value);
		}
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.fireTableRowsInserted(event.getIndex(), event.getIndex());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.fireTableRowsDeleted(event.getIndex(), event.getIndex());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		int index = this.files.getIndexOf((FileItem) event.getSource());
		this.fireTableRowsUpdated(index, index);
	}
	
}
