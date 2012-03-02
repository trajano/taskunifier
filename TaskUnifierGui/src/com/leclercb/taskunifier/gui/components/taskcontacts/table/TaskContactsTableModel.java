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
package com.leclercb.taskunifier.gui.components.taskcontacts.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.ContactGroup;
import com.leclercb.taskunifier.api.models.ContactGroup.ContactItem;
import com.leclercb.taskunifier.gui.components.taskcontacts.TaskContactsColumn;

public class TaskContactsTableModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {
	
	private ContactGroup contacts;
	
	public TaskContactsTableModel() {
		this.contacts = new ContactGroup();
	}
	
	public ContactGroup getContactGroup() {
		return this.contacts;
	}
	
	public void setContactGroup(ContactGroup contacts) {
		if (EqualsUtils.equals(this.contacts, contacts))
			return;
		
		if (this.contacts != null) {
			this.contacts.removeListChangeListener(this);
			this.contacts.removePropertyChangeListener(this);
		}
		
		this.contacts = contacts;
		
		if (this.contacts != null) {
			this.contacts.addListChangeListener(this);
			this.contacts.addPropertyChangeListener(this);
		}
		
		this.fireTableDataChanged();
	}
	
	public ContactItem getContactItem(int row) {
		return this.contacts.get(row);
	}
	
	public TaskContactsColumn getTaskContactsColumn(int col) {
		return TaskContactsColumn.values()[col];
	}
	
	@Override
	public int getColumnCount() {
		return TaskContactsColumn.values().length;
	}
	
	@Override
	public int getRowCount() {
		if (this.contacts == null)
			return 0;
		
		return this.contacts.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return TaskContactsColumn.values()[col].getLabel();
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return TaskContactsColumn.values()[col].getType();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		ContactItem item = this.contacts.get(row);
		return TaskContactsColumn.values()[col].getProperty(item);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return TaskContactsColumn.values()[col].isEditable();
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		ContactItem item = this.contacts.get(row);
		TaskContactsColumn column = TaskContactsColumn.values()[col];
		
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
		int index = this.contacts.getIndexOf((ContactItem) event.getSource());
		this.fireTableRowsUpdated(index, index);
	}
	
}
