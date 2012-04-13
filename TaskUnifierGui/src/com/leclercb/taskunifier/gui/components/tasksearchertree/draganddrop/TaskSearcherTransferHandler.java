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
package com.leclercb.taskunifier.gui.components.tasksearchertree.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelParent;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;
import com.leclercb.taskunifier.gui.commons.transfer.TaskSearcherTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.TaskSearcherTransferable;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherTree;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.ModelItem;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.SearcherItem;
import com.leclercb.taskunifier.gui.components.tasksearchertree.nodes.SearcherNode;

public class TaskSearcherTransferHandler extends TransferHandler {
	
	public TaskSearcherTransferHandler() {
		
	}
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (this.canImportModelFlavor(support))
			return true;
		
		if (this.canImportTaskSearcherFlavor(support))
			return true;
		
		return false;
	}
	
	private boolean canImportModelFlavor(TransferSupport support) {
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			if (!support.isDrop())
				return false;
			
			Transferable t = support.getTransferable();
			ModelTransferData data = null;
			
			try {
				data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (data == null)
					return false;
			} catch (Exception e) {
				return false;
			}
			
			SearcherNode node = this.getSearcherNodeForLocation(support);
			
			if (node == null)
				return false;
			
			if (data.getType().equals(ModelType.TASK)) {
				if (node.getTaskSearcher().getTemplate() == null)
					return false;
				
				return true;
			}
			
			if (data.getType().equals(ModelType.CONTEXT)
					|| data.getType().equals(ModelType.FOLDER)
					|| data.getType().equals(ModelType.GOAL)) {
				if (!(node instanceof ModelItem))
					return false;
				
				if (!data.getType().equals(((ModelItem) node).getModelType()))
					return false;
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean canImportTaskSearcherFlavor(TransferSupport support) {
		if (support.isDataFlavorSupported(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR)) {
			Transferable t = support.getTransferable();
			TaskSearcherTransferData data = null;
			TaskSearcher dragSearcher = null;
			
			try {
				data = (TaskSearcherTransferData) t.getTransferData(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR);
				dragSearcher = data.getTaskSearcher();
			} catch (Exception e) {
				return false;
			}
			
			if (dragSearcher == null)
				return false;
			
			if (!support.isDrop()) {
				return true;
			} else {
				TaskSearcherTree tree = (TaskSearcherTree) support.getComponent();
				SearcherNode node = this.getSearcherNodeForLocation(support);
				
				if (node == null)
					return false;
				
				if (!(node instanceof SearcherItem))
					return false;
				
				SearcherItem dragItem = tree.getSearcherModel().findItemFromSearcher(
						dragSearcher);
				
				if (dragItem == null)
					return false;
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		TaskSearcherTree tree = (TaskSearcherTree) c;
		TaskSearcher searcher = tree.getSelectedTaskSearcher();
		Model model = tree.getSelectedModel();
		
		if (searcher == null)
			return null;
		
		if (model == null) {
			return new TaskSearcherTransferable(new TaskSearcherTransferData(
					searcher), null);
		}
		
		return new TaskSearcherTransferable(new TaskSearcherTransferData(
				searcher), new ModelTransferData(
				model.getModelType(),
				model.getModelId()));
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support))
			return false;
		
		if (this.importModelFlavorData(support))
			return true;
		
		if (this.importTaskSearcherFlavorData(support))
			return true;
		
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean importModelFlavorData(TransferSupport support) {
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			Transferable t = support.getTransferable();
			ModelTransferData data = null;
			
			try {
				data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (data == null)
					return false;
			} catch (Exception e) {
				return false;
			}
			
			SearcherNode node = this.getSearcherNodeForLocation(support);
			
			if (data.getType().equals(ModelType.TASK)) {
				for (ModelId id : data.getIds()) {
					Task task = TaskFactory.getInstance().get(id);
					node.getTaskSearcher().getTemplate().applyTo(task);
				}
			}
			
			if (data.getType().equals(ModelType.CONTEXT)
					|| data.getType().equals(ModelType.FOLDER)
					|| data.getType().equals(ModelType.GOAL)) {
				for (ModelId id : data.getIds()) {
					ModelParent model = (ModelParent) ModelFactoryUtils.getModel(
							data.getType(),
							id);
					ModelParent childModel = (ModelParent) ((ModelItem) node).getModel();
					
					if (!model.equals(childModel))
						model.setParent(childModel);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private boolean importTaskSearcherFlavorData(TransferSupport support) {
		if (support.isDataFlavorSupported(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR)) {
			Transferable t = support.getTransferable();
			TaskSearcherTransferData data = null;
			TaskSearcher dragSearcher = null;
			
			try {
				data = (TaskSearcherTransferData) t.getTransferData(TaskSearcherTransferable.TASK_SEARCHER_FLAVOR);
				dragSearcher = data.getTaskSearcher();
			} catch (Exception e) {
				return false;
			}
			
			if (dragSearcher == null)
				return false;
			
			if (!support.isDrop()) {
				TaskSearcher newSearcher = dragSearcher.clone();
				TaskSearcherFactory.getInstance().register(newSearcher);
				
				return true;
			} else {
				TaskSearcherTree tree = (TaskSearcherTree) support.getComponent();
				SearcherNode node = this.getSearcherNodeForLocation(support);
				
				if (node == null)
					return false;
				
				if (!(node instanceof SearcherItem))
					return false;
				
				SearcherItem dragItem = tree.getSearcherModel().findItemFromSearcher(
						dragSearcher);
				
				if (dragItem == null)
					return false;
				
				SearcherCategory category = (SearcherCategory) node.getParent();
				
				if (category.getType() == dragSearcher.getType()) {
					List<SearcherItem> items = new ArrayList<SearcherItem>();
					
					for (int i = 0; i < category.getChildCount(); i++) {
						items.add((SearcherItem) category.getChildAt(i));
					}
					
					int index = items.indexOf(node);
					
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
						tree.getSearcherModel().insertNodeInto(
								i,
								category,
								order++);
					}
					
					tree.expandPath(TreeUtils.getPath(category));
				} else {
					dragSearcher.setType(category.getType());
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		
	}
	
	private SearcherNode getSearcherNodeForLocation(TransferSupport support) {
		TaskSearcherTree tree = (TaskSearcherTree) support.getComponent();
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		TreePath path = tree.getPathForLocation(
				dl.getDropPoint().x,
				dl.getDropPoint().y);
		
		if (path == null)
			return null;
		
		if (!(path.getLastPathComponent() instanceof SearcherNode))
			return null;
		
		return (SearcherNode) path.getLastPathComponent();
	}
	
}
