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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXTable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ContactList;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.components.taskcontacts.TaskContactsColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;

public class TaskContactsTable extends JXTable {
	
	private TUTableProperties<TaskContactsColumn> tableProperties;
	
	public TaskContactsTable(
			TUTableProperties<TaskContactsColumn> tableProperties) {
		CheckUtils.isNotNull(tableProperties);
		this.tableProperties = tableProperties;
		
		this.initialize();
	}
	
	public ContactList getContactGroup() {
		TaskContactsTableModel model = (TaskContactsTableModel) this.getModel();
		return model.getContactGroup();
	}
	
	public void setContactGroup(ContactList contacts) {
		this.commitChanges();
		TaskContactsTableModel model = (TaskContactsTableModel) this.getModel();
		model.setContactGroup(contacts);
	}
	
	public int getContactItemCount() {
		return this.getRowCount();
	}
	
	public ContactItem getContactItem(int row) {
		try {
			int index = this.getRowSorter().convertRowIndexToModel(row);
			return ((TaskContactsTableModel) this.getModel()).getContactItem(index);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}
	
	public ContactItem[] getSelectedContactItems() {
		int[] indexes = this.getSelectedRows();
		
		List<ContactItem> items = new ArrayList<ContactItem>();
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1) {
				ContactItem item = this.getContactItem(indexes[i]);
				
				if (item != null)
					items.add(item);
			}
		}
		
		return items.toArray(new ContactItem[0]);
	}
	
	public void commitChanges() {
		if (this.getCellEditor() != null)
			this.getCellEditor().stopCellEditing();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		TaskContactsTableColumnModel columnModel = new TaskContactsTableColumnModel(
				this.tableProperties);
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
		this.setSortOrderCycle(SortOrder.ASCENDING, SortOrder.DESCENDING);
		this.setColumnControlVisible(true);
		this.setSortOrder(TaskContactsColumn.LINK, SortOrder.ASCENDING);
		
		this.initializeHighlighters();
	}
	
	private void initializeHighlighters() {
		this.setHighlighters(new AlternateHighlighter());
	}
	
}
