/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.tasks.table;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.JXTable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.tasks.table.draganddrop.TaskTransferHandler;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskAlternateHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskDueTodayHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskDueTodayHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskOverDueHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskOverDueHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskRepeatHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskRepeatHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskSelectedHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskSelectedHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskTitleHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskTitleHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskTooltipHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskTooltipHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.menu.TaskTableMenu;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowComparator;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowFilter;
import com.leclercb.taskunifier.gui.components.views.TaskView.InfoTab;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public class TaskTable extends JXTable implements TaskTableView {
	
	private UndoSupport undoSupport;
	
	private ModelSelectionChangeSupport modelSelectionChangeSupport;
	
	private TUTableProperties<TaskColumn> tableProperties;
	private TaskTableMenu taskTableMenu;
	
	public TaskTable(TUTableProperties<TaskColumn> tableProperties) {
		CheckUtils.isNotNull(tableProperties);
		this.tableProperties = tableProperties;
		this.undoSupport = Constants.UNDO_SUPPORT;
		this.modelSelectionChangeSupport = new ModelSelectionChangeSupport(this);
		this.initialize();
	}
	
	@Override
	public TUTableProperties<TaskColumn> getTableProperties() {
		return this.tableProperties;
	}
	
	@Override
	public int getTaskCount() {
		return this.getRowCount();
	}
	
	public Task getTask(int row) {
		try {
			int index = this.getRowSorter().convertRowIndexToModel(row);
			return ((TaskTableModel) this.getModel()).getTask(index);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}
	
	@Override
	public Task[] getSelectedTasks() {
		int[] indexes = this.getSelectedRows();
		
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1) {
				Task task = this.getTask(indexes[i]);
				
				if (task != null)
					tasks.add(task);
			}
		}
		
		return tasks.toArray(new Task[0]);
	}
	
	@Override
	public void setSelectedTasks(Task[] tasks) {
		TaskTableModel model = (TaskTableModel) this.getModel();
		
		this.getSelectionModel().setValueIsAdjusting(true);
		this.getSelectionModel().clearSelection();
		
		int firstRowIndex = -1;
		for (Task task : tasks) {
			for (int i = 0; i < model.getRowCount(); i++) {
				if (task.equals(model.getTask(i))) {
					int index = this.getRowSorter().convertRowIndexToView(i);
					
					if (index != -1) {
						this.getSelectionModel().addSelectionInterval(
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
			this.scrollRowToVisible(firstRowIndex);
	}
	
	@Override
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
						
						if (editor instanceof JTextComponent)
							((JTextComponent) editor).selectAll();
					}
				}
				
				break;
			}
		}
	}
	
	@Override
	public void refreshTasks() {
		this.getRowSorter().allRowsChanged();
		
		try {
			if (this.getSelectedRow() != -1)
				this.scrollRowToVisible(this.getSelectedRow());
		} catch (Throwable t) {
			
		}
		
		this.firePropertyChange(PROP_TASK_COUNT, null, this.getTaskCount());
	}
	
	public TaskSearcher getTaskSearcher() {
		return TaskRowComparator.getInstance().getTaskSearcher();
	}
	
	public void setTaskSearcher(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher);
		
		TaskRowComparator.getInstance().setTaskSearcher(searcher);
		
		this.setSortOrder(TaskColumn.MODEL, SortOrder.ASCENDING);
		this.getSortController().setRowFilter(
				new TaskRowFilter(searcher.getFilter()));
		this.refreshTasks();
	}
	
	@Override
	public void printTasks() throws HeadlessException, PrinterException {
		PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		attributes.add(new JobName(Constants.TITLE, null));
		attributes.add(OrientationRequested.LANDSCAPE);
		
		this.print(
				PrintMode.FIT_WIDTH,
				new MessageFormat(Constants.TITLE
						+ " - "
						+ this.getTaskSearcher().getTitle()),
				new MessageFormat(this.getTaskCount() + " tasks | Page - {0}"),
				true,
				attributes,
				true);
	}
	
	@Override
	public void pasteTask() {
		TransferHandler.getPasteAction().actionPerformed(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		
		this.commitChanges();
	}
	
	@Override
	public void commitChanges() {
		if (this.getCellEditor() != null)
			this.getCellEditor().stopCellEditing();
	}
	
	@Override
	public void addModelSelectionChangeListener(ModelSelectionListener listener) {
		this.modelSelectionChangeSupport.addModelSelectionChangeListener(listener);
	}
	
	@Override
	public void removeModelSelectionChangeListener(
			ModelSelectionListener listener) {
		this.modelSelectionChangeSupport.removeModelSelectionChangeListener(listener);
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		if (event.getSelectedTaskSearcher() != null)
			this.setTaskSearcher(event.getSelectedTaskSearcher());
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		TaskTableColumnModel columnModel = new TaskTableColumnModel(
				this.tableProperties);
		TaskTableModel tableModel = new TaskTableModel(this.undoSupport);
		
		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(24);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setShowGrid(true, false);
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		
		this.setSortable(true);
		this.setSortsOnUpdates(false);
		this.setSortOrderCycle(SortOrder.ASCENDING);
		this.setColumnControlVisible(true);
		
		this.initializeHeaderListener();
		this.initializeDeleteTasks();
		this.initializeDoubleClick();
		this.initializeTaskTableMenu();
		this.initializeDragAndDrop();
		this.initializeEnter();
		this.initializeUndoRedo();
		this.initializeCopyAndPaste();
		this.initializeHighlighters();
		
		Main.getSettings().addPropertyChangeListener(
				"task.indent_subtasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TaskTable.this.refreshTasks();
					}
					
				});
		
		Main.getSettings().addPropertyChangeListener(
				"tasksearcher.show_completed_tasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TaskTable.this.refreshTasks();
					}
					
				});
		
		this.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting())
							TaskTable.this.modelSelectionChangeSupport.fireModelSelectionChange(TaskTable.this.getSelectedTasks());
					}
					
				});
		
		this.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent evt) {
				TaskTable.this.firePropertyChange(
						PROP_TASK_COUNT,
						null,
						TaskTable.this.getTaskCount());
			}
			
		});
	}
	
	private void initializeHeaderListener() {
		this.getTableHeader().addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent evt) {
				JTable table = ((JTableHeader) evt.getSource()).getTable();
				TableColumnModel colModel = table.getColumnModel();
				
				int colIndex = colModel.getColumnIndexAtX(evt.getX());
				
				if (colIndex == -1) {
					return;
				}
				
				Rectangle headerRect = table.getTableHeader().getHeaderRect(
						colIndex);
				if (headerRect.contains(evt.getX(), evt.getY())) {
					TaskColumn column = (TaskColumn) colModel.getColumn(
							colIndex).getIdentifier();
					
					if (column == TaskColumn.MODEL)
						return;
					
					TaskSearcher searcher = TaskTable.this.getTaskSearcher().clone();
					
					if (searcher.getSorter().getElementCount() != 0
							&& searcher.getSorter().getElement(0).getProperty() == column) {
						TaskSorterElement element = searcher.getSorter().getElement(
								0);
						
						SortOrder order = element.getSortOrder();
						
						if (order == SortOrder.ASCENDING)
							order = SortOrder.DESCENDING;
						else
							order = SortOrder.ASCENDING;
						
						element.setSortOrder(order);
					} else {
						TaskSorterElement element = new TaskSorterElement(
								column,
								SortOrder.ASCENDING);
						
						searcher.getSorter().insertElement(element, 0);
					}
					
					TaskTable.this.setTaskSearcher(searcher);
				}
			}
			
		});
	}
	
	private void initializeDeleteTasks() {
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE)
					ActionDelete.delete();
			}
			
		});
	}
	
	private void initializeDoubleClick() {
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1
						&& event.getClickCount() == 2) {
					try {
						int rowIndex = TaskTable.this.getRowSorter().convertRowIndexToModel(
								TaskTable.this.rowAtPoint(event.getPoint()));
						
						int colIndex = TaskTable.this.columnAtPoint(event.getPoint());
						
						TaskColumn column = (TaskColumn) TaskTable.this.getColumn(
								colIndex).getIdentifier();
						
						if (column == TaskColumn.CONTACTS
								|| column == TaskColumn.TASKS
								|| column == TaskColumn.FILES
								|| column == TaskColumn.MODEL_EDIT
								|| column == TaskColumn.NOTE) {
							Task task = ((TaskTableModel) TaskTable.this.getModel()).getTask(rowIndex);
							
							if (task == null)
								return;
							
							TaskTable.this.commitChanges();
							TaskTable.this.setSelectedTasks(new Task[] { task });
							
							if (column == TaskColumn.CONTACTS) {
								if (ViewUtils.getCurrentTaskView() != null) {
									ViewUtils.getCurrentTaskView().setSelectedInfoTab(
											InfoTab.CONTACTS);
								}
							}
							
							if (column == TaskColumn.TASKS) {
								if (ViewUtils.getCurrentTaskView() != null) {
									ViewUtils.getCurrentTaskView().setSelectedInfoTab(
											InfoTab.TASKS);
								}
							}
							
							if (column == TaskColumn.FILES) {
								if (ViewUtils.getCurrentTaskView() != null) {
									ViewUtils.getCurrentTaskView().setSelectedInfoTab(
											InfoTab.FILES);
								}
							}
							
							if (column == TaskColumn.MODEL_EDIT) {
								ActionEditTasks.editTasks(
										new Task[] { task },
										true);
							}
							
							if (column == TaskColumn.NOTE) {
								if (ViewUtils.getCurrentTaskView() != null) {
									ViewUtils.getCurrentTaskView().setSelectedInfoTab(
											InfoTab.NOTE);
									ViewUtils.getCurrentTaskView().getModelNoteView().edit();
								}
							}
						}
					} catch (Exception e) {
						
					}
				}
			}
			
		});
	}
	
	private void initializeTaskTableMenu() {
		this.taskTableMenu = new TaskTableMenu();
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger()
						|| event.getButton() == MouseEvent.BUTTON3) {
					int rowIndex = TaskTable.this.getRowSorter().convertRowIndexToModel(
							TaskTable.this.rowAtPoint(event.getPoint()));
					
					Task task = ((TaskTableModel) TaskTable.this.getModel()).getTask(rowIndex);
					
					if (task == null)
						return;
					
					TaskTable.this.commitChanges();
					
					boolean found = false;
					Task[] selectedTasks = TaskTable.this.getSelectedTasks();
					for (Task selectedTask : selectedTasks) {
						if (selectedTask.equals(task)) {
							found = true;
							break;
						}
					}
					
					if (!found)
						TaskTable.this.setSelectedTasks(new Task[] { task });
					
					TaskTable.this.taskTableMenu.show(
							event.getComponent(),
							event.getX(),
							event.getY());
				}
			}
			
		});
	}
	
	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		this.setTransferHandler(new TaskTransferHandler());
		this.setDropMode(DropMode.ON_OR_INSERT_ROWS);
	}
	
	private void initializeEnter() {
		ActionMap amap = this.getActionMap();
		amap.put("editTask", new ActionEditTasks() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (TaskTable.this.isEditing()) {
					TaskTable.this.commitChanges();
					return;
				}
				
				super.actionPerformed(e);
			}
			
		});
		
		InputMap imap = this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "editTask");
	}
	
	private void initializeUndoRedo() {
		this.undoSupport.initializeMaps(this);
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
	
	private void initializeHighlighters() {
		this.setHighlighters(
				new TaskAlternateHighlighter(),
				new TaskHighlighter(new TaskHighlightPredicate()),
				new TaskTitleHighlighter(new TaskTitleHighlightPredicate()),
				new TaskRepeatHighlighter(new TaskRepeatHighlightPredicate()),
				new TaskDueTodayHighlighter(
						new TaskDueTodayHighlightPredicate()),
				new TaskOverDueHighlighter(new TaskOverDueHighlightPredicate()),
				new TaskSelectedHighlighter(
						new TaskSelectedHighlightPredicate()),
				new TaskTooltipHighlighter(new TaskTooltipHighlightPredicate()));
	}
	
}
