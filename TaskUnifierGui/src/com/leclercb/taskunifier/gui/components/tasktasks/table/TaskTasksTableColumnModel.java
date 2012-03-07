package com.leclercb.taskunifier.gui.components.tasktasks.table;

import com.leclercb.taskunifier.gui.components.tasktasks.TaskTasksColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class TaskTasksTableColumnModel extends TUTableColumnModel<TaskTasksColumn> {
	
	public TaskTasksTableColumnModel(
			TUTableProperties<TaskTasksColumn> tableProperties) {
		super(tableProperties);
	}
	
	@Override
	public TUTableColumn<TaskTasksColumn> newTableColumnInstance(
			TableColumnProperties<TaskTasksColumn> column) {
		return new TaskTasksTableColumn(column);
	}
	
}
