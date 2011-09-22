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
package com.leclercb.taskunifier.gui.components.tasks.table.draganddrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.apache.commons.io.IOUtils;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionDuplicateTasks;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class TaskTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		Transferable t = support.getTransferable();
		
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			// Get Drag Task
			List<Task> dragTasks = new ArrayList<Task>();
			
			try {
				ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (!data.getType().equals(ModelType.TASK))
					return false;
				
				for (ModelId id : data.getIds())
					dragTasks.add(TaskFactory.getInstance().get(id));
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						"Transfer data error",
						e);
				
				return false;
			}
			
			if (support.isDrop()) {
				// Get Objects
				TaskTable table = (TaskTable) support.getComponent();
				JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
				
				// True : If insert row
				if (((JTable.DropLocation) support.getDropLocation()).isInsertRow())
					return true;
				
				// False : If drag task has at least one child
				for (Task dragTask : dragTasks)
					if (dragTask.getChildren().length != 0)
						return false;
				
				// Get Drop Task
				Task dropTask = table.getTask(dl.getRow());
				
				if (dropTask == null)
					return false;
				
				// False if drag task equals to drop task
				for (Task dragTask : dragTasks)
					if (dragTask.equals(dropTask))
						return false;
				
				// False if drop task has a parent task
				if (dropTask.getParent() != null)
					return false;
				
				return true;
			} else {
				return true;
			}
		} else {
			DataFlavor[] flavors = t.getTransferDataFlavors();
			for (DataFlavor flavor : flavors) {
				if (flavor.isFlavorTextType()
						&& flavor.getHumanPresentableName().equals("text/plain"))
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		TaskTable table = (TaskTable) c;
		Task[] tasks = table.getSelectedTasks();
		
		List<ModelId> ids = new ArrayList<ModelId>();
		for (Task task : tasks)
			ids.add(task.getModelId());
		
		return new ModelTransferable(new ModelTransferData(
				ModelType.TASK,
				ids.toArray(new ModelId[0])));
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		
		Transferable t = support.getTransferable();
		TaskTable table = (TaskTable) support.getComponent();
		
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			// Get Drag Task
			List<Task> dragTasks = new ArrayList<Task>();
			
			try {
				ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (!data.getType().equals(ModelType.TASK))
					return false;
				
				for (ModelId id : data.getIds())
					dragTasks.add(TaskFactory.getInstance().get(id));
			} catch (Exception e) {
				return false;
			}
			
			if (support.isDrop()) {
				// Get Objects
				JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
				
				// Import : If insert row
				if (dl.isInsertRow()) {
					if (this.isSortByOrder()) {
						Task prevTask = table.getTask(dl.getRow() - 1);
						
						TaskUtils.updateOrder(
								(prevTask == null ? 0 : prevTask.getOrder() + 1),
								dragTasks.toArray(new Task[0]));
						
						for (Task dragTask : dragTasks)
							if (!EqualsUtils.equals(
									dragTask.getParent(),
									this.getParent(table, dl.getRow())))
								dragTask.setParent(null);
					} else {
						for (Task dragTask : dragTasks)
							dragTask.setParent(null);
					}
					
					table.refreshTasks();
					return true;
				}
				
				// Get Drop Task
				Task dropTask = table.getTask(dl.getRow());
				
				if (dropTask == null)
					return false;
				
				// Validate Import
				if (dropTask.getParent() != null)
					return false;
				
				// Import
				for (Task dragTask : dragTasks)
					dragTask.setParent(dropTask);
				
				table.refreshTasks();
				
				return true;
			} else {
				ActionDuplicateTasks.duplicateTasks(dragTasks.toArray(new Task[0]));
				
				return true;
			}
		} else {
			try {
				DataFlavor[] flavors = t.getTransferDataFlavors();
				for (DataFlavor flavor : flavors) {
					if (flavor.isFlavorTextType()
							&& flavor.getHumanPresentableName().equals(
									"text/plain")) {
						Reader reader = flavor.getReaderForText(t);
						String title = IOUtils.toString(reader);
						Task task = ActionAddTask.addTask(title, false);
						
						table.refreshTasks();
						table.setSelectedTasks(new Task[] { task });
						
						return true;
					}
				}
			} catch (Throwable throwable) {
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		
	}
	
	private boolean isSortByOrder() {
		boolean isSortByOrder = false;
		TaskSorter sorter = ViewType.getTaskView().getTaskSearcherView().getSelectedOriginalTaskSearcher().getSorter();
		if (sorter.getElementCount() >= 1)
			if (sorter.getElement(0).getProperty() == TaskColumn.ORDER)
				isSortByOrder = true;
		
		return isSortByOrder;
	}
	
	private Task getParent(TaskTable table, int index) {
		Task[] tasks = this.getDisplayedTasks(table);
		
		if (tasks.length < index)
			return null;
		
		for (int i = index; i > 0; i--) {
			Task task = tasks[i];
			
			if (task.getParent() == null)
				if (i == index)
					return null;
				else
					return task;
		}
		
		return null;
	}
	
	private Task[] getDisplayedTasks(TaskTable table) {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < table.getTaskCount(); i++) {
			Task task = table.getTask(i);
			
			if (task == null)
				continue;
			
			tasks.add(task);
		}
		
		return tasks.toArray(new Task[0]);
	}
	
}
