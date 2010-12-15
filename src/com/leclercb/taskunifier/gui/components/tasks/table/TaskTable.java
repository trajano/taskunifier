/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.tasks.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.draganddrop.TaskTransferHandler;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.DateEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LengthEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.CalendarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.CheckBoxRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.DefaultRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.LengthRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.StarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskPriorityRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskRepeatFromRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskStatusRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskTitleRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowFilter;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskTableRowSorter;
import com.leclercb.taskunifier.gui.models.ContextComboBoxModel;
import com.leclercb.taskunifier.gui.models.FolderComboBoxModel;
import com.leclercb.taskunifier.gui.models.GoalComboBoxModel;
import com.leclercb.taskunifier.gui.models.LocationComboBoxModel;
import com.leclercb.taskunifier.gui.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskTable extends JTable {

	private static final DefaultRenderer DEFAULT_RENDERER;
	private static final CheckBoxRenderer CHECK_BOX_RENDERER;
	private static final CalendarRenderer DATE_RENDERER;
	private static final LengthRenderer LENGTH_RENDERER;
	private static final TaskTitleRenderer TASK_TITLE_RENDERER;
	private static final StarRenderer STAR_RENDERER;

	private static final TaskPriorityRenderer TASK_PRIORITY_RENDERER;
	private static final TaskRepeatFromRenderer TASK_REPEAT_FROM_RENDERER;
	private static final TaskStatusRenderer TASK_STATUS_RENDERER;

	private static final DefaultCellEditor CHECK_BOX_EDITOR;
	private static final DateEditor DATE_EDITOR;
	private static final LengthEditor LENGTH_EDITOR;
	private static final DefaultCellEditor STAR_EDITOR;

	private static final DefaultCellEditor CONTEXT_EDITOR;
	private static final DefaultCellEditor FOLDER_EDITOR;
	private static final DefaultCellEditor GOAL_EDITOR;
	private static final DefaultCellEditor LOCATION_EDITOR;
	private static final DefaultCellEditor REPEAT_FROM_EDITOR;

	private static final DefaultCellEditor TASK_PRIORITY_EDITOR;
	private static final DefaultCellEditor TASK_REPEAT_EDITOR;
	private static final DefaultCellEditor TASK_STATUS_EDITOR;

	static {
		// RENDERERS
		DEFAULT_RENDERER = new DefaultRenderer();
		CHECK_BOX_RENDERER = new CheckBoxRenderer();
		DATE_RENDERER = new CalendarRenderer(new SimpleDateFormat(Settings.getStringProperty("date.date_format") + " "
				+ Settings.getStringProperty("date.time_format")));
		LENGTH_RENDERER = new LengthRenderer(new SimpleDateFormat(Settings.getStringProperty("date.time_format")));
		TASK_TITLE_RENDERER = new TaskTitleRenderer();
		STAR_RENDERER = new StarRenderer();

		TASK_PRIORITY_RENDERER = new TaskPriorityRenderer();
		TASK_REPEAT_FROM_RENDERER = new TaskRepeatFromRenderer();
		TASK_STATUS_RENDERER = new TaskStatusRenderer();

		// EDITORS
		JCheckBox checkBox = null;

		checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);

		CHECK_BOX_EDITOR = new DefaultCellEditor(checkBox);
		DATE_EDITOR = new DateEditor(new SimpleDateFormat(Settings.getStringProperty("date.date_format") + " "
				+ Settings.getStringProperty("date.time_format")));
		LENGTH_EDITOR = new LengthEditor();

		checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		checkBox.setIcon(new ImageIcon("images/blank_star.gif"));
		checkBox.setSelectedIcon(new ImageIcon("images/star.gif"));

		STAR_EDITOR = new DefaultCellEditor(checkBox);

		CONTEXT_EDITOR = new DefaultCellEditor(new JComboBox(new ContextComboBoxModel()));
		FOLDER_EDITOR = new DefaultCellEditor(new JComboBox(new FolderComboBoxModel()));
		GOAL_EDITOR = new DefaultCellEditor(new JComboBox(new GoalComboBoxModel()));
		LOCATION_EDITOR = new DefaultCellEditor(new JComboBox(new LocationComboBoxModel()));

		JTextField textField = null;

		textField = new JTextField();

		TASK_REPEAT_EDITOR = new DefaultCellEditor(textField);

		JComboBox comboBox = null;

		comboBox = new JComboBox(TaskPriority.values());
		comboBox.setRenderer(new TaskPriorityListCellRenderer());

		TASK_PRIORITY_EDITOR = new DefaultCellEditor(comboBox);

		comboBox = new JComboBox(TaskRepeatFrom.values());
		comboBox.setRenderer(new TaskRepeatFromListCellRenderer());

		REPEAT_FROM_EDITOR = new DefaultCellEditor(comboBox);

		comboBox = new JComboBox(TaskStatus.values());
		comboBox.setRenderer(new TaskStatusListCellRenderer());

		TASK_STATUS_EDITOR = new DefaultCellEditor(comboBox);
	}

	private TaskSearcher searcher;

	public TaskTable() {
		this.searcher = null;

		this.initialize();
	}

	public void showColumn(TaskColumn taskColumn, boolean show) {
		if (show) {
			try {
				this.getColumn(taskColumn);
			} catch (IllegalArgumentException e) {
				// This column does not exist
				taskColumn.setVisible(true);
				((TaskTableColumnModel) this.getColumnModel()).addColumn(taskColumn);
			}
		} else {
			try {
				TableColumn column = this.getColumn(taskColumn);

				taskColumn.setVisible(false);
				((TaskTableColumnModel) this.getColumnModel()).removeColumn(column);
			} catch (IllegalArgumentException e) {
				// This column does not exist
			}
		}
	}

	public Task getTask(int row) {
		int index = this.getRowSorter().convertRowIndexToModel(row);
		return ((TaskTableModel) this.getModel()).getTask(index);
	}

	public Task getSelectedTask() {
		int index = this.getSelectedRow();

		if (index == -1)
			return null;

		return this.getTask(index);
	}

	public void setSelectedTask(Task task) {
		TaskTableModel model = (TaskTableModel) this.getModel();

		int index = -1;
		for (int i = 0; i < model.getRowCount(); i++)
			if (task.equals(model.getTask(i)))
				index = this.getRowSorter().convertRowIndexToView(i);

		if (index == -1)
			return;

		this.getSelectionModel().setSelectionInterval(index, index);
	}

	public void refreshTasks() {
		this.getRowSorter().allRowsChanged();
	}

	public TaskSearcher getTaskSearcher() {
		return this.searcher;
	}

	public void setTaskSearcher(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Task searcher cannot be null");

		this.searcher = searcher;

		TaskRowFilter taskRowFilter = (TaskRowFilter) ((TaskTableRowSorter) this.getRowSorter()).getRowFilter();
		taskRowFilter.setFilter(searcher.getFilter());

		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		List<TaskSorterElement> sortElements = new ArrayList<TaskSorterElement>(searcher.getSorter().getElements());
		Collections.sort(sortElements, new Comparator<TaskSorterElement>() {

			@Override
			public int compare(TaskSorterElement o1, TaskSorterElement o2) {
				return new Integer(o1.getOrder()).compareTo(o2.getOrder());
			}

		});

		for (TaskSorterElement element : sortElements) {
			// Don't sort if column is not visible (does not exist)
			try {
				this.getColumn(element.getColumn());
			} catch (IllegalArgumentException e) {
				continue;
			}

			sortKeys.add(new RowSorter.SortKey(this.getColumn(element.getColumn()).getModelIndex(), element
					.getSortOrder()));
		}

		this.getRowSorter().setSortKeys(sortKeys);
		this.refreshTasks();
	}

	private void initialize() {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TaskTableColumnModel columnModel = new TaskTableColumnModel();
		TaskTableModel tableModel = new TaskTableModel();

		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(30);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		this.initializeDragAndDrop();
		this.initializeCopyAndPaste();
		this.initiliazeTableSorter();
		this.initializeTableHeaderMenu();
	}

	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		this.setTransferHandler(new TaskTransferHandler());
		this.setDropMode(DropMode.ON_OR_INSERT_ROWS);
	}

	private void initializeCopyAndPaste() {
		ActionMap amap = this.getActionMap();
		amap.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
		amap.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
		amap.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());

		InputMap imap = this.getInputMap();
		imap.put(KeyStroke.getKeyStroke("ctrl X"), TransferHandler.getCutAction().getValue(Action.NAME));
		imap.put(KeyStroke.getKeyStroke("ctrl C"), TransferHandler.getCopyAction().getValue(Action.NAME));
		imap.put(KeyStroke.getKeyStroke("ctrl V"), TransferHandler.getPasteAction().getValue(Action.NAME));
	}

	private void initiliazeTableSorter() {
		TaskTableRowSorter sorter = new TaskTableRowSorter((TaskTableModel) this.getModel());
		sorter.setRowFilter(new TaskRowFilter(new TaskFilter()));
		this.setRowSorter(sorter);
	}

	private void initializeTableHeaderMenu() {
		this.getTableHeader().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger() || event.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popup = new JPopupMenu(Translations.getString("general.columns"));

					int x = 0;
					TaskColumn[] currentTaskColumns = new TaskColumn[TaskTable.this.getColumnCount()];
					Enumeration<TableColumn> columns = TaskTable.this.getColumnModel().getColumns();
					while (columns.hasMoreElements()) {
						currentTaskColumns[x++] = (TaskColumn) columns.nextElement().getIdentifier();
					}

					TaskColumn[] taskColumns = TaskColumn.getValues(false);
					for (int i = 0; i < taskColumns.length; i++) {
						final TaskColumn taskColumn = taskColumns[i];

						boolean found = false;

						for (int j = 0; j < currentTaskColumns.length; j++) {
							if (taskColumn == currentTaskColumns[j]) {
								found = true;
								break;
							}
						}

						final JCheckBoxMenuItem item = new JCheckBoxMenuItem(taskColumns[i].getLabel());
						item.setSelected(found);
						item.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent event) {
								TaskTable.this.showColumn(taskColumn, item.isSelected());
							}

						});

						popup.add(item);
					}

					popup.show(event.getComponent(), event.getX(), event.getY());
				}
			}

		});
	}

	@Override
	public TableCellEditor getCellEditor(int row, int col) {
		TaskColumn column = ((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(col);

		switch (column) {
			case FOLDER:
				return FOLDER_EDITOR;
			case CONTEXT:
				return CONTEXT_EDITOR;
			case GOAL:
				return GOAL_EDITOR;
			case LOCATION:
				return LOCATION_EDITOR;
			case COMPLETED:
				return CHECK_BOX_EDITOR;
			case DUE_DATE:
				return DATE_EDITOR;
			case START_DATE:
				return DATE_EDITOR;
			case REPEAT:
				return TASK_REPEAT_EDITOR;
			case REPEAT_FROM:
				return REPEAT_FROM_EDITOR;
			case STATUS:
				return TASK_STATUS_EDITOR;
			case LENGTH:
				return LENGTH_EDITOR;
			case PRIORITY:
				return TASK_PRIORITY_EDITOR;
			case STAR:
				return STAR_EDITOR;
			default:
				return super.getCellEditor(row, col);
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		TaskColumn column = ((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(col);

		switch (column) {
			case TITLE:
				return TASK_TITLE_RENDERER;
			case COMPLETED:
				return CHECK_BOX_RENDERER;
			case COMPLETED_ON:
				return DATE_RENDERER;
			case DUE_DATE:
				return DATE_RENDERER;
			case START_DATE:
				return DATE_RENDERER;
			case LENGTH:
				return LENGTH_RENDERER;
			case STAR:
				return STAR_RENDERER;
			case PRIORITY:
				return TASK_PRIORITY_RENDERER;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_RENDERER;
			case STATUS:
				return TASK_STATUS_RENDERER;
			default:
				return DEFAULT_RENDERER;
		}
	}

}
