package com.leclercb.taskunifier.gui.components.taskcontacts.table;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXTable;

import com.leclercb.taskunifier.api.models.ContactList;

public class TaskContactsTable extends JXTable {
	
	public TaskContactsTable() {
		this.initialize();
	}
	
	public ContactList getContactList() {
		TaskContactsTableModel model = (TaskContactsTableModel) this.getModel();
		return model.getContactList();
	}
	
	public void setContactList(ContactList contacts) {
		this.commitChanges();
		TaskContactsTableModel model = (TaskContactsTableModel) this.getModel();
		model.setContactList(contacts);
	}
	
	public void commitChanges() {
		if (this.getCellEditor() != null)
			this.getCellEditor().stopCellEditing();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		TaskContactsTableColumnModel columnModel = new TaskContactsTableColumnModel();
		TaskContactsTableModel tableModel = new TaskContactsTableModel();
		
		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(24);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setShowGrid(true, false);
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		
		this.setSortable(true);
		this.setSortsOnUpdates(false);
		this.setSortOrderCycle(SortOrder.ASCENDING);
		this.setColumnControlVisible(true);
	}
	
}
