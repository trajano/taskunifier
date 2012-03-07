package com.leclercb.taskunifier.gui.components.modelselectiontable.table;

import com.leclercb.taskunifier.gui.components.modelselectiontable.ModelSelectionColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class ModelSelectionTableColumnModel extends TUTableColumnModel<ModelSelectionColumn> {
	
	public ModelSelectionTableColumnModel(
			TUTableProperties<ModelSelectionColumn> tableProperties) {
		super(tableProperties);
	}
	
	@Override
	public TUTableColumn<ModelSelectionColumn> newTableColumnInstance(
			TableColumnProperties<ModelSelectionColumn> column) {
		return new ModelSelectionTableColumn(column);
	}
	
}
