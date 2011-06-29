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
package com.leclercb.taskunifier.gui.components.searchertree.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.commons.transfer.TaskSearcherTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.TaskSearcherTransferable;
import com.leclercb.taskunifier.gui.components.searchertree.SearcherTree;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.searchertree.nodes.SearcherItem;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskSearcherTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR))
			return false;
		
		Transferable t = support.getTransferable();
		TaskSearcherTransferData data = null;
		TaskSearcher dragSearcher = null;
		
		try {
			data = (TaskSearcherTransferData) t.getTransferData(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR);
			dragSearcher = data.getTaskSearcher();
			
			if (dragSearcher == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		
		if (!support.isDrop()) {
			return true;
		} else {
			SearcherTree tree = (SearcherTree) support.getComponent();
			JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
			TreePath path = tree.getPathForLocation(
					dl.getDropPoint().x,
					dl.getDropPoint().y);
			
			if (path == null
					|| !(path.getLastPathComponent() instanceof SearcherItem))
				return false;
			
			SearcherItem dropItem = (SearcherItem) path.getLastPathComponent();
			
			SearcherItem dragItem = null;
			List<SearcherItem> items = new ArrayList<SearcherItem>();
			SearcherCategory category = (SearcherCategory) dropItem.getParent();
			
			for (int i = 0; i < category.getChildCount(); i++) {
				SearcherItem item = (SearcherItem) category.getChildAt(i);
				items.add(item);
				if (EqualsUtils.equals(item.getTaskSearcher(), dragSearcher))
					dragItem = item;
			}
			
			if (dragItem == null)
				return false;
		}
		
		return true;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		SearcherTree tree = (SearcherTree) c;
		TaskSearcher searcher = tree.getSelectedTaskSearcher();
		
		if (searcher == null)
			return null;
		
		return new TaskSearcherTransferable(new TaskSearcherTransferData(
				searcher));
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
		TaskSearcherTransferData data = null;
		TaskSearcher dragSearcher = null;
		
		try {
			data = (TaskSearcherTransferData) t.getTransferData(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR);
			dragSearcher = data.getTaskSearcher();
			
			if (dragSearcher == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		
		if (!support.isDrop()) {
			TaskSearcher newSearcher = dragSearcher.clone();
			TaskSearcherFactory.getInstance().register(newSearcher);
			
			return true;
		} else {
			SearcherTree tree = (SearcherTree) support.getComponent();
			JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
			TreePath path = tree.getPathForLocation(
					dl.getDropPoint().x,
					dl.getDropPoint().y);
			
			if (path == null
					|| !(path.getLastPathComponent() instanceof SearcherItem))
				return false;
			
			SearcherItem dropItem = (SearcherItem) path.getLastPathComponent();
			
			SearcherItem dragItem = null;
			List<SearcherItem> items = new ArrayList<SearcherItem>();
			SearcherCategory category = (SearcherCategory) dropItem.getParent();
			
			for (int i = 0; i < category.getChildCount(); i++) {
				SearcherItem item = (SearcherItem) category.getChildAt(i);
				items.add(item);
				if (EqualsUtils.equals(item.getTaskSearcher(), dragSearcher))
					dragItem = item;
			}
			
			if (dragItem == null)
				return false;
			
			Collections.sort(items, new Comparator<SearcherItem>() {
				
				@Override
				public int compare(SearcherItem o1, SearcherItem o2) {
					return CompareUtils.compare(
							o1.getTaskSearcher(),
							o2.getTaskSearcher());
				}
				
			});
			
			int index = items.indexOf(dropItem);
			
			items.remove(dragItem);
			items.add(index, dragItem);
			
			int order = 1;
			for (SearcherItem i : items) {
				i.getTaskSearcher().setOrder(order++);
			}
			
			for (SearcherItem i : items) {
				tree.getSearcherModel().removeNodeFromParent(i);
			}
			
			order = 0;
			for (SearcherItem i : items) {
				tree.getSearcherModel().insertNodeInto(i, category, order++);
			}
			
			tree.expandPath(TreeUtils.getPath(category));
			
			return true;
		}
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}
	
}
