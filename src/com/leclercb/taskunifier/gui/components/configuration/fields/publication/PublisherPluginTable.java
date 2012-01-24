package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.MappedValue;

import com.leclercb.taskunifier.gui.commons.values.BooleanValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.IconValueCompleted;
import com.leclercb.taskunifier.gui.components.configuration.fields.publication.editors.OptionsEditor;
import com.leclercb.taskunifier.gui.components.configuration.fields.publication.renderers.OptionsRenderer;

public class PublisherPluginTable extends JXTable {
	
	private static final TableCellRenderer BOOLEAN_RENDERER;
	private static final TableCellRenderer GENERIC_RENDERER;
	private static final TableCellRenderer OPTIONS_RENDERER;
	
	private static final TableCellEditor BOOLEAN_EDITOR;
	private static final TableCellEditor OPTIONS_EDITOR;
	
	static {
		// RENDERERS
		BOOLEAN_RENDERER = new DefaultTableRenderer(new MappedValue(
				null,
				IconValueCompleted.INSTANCE,
				BooleanValueBoolean.INSTANCE), SwingConstants.CENTER);
		GENERIC_RENDERER = new DefaultTableCellRenderer();
		OPTIONS_RENDERER = new OptionsRenderer();
		
		// EDITORS
		BOOLEAN_EDITOR = new BooleanEditor();
		OPTIONS_EDITOR = new OptionsEditor();
	}
	
	public PublisherPluginTable() {
		this.initialize();
	}
	
	public void refresh() {
		this.getPublisherPluginModel().fireTableDataChanged();
	}
	
	public PublisherPluginModel getPublisherPluginModel() {
		return (PublisherPluginModel) this.getModel();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		PublisherPluginModel tableModel = new PublisherPluginModel();
		
		this.setModel(tableModel);
		this.getTableHeader().setReorderingAllowed(false);
		this.setShowGrid(true, false);
		
		this.setSortable(false);
		this.setSortsOnUpdates(true);
		this.setSortOrder(1, SortOrder.ASCENDING);
		this.setColumnControlVisible(false);
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int col) {
		switch (col) {
			case 0:
				return BOOLEAN_EDITOR;
			case 1:
				return super.getCellEditor(row, col);
			case 2:
				return OPTIONS_EDITOR;
			default:
				return super.getCellEditor(row, col);
		}
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		switch (col) {
			case 0:
				return BOOLEAN_RENDERER;
			case 1:
				return GENERIC_RENDERER;
			case 2:
				return OPTIONS_RENDERER;
			default:
				return super.getCellRenderer(row, col);
		}
	}
	
}
