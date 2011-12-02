package com.leclercb.taskunifier.gui.components.taskcontacts.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.ContactList;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.gui.components.taskcontacts.TaskContactsColumn;

public class TaskContactsTableModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {
	
	private ContactList contacts;
	
	public TaskContactsTableModel() {
		this.contacts = new ContactList();
	}
	
	public ContactList getContactList() {
		return this.contacts;
	}
	
	public void setContactList(ContactList contacts) {
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
