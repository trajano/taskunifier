package com.leclercb.taskunifier.gui.components.taskfiles.table;

import com.leclercb.taskunifier.gui.components.taskfiles.TaskFilesColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class TaskFilesTableColumnModel extends TUTableColumnModel<TaskFilesColumn> {
	
	public TaskFilesTableColumnModel(
			TUTableProperties<TaskFilesColumn> tableProperties) {
		super(tableProperties);
	}
	
	@Override
	public TUTableColumn<TaskFilesColumn> newTableColumnInstance(
			TableColumnProperties<TaskFilesColumn> column) {
		return new TaskFilesTableColumn(column);
	}
	
}
