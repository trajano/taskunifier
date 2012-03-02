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
package com.leclercb.taskunifier.gui.components.notesearchertree.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.TreeUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherFactory;
import com.leclercb.taskunifier.gui.commons.comparators.NoteSearcherComparator;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;
import com.leclercb.taskunifier.gui.commons.transfer.NoteSearcherTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.NoteSearcherTransferable;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherTree;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherCategory;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherItem;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.SearcherNode;

public class NoteSearcherTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (this.canImportModelFlavor(support))
			return true;
		
		if (this.canImportNoteSearcherFlavor(support))
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
			} catch (Exception e) {
				return false;
			}
			
			if (!data.getType().equals(ModelType.NOTE))
				return false;
			
			SearcherNode node = this.getSearcherNodeForLocation(support);
			
			if (node == null)
				return false;
			
			if (node.getNoteSearcher().getTemplate() == null)
				return false;
			
			return true;
		}
		
		return false;
	}
	
	private boolean canImportNoteSearcherFlavor(TransferSupport support) {
		if (support.isDataFlavorSupported(NoteSearcherTransferable.NOTE_SEARCHER_FLAVOR)) {
			Transferable t = support.getTransferable();
			NoteSearcherTransferData data = null;
			NoteSearcher dragSearcher = null;
			
			try {
				data = (NoteSearcherTransferData) t.getTransferData(NoteSearcherTransferable.NOTE_SEARCHER_FLAVOR);
				dragSearcher = data.getNoteSearcher();
			} catch (Exception e) {
				return false;
			}
			
			if (dragSearcher == null)
				return false;
			
			if (!support.isDrop()) {
				return true;
			} else {
				SearcherNode node = this.getSearcherNodeForLocation(support);
				
				if (node == null)
					return false;
				
				if (!(node instanceof SearcherItem))
					return false;
				
				SearcherItem dragItem = null;
				List<SearcherItem> items = new ArrayList<SearcherItem>();
				SearcherCategory category = (SearcherCategory) node.getParent();
				
				for (int i = 0; i < category.getChildCount(); i++) {
					SearcherItem item = (SearcherItem) category.getChildAt(i);
					items.add(item);
					if (EqualsUtils.equals(item.getNoteSearcher(), dragSearcher))
						dragItem = item;
				}
				
				if (dragItem == null)
					return false;
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		NoteSearcherTree tree = (NoteSearcherTree) c;
		NoteSearcher searcher = tree.getSelectedNoteSearcher();
		
		if (searcher == null)
			return null;
		
		return new NoteSearcherTransferable(new NoteSearcherTransferData(
				searcher));
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
		
		if (this.importNoteSearcherFlavorData(support))
			return true;
		
		return false;
	}
	
	private boolean importModelFlavorData(TransferSupport support) {
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			Transferable t = support.getTransferable();
			ModelTransferData data = null;
			
			try {
				data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
			} catch (Exception e) {
				return false;
			}
			
			SearcherNode node = this.getSearcherNodeForLocation(support);
			
			for (ModelId id : data.getIds()) {
				Note note = NoteFactory.getInstance().get(id);
				node.getNoteSearcher().getTemplate().applyTo(note);
			}
			
			return true;
		}
		
		return false;
	}
	
	private boolean importNoteSearcherFlavorData(TransferSupport support) {
		if (support.isDataFlavorSupported(NoteSearcherTransferable.NOTE_SEARCHER_FLAVOR)) {
			Transferable t = support.getTransferable();
			NoteSearcherTransferData data = null;
			NoteSearcher dragSearcher = null;
			
			try {
				data = (NoteSearcherTransferData) t.getTransferData(NoteSearcherTransferable.NOTE_SEARCHER_FLAVOR);
				dragSearcher = data.getNoteSearcher();
			} catch (Exception e) {
				return false;
			}
			
			if (dragSearcher == null)
				return false;
			
			if (!support.isDrop()) {
				NoteSearcher newSearcher = dragSearcher.clone();
				NoteSearcherFactory.getInstance().register(newSearcher);
				
				return true;
			} else {
				NoteSearcherTree tree = (NoteSearcherTree) support.getComponent();
				SearcherNode node = this.getSearcherNodeForLocation(support);
				
				if (node == null)
					return false;
				
				if (!(node instanceof SearcherItem))
					return false;
				
				SearcherItem dragItem = null;
				List<SearcherItem> items = new ArrayList<SearcherItem>();
				SearcherCategory category = (SearcherCategory) node.getParent();
				
				for (int i = 0; i < category.getChildCount(); i++) {
					SearcherItem item = (SearcherItem) category.getChildAt(i);
					items.add(item);
					if (EqualsUtils.equals(item.getNoteSearcher(), dragSearcher))
						dragItem = item;
				}
				
				if (dragItem == null)
					return false;
				
				Collections.sort(items, new Comparator<SearcherItem>() {
					
					@Override
					public int compare(SearcherItem o1, SearcherItem o2) {
						return NoteSearcherComparator.INSTANCE.compare(
								o1.getNoteSearcher(),
								o2.getNoteSearcher());
					}
					
				});
				
				int index = items.indexOf(node);
				
				items.remove(dragItem);
				items.add(index, dragItem);
				
				int order = 1;
				for (SearcherItem i : items) {
					i.getNoteSearcher().setOrder(order++);
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
		
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		
	}
	
	private SearcherNode getSearcherNodeForLocation(TransferSupport support) {
		NoteSearcherTree tree = (NoteSearcherTree) support.getComponent();
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
