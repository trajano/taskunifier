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
package com.leclercb.taskunifier.gui.components.tasktasks.table.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.TaskList.TaskItem;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;
import com.leclercb.taskunifier.gui.components.tasktasks.table.TaskTasksTable;

public class TaskTasksTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		Transferable t = support.getTransferable();
		TaskTasksTable table = (TaskTasksTable) support.getComponent();
		
		if (table.getTaskGroup() == null) {
			return false;
		}
		
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			try {
				ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (!data.getType().equals(ModelType.TASK))
					return false;
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						"Transfer data error",
						e);
				
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.LINK;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		
		Transferable t = support.getTransferable();
		TaskTasksTable table = (TaskTasksTable) support.getComponent();
		
		if (table.getTaskGroup() == null) {
			return false;
		}
		
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
				for (Task task : dragTasks) {
					table.getTaskGroup().add(new TaskItem(task, null));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		
	}
	
}
