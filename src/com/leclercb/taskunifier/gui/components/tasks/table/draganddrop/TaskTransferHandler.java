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
package com.leclercb.taskunifier.gui.components.tasks.table.draganddrop;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.transfer.ModelTransferable;

public class TaskTransferHandler extends TransferHandler {

	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR))
			return false;

		// Get Drag Task
		Transferable t = support.getTransferable();
		Task dragTask = null;

		try {
			ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);

			if (!data.getType().equals(ModelType.TASK))
				return false;

			dragTask = TaskFactory.getInstance().get(data.getId());
		} catch (Exception e) { 
			e.printStackTrace();
			return false; 
		}

		if (support.isDrop()) {
			// Get Objects
			TaskTable table = (TaskTable) support.getComponent();
			JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();

			// False : If drag task has at least one child
			if (TaskFactory.getInstance().getChildren(dragTask).size() != 0)
				return false;

			// True : If insert row
			if (((JTable.DropLocation) support.getDropLocation()).isInsertRow()) {
				return true;
			}

			// Get Drop Task
			Task dropTask = table.getTask(dl.getRow());

			// False if drag task equals to drop task
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
		Task task = table.getSelectedTask();
		return new ModelTransferable(new ModelTransferData(ModelType.TASK, task.getModelId()));
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.LINK;
	}

	@Override
	public boolean importData(TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}

		// Get Drag Task
		Transferable t = support.getTransferable();
		Task dragTask = null;

		try {
			ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);

			if (!data.getType().equals(ModelType.TASK))
				return false;

			dragTask = TaskFactory.getInstance().get(data.getId());
		} catch (Exception e) { 
			return false; 
		}

		if (support.isDrop()) {
			// Get Objects
			TaskTable table = (TaskTable) support.getComponent();
			JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();

			// Import : If insert row
			if (((JTable.DropLocation) support.getDropLocation()).isInsertRow()) {
				dragTask.setParent(null);
				table.getRowSorter().allRowsChanged();
				return true;
			}

			// Get Drop Task
			Task dropTask = table.getTask(dl.getRow());

			// Validate Import
			if (dropTask.getParent() != null)
				return false;

			// Import
			try {
				dragTask.setParent(dropTask);
				table.getRowSorter().allRowsChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		} else {
			// Get Objects
			TaskTable table = (TaskTable) support.getComponent();

			Task newTask = TaskFactory.getInstance().create(dragTask);			

			table.getRowSorter().allRowsChanged();
			table.setSelectedTask(newTask);

			return true;
		}
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}

}
