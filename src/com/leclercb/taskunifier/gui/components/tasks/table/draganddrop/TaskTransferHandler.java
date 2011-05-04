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

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;

public class TaskTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR))
			return false;
		
		// Get Drag Task
		Transferable t = support.getTransferable();
		List<Task> dragTasks = new ArrayList<Task>();
		
		try {
			ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
			
			if (!data.getType().equals(ModelType.TASK))
				return false;
			
			for (ModelId id : data.getIds())
				dragTasks.add(TaskFactory.getInstance().get(id));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (support.isDrop()) {
			// Get Objects
			TaskTable table = (TaskTable) support.getComponent();
			JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
			
			// False : If drag task has at least one child
			for (Task dragTask : dragTasks)
				if (TaskFactory.getInstance().getChildren(dragTask).size() != 0)
					return false;
			
			// True : If insert row
			if (((JTable.DropLocation) support.getDropLocation()).isInsertRow()) {
				return true;
			}
			
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
		
		// Get Drag Task
		Transferable t = support.getTransferable();
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
			TaskTable table = (TaskTable) support.getComponent();
			JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
			
			// Import : If insert row
			if (dl.isInsertRow()) {
				for (Task dragTask : dragTasks)
					dragTask.setParent(null);
				
				table.getRowSorter().allRowsChanged();
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
			try {
				for (Task dragTask : dragTasks)
					dragTask.setParent(dropTask);
				
				table.getRowSorter().allRowsChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;
		} else {
			// Get Objects
			TaskTable table = (TaskTable) support.getComponent();
			
			List<Task> newTasks = new ArrayList<Task>();
			
			Synchronizing.setSynchronizing(true);
			
			for (Task dragTask : dragTasks)
				newTasks.add(TaskFactory.getInstance().create(dragTask));
			
			Synchronizing.setSynchronizing(false);
			
			table.getRowSorter().allRowsChanged();
			table.setSelectedTasks(newTasks.toArray(new Task[0]));
			
			return true;
		}
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}
	
}
