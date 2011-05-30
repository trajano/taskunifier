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
package com.leclercb.taskunifier.gui.components.searcherlist.transfer;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherView;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
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
		newSearcher.setType(TaskSearcherType.PERSONAL);
		TaskSearcherFactory.getInstance().register(newSearcher);
		
		return true;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}
	
}
