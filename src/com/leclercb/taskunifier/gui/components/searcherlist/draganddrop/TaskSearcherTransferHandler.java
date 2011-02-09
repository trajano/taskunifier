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
package com.leclercb.taskunifier.gui.components.searcherlist.draganddrop;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherView;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;

public class TaskSearcherTransferHandler extends TransferHandler {
	
	private SearcherView view;
	
	public TaskSearcherTransferHandler(SearcherView view) {
		CheckUtils.isNotNull(view, "View cannot be null");
		this.view = view;
	}
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR))
			return false;
		
		if (support.isDrop())
			return false;
		
		return true;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		TaskSearcher searcher = this.view.getSelectedTaskSearcher();
		
		if (searcher == null)
			return null;
		
		return new TaskSearcherTransferable(new TaskSearcherTransferData(
				searcher));
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		
		// Get Drag Task
		Transferable t = support.getTransferable();
		TaskSearcher dragSearcher = null;
		
		try {
			TaskSearcherTransferData data = (TaskSearcherTransferData) t.getTransferData(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR);
			dragSearcher = data.getTaskSearcher();
			
			if (dragSearcher == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		
		TaskSearcher newSearcher = dragSearcher.clone();
		TaskSearcherFactory.getInstance().register(newSearcher);
		
		return true;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}
	
}
