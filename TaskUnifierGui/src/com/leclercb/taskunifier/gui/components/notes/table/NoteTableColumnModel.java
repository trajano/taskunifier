package com.leclercb.taskunifier.gui.components.notes.table;

import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class NoteTableColumnModel extends TUTableColumnModel<NoteColumn> {
	
	public NoteTableColumnModel(TUTableProperties<NoteColumn> tableProperties) {
		super(tableProperties);
	}
	
	@Override
	public TUTableColumn<NoteColumn> newTableColumnInstance(
			TableColumnProperties<NoteColumn> column) {
		return new NoteTableColumn(column);
	}
	
}
