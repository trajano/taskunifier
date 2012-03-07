package com.leclercb.taskunifier.gui.components.taskcontacts.table;

import com.leclercb.taskunifier.gui.components.taskcontacts.TaskContactsColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class TaskContactsTableColumnModel extends TUTableColumnModel<TaskContactsColumn> {
	
	public TaskContactsTableColumnModel(
			TUTableProperties<TaskContactsColumn> tableProperties) {
		super(tableProperties);
	}
	
	@Override
	public TUTableColumn<TaskContactsColumn> newTableColumnInstance(
			TableColumnProperties<TaskContactsColumn> column) {
		return new TaskContactsTableColumn(column);
	}
	
}
