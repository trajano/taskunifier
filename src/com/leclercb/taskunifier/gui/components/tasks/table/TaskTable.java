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
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.TransferHandler;

import org.jdesktop.swingx.JXTable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.draganddrop.TaskTransferHandler;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskAlternateHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskRepeatHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskRepeatHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskTitleHighlightPredicate;
import com.leclercb.taskunifier.gui.components.tasks.table.highlighters.TaskTitleHighlighter;
import com.leclercb.taskunifier.gui.components.tasks.table.menu.TaskTableMenu;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowComparator;
import com.leclercb.taskunifier.gui.components.tasks.table.sorter.TaskRowFilter;

public class TaskTable extends JXTable {
	
	private TaskTableMenu taskTableMenu;
	
	public TaskTable() {
		this.initialize();
	}
	
	public Task getTask(int row) {
		try {
			int index = this.getRowSorter().convertRowIndexToModel(row);
			return ((TaskTableModel) this.getModel()).getTask(index);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}
	
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
			this.scrollRowToVisible(firstRowIndex);
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
	
	public void refreshTasks() {
		this.getRowSorter().allRowsChanged();
	}
	
	public TaskSearcher getTaskSearcher() {
		return TaskRowComparator.getInstance().getSearcher();
	}
	
	public void setTaskSearcher(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Task searcher cannot be null");
		
		TaskRowComparator.getInstance().setSearcher(searcher);
		
		this.setSortOrder(TaskColumn.MODEL, SortOrder.ASCENDING);
		this.getSortController().setRowFilter(
				new TaskRowFilter(searcher.getFilter()));
		this.refreshTasks();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		TaskTableColumnModel columnModel = new TaskTableColumnModel();
		TaskTableModel tableModel = new TaskTableModel();
		
		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(24);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		
		this.setSortable(true);
		this.setSortsOnUpdates(false);
		this.setSortOrderCycle(SortOrder.ASCENDING);
		this.setColumnControlVisible(true);
		
		this.initializeDeleteTask();
		this.initializeTaskTableMenu();
		this.initializeDragAndDrop();
		this.initializeCopyAndPaste();
		this.initializeHighlighter();
	}
	
	private void initializeDeleteTask() {
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE)
					ActionDelete.delete();
			}
			
		});
	}
	
	private void initializeTaskTableMenu() {
		this.taskTableMenu = new TaskTableMenu(this);
		
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
					
					boolean found = false;
					Task[] selectedTasks = TaskTable.this.getSelectedTasks();
					for (Task selectedTask : selectedTasks) {
						if (task.equals(selectedTask)) {
							found = true;
							break;
						}
					}
					
					if (!found)
						TaskTable.this.setSelectedTasks(new Task[] { task });
					
					if (TaskTable.this.getCellEditor() != null)
						TaskTable.this.getCellEditor().stopCellEditing();
					
					TaskTable.this.taskTableMenu.setTaskToEdit(task);
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
	
	private void initializeHighlighter() {
		this.setHighlighters(
				new TaskAlternateHighlighter(),
				new TaskHighlighter(new TaskHighlightPredicate()),
				new TaskRepeatHighlighter(new TaskRepeatHighlightPredicate()),
				new TaskTitleHighlighter(new TaskTitleHighlightPredicate()));
	}
	
}
