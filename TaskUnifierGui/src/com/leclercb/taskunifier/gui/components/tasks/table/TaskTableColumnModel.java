package com.leclercb.taskunifier.gui.components.tasks.table;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class TaskTableColumnModel extends TUTableColumnModel<TaskColumn> {
	
	public TaskTableColumnModel(TUTableProperties<TaskColumn> tableProperties) {
		super(tableProperties);
	}
	
	@Override
	public TUTableColumn<TaskColumn> newTableColumnInstance(
			TableColumnProperties<TaskColumn> column) {
		return new TaskTableColumn(column);
	}
	
}
