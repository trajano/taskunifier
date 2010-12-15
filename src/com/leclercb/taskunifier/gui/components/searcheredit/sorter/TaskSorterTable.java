package com.leclercb.taskunifier.gui.components.searcheredit.sorter;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.leclercb.taskunifier.gui.components.searcheredit.sorter.renderers.TaskSorterSortOrderRenderer;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.renderers.SortOrderListCellRenderer;
import com.leclercb.taskunifier.gui.searchers.TaskSorter;

public class TaskSorterTable extends JTable {

	private static final DefaultTableCellRenderer ORDER_RENDERER;
	private static final DefaultTableCellRenderer COLUMN_RENDERER;
	private static final DefaultTableCellRenderer SORT_ORDER_RENDERER;

	private static final DefaultCellEditor ORDER_EDITOR;
	private static final DefaultCellEditor COLUMN_EDITOR;
	private static final DefaultCellEditor SORT_ORDER_EDITOR;

	static {
		// RENDERERS
		ORDER_RENDERER = new DefaultTableCellRenderer();
		COLUMN_RENDERER = new DefaultTableCellRenderer();
		SORT_ORDER_RENDERER = new TaskSorterSortOrderRenderer();

		// EDITORS
		ORDER_EDITOR = new DefaultCellEditor(new JTextField());
		COLUMN_EDITOR = new DefaultCellEditor(new JComboBox(TaskColumn.values()));

		JComboBox comboBox = null;

		comboBox = new JComboBox(SortOrder.values());
		comboBox.setRenderer(new SortOrderListCellRenderer());

		SORT_ORDER_EDITOR = new DefaultCellEditor(comboBox);
	}

	public TaskSorterTable(TaskSorter sorter) {
		this.initialize(sorter);
	}

	private void initialize(TaskSorter sorter) {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TaskSorterTableModel tableModel = new TaskSorterTableModel(sorter);

		this.setModel(tableModel);
		this.getTableHeader().setReorderingAllowed(false);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int col) {
		switch (col) {
			case 0:
				return ORDER_EDITOR;
			case 1:
				return COLUMN_EDITOR;
			case 2:
				return SORT_ORDER_EDITOR;
			default:
				return super.getCellEditor(row, col);
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		switch (col) {
			case 0:
				return ORDER_RENDERER;
			case 1:
				return COLUMN_RENDERER;
			case 2:
				return SORT_ORDER_RENDERER;
			default:
				return super.getCellRenderer(row, col);
		}
	}

}
