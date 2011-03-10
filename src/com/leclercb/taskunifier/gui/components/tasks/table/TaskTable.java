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

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.components.tasks.table.draganddrop.TaskTransferHandler;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.DateEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LengthEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.RepeatEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.CalendarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.CheckBoxRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.DefaultRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.LengthRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.ModelRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.RepeatRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.StarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskPriorityRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskRepeatFromRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskStatusRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskTitleRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowFilter;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskTableRowSorter;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TaskTable extends JTable {
	
	private static final DefaultRenderer DEFAULT_RENDERER;
	private static final CheckBoxRenderer CHECK_BOX_RENDERER;
	private static final ModelRenderer MODEL_RENDERER;
	private static final CalendarRenderer DATE_RENDERER;
	private static final RepeatRenderer REPEAT_RENDERER;
	private static final LengthRenderer LENGTH_RENDERER;
	private static final TaskTitleRenderer TASK_TITLE_RENDERER;
	private static final StarRenderer STAR_RENDERER;
	
	private static final TaskPriorityRenderer TASK_PRIORITY_RENDERER;
	private static final TaskRepeatFromRenderer TASK_REPEAT_FROM_RENDERER;
	private static final TaskStatusRenderer TASK_STATUS_RENDERER;
	
	private static final TableCellEditor CHECK_BOX_EDITOR;
	private static final TableCellEditor DATE_EDITOR;
	private static final TableCellEditor LENGTH_EDITOR;
	private static final TableCellEditor STAR_EDITOR;
	
	private static final TableCellEditor CONTEXT_EDITOR;
	private static final TableCellEditor FOLDER_EDITOR;
	private static final TableCellEditor GOAL_EDITOR;
	private static final TableCellEditor LOCATION_EDITOR;
	
	private static final TableCellEditor REPEAT_EDITOR;
	
	private static final TableCellEditor TASK_PRIORITY_EDITOR;
	private static final TableCellEditor TASK_REPEAT_FROM_EDITOR;
	private static final TableCellEditor TASK_STATUS_EDITOR;
	
	static {
		// RENDERERS
		DEFAULT_RENDERER = new DefaultRenderer();
		CHECK_BOX_RENDERER = new CheckBoxRenderer();
		MODEL_RENDERER = new ModelRenderer();
		DATE_RENDERER = new CalendarRenderer(new SimpleDateFormat(
				Main.SETTINGS.getStringProperty("date.date_format")
						+ " "
						+ Main.SETTINGS.getStringProperty("date.time_format")));
		REPEAT_RENDERER = new RepeatRenderer();
		LENGTH_RENDERER = new LengthRenderer(
				Main.SETTINGS.getSimpleDateFormatProperty("date.time_format"));
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
		LENGTH_EDITOR = new LengthEditor();
		DATE_EDITOR = new DateEditor();
		
		checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		checkBox.setIcon(new ImageIcon("images/blank_star.gif"));
		checkBox.setSelectedIcon(new ImageIcon("images/star.gif"));
		
		JComboBox comboBox = null;
		
		STAR_EDITOR = new DefaultCellEditor(checkBox);
		
		CONTEXT_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new ContextModel(true)));
		
		FOLDER_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new FolderModel(true)));
		
		GOAL_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new GoalModel(true)));
		
		LOCATION_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new LocationModel(true)));
		
		REPEAT_EDITOR = new RepeatEditor();
		
		comboBox = new JComboBox(TaskPriority.values());
		comboBox.setRenderer(new TaskPriorityListCellRenderer());
		
		TASK_PRIORITY_EDITOR = new DefaultCellEditor(comboBox);
		
		comboBox = new JComboBox(TaskRepeatFrom.values());
		comboBox.setRenderer(new TaskRepeatFromListCellRenderer());
		
		TASK_REPEAT_FROM_EDITOR = new DefaultCellEditor(comboBox);
		
		comboBox = new JComboBox(TaskStatus.values());
		comboBox.setRenderer(new TaskStatusListCellRenderer());
		
		TASK_STATUS_EDITOR = new DefaultCellEditor(comboBox);
	}
	
	private TaskSearcher searcher;
	
	public TaskTable() {
		this.searcher = null;
		
		this.initialize();
	}
	
	private void showColumn(TaskColumn taskColumn, boolean show) {
		if (show) {
			try {
				this.getColumn(taskColumn);
			} catch (IllegalArgumentException e) {
				((TaskTableColumnModel) this.getColumnModel()).addColumn(taskColumn);
			}
		} else {
			try {
				TableColumn column = this.getColumn(taskColumn);
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
	
	public Task[] getSelectedTasks() {
		int[] indexes = this.getSelectedRows();
		
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < indexes.length; i++)
			if (indexes[i] != -1)
				tasks.add(this.getTask(indexes[i]));
		
		return tasks.toArray(new Task[0]);
	}
	
	public void setSelectedTaskAndStartEdit(Task task) {
		this.setSelectedTasks(new Task[] { task });
		
		TaskTableColumnModel columnModel = (TaskTableColumnModel) this.getColumnModel();
		TaskTableModel model = (TaskTableModel) this.getModel();
		
		for (int i = 0; i < model.getRowCount(); i++) {
			if (task.equals(model.getTask(i))) {
				int row = this.getRowSorter().convertRowIndexToView(i);
				int col = columnModel.getColumnIndex(TaskColumn.TITLE);
				
				if (row != -1) {
					if (this.editCellAt(row, col)) {
						Component editor = this.getEditorComponent();
						editor.requestFocusInWindow();
					}
				}
				
				break;
			}
		}
	}
	
	public void setSelectedTasks(Task[] tasks) {
		TaskTableModel model = (TaskTableModel) this.getModel();
		
		this.getSelectionModel().setValueIsAdjusting(true);
		
		int firstRowIndex = -1;
		for (Task task : tasks) {
			for (int i = 0; i < model.getRowCount(); i++) {
				if (task.equals(model.getTask(i))) {
					int index = this.getRowSorter().convertRowIndexToView(i);
					
					if (index != -1) {
						this.getSelectionModel().setSelectionInterval(
								index,
								index);
						
						if (firstRowIndex == -1)
							firstRowIndex = index;
					}
				}
			}
		}
		
		this.getSelectionModel().setValueIsAdjusting(false);
		
		if (firstRowIndex != -1)
			this.scrollToVisible(firstRowIndex, 0);
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
		List<TaskSorterElement> sortElements = searcher.getSorter().getElements();
		
		for (TaskSorterElement element : sortElements) {
			// Don't sort if column is not visible (does not exist)
			try {
				this.getColumn(element.getColumn());
			} catch (IllegalArgumentException e) {
				continue;
			}
			
			sortKeys.add(new RowSorter.SortKey(
					this.getColumn(element.getColumn()).getModelIndex(),
					element.getSortOrder()));
		}
		
		this.getRowSorter().setSortKeys(sortKeys);
		this.refreshTasks();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		TaskTableColumnModel columnModel = new TaskTableColumnModel();
		TaskTableModel tableModel = new TaskTableModel();
		
		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(28);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		
		this.initializeTaskEdit();
		this.initializeTaskColumn();
		
		this.initializeDragAndDrop();
		this.initializeCopyAndPaste();
		this.initiliazeTableSorter();
		this.initializeTableHeaderMenu();
	}
	
	private void initializeTaskEdit() {
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger()
						|| event.getButton() == MouseEvent.BUTTON3) {
					int rowIndex = TaskTable.this.getRowSorter().convertRowIndexToModel(
							TaskTable.this.rowAtPoint(event.getPoint()));
					
					Task task = ((TaskTableModel) TaskTable.this.getModel()).getTask(rowIndex);
					
					TaskEditDialog dialog = new TaskEditDialog(
							task,
							MainFrame.getInstance().getFrame(),
							true);
					dialog.setVisible(true);
				}
			}
			
		});
	}
	
	private void initializeTaskColumn() {
		TaskColumn.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				TaskColumn column = (TaskColumn) evt.getSource();
				
				if (evt.getPropertyName().equals(TaskColumn.PROP_VISIBLE)) {
					TaskTable.this.showColumn(
							column,
							(Boolean) evt.getNewValue());
				}
				
				if (evt.getPropertyName().equals(TaskColumn.PROP_WIDTH)) {
					try {
						TableColumn tableCol = TaskTable.this.getColumn(column);
						System.out.println(column + " : " + evt.getNewValue());
						tableCol.setPreferredWidth((Integer) evt.getNewValue());
					} catch (IllegalArgumentException e) {

					}
				}
				
				if (evt.getPropertyName().equals(TaskColumn.PROP_ORDER)) {
					// TODO: fix set order problem
					System.out.println(column + " : " + evt.getNewValue());
					TaskTableColumnModel columnModel = (TaskTableColumnModel) TaskTable.this.getColumnModel();
					
					try {
						columnModel.moveColumn(
								columnModel.getColumnIndex(column),
								(Integer) evt.getNewValue());
					} catch (IllegalArgumentException e) {

					}
				}
			}
			
		});
	}
	
	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		this.setTransferHandler(new TaskTransferHandler());
		this.setDropMode(DropMode.ON_OR_INSERT_ROWS);
	}
	
	private void initializeCopyAndPaste() {
		ActionMap amap = this.getActionMap();
		amap.put(
				TransferHandler.getCutAction().getValue(Action.NAME),
				TransferHandler.getCutAction());
		amap.put(
				TransferHandler.getCopyAction().getValue(Action.NAME),
				TransferHandler.getCopyAction());
		amap.put(
				TransferHandler.getPasteAction().getValue(Action.NAME),
				TransferHandler.getPasteAction());
		
		InputMap imap = this.getInputMap();
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_X,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getCutAction().getValue(Action.NAME));
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_C,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getCopyAction().getValue(Action.NAME));
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_V,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getPasteAction().getValue(Action.NAME));
	}
	
	private void initiliazeTableSorter() {
		TaskTableRowSorter sorter = new TaskTableRowSorter(
				(TaskTableModel) this.getModel());
		sorter.setRowFilter(new TaskRowFilter(new TaskFilter()));
		this.setRowSorter(sorter);
	}
	
	private void initializeTableHeaderMenu() {
		this.getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger()
						|| event.getButton() == MouseEvent.BUTTON3) {
					JPopupMenu popup = new JPopupMenu(
							Translations.getString("general.columns"));
					
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
						
						final JCheckBoxMenuItem item = new JCheckBoxMenuItem(
								taskColumns[i].getLabel());
						item.setSelected(found);
						item.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent event) {
								taskColumn.setVisible(item.isSelected());
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
	public String getToolTipText(MouseEvent event) {
		Point p = event.getPoint();
		
		int colIndex = this.columnAtPoint(p);
		int rowIndex = this.getRowSorter().convertRowIndexToModel(
				this.rowAtPoint(p));
		
		if (((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(colIndex) != TaskColumn.TITLE)
			return null;
		
		Task task = ((TaskTableModel) this.getModel()).getTask(rowIndex);
		
		if (task != null)
			return task.getTitle();
		
		return null;
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
				return REPEAT_EDITOR;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_EDITOR;
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
			case CONTEXT:
			case FOLDER:
			case GOAL:
			case LOCATION:
				return MODEL_RENDERER;
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
			case REPEAT:
				return REPEAT_RENDERER;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_RENDERER;
			case STATUS:
				return TASK_STATUS_RENDERER;
			default:
				return DEFAULT_RENDERER;
		}
	}
	
	private void scrollToVisible(int row, int col) {
		if (!(this.getParent() instanceof JViewport)) {
			return;
		}
		
		JViewport viewport = (JViewport) this.getParent();
		Rectangle rect = this.getCellRect(row, col, true);
		Point pt = viewport.getViewPosition();
		rect.setLocation(rect.x - pt.x, rect.y - pt.y);
		viewport.scrollRectToVisible(rect);
	}
	
}
