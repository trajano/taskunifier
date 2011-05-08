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
package com.leclercb.taskunifier.gui.components.notes.table.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;
import com.leclercb.taskunifier.gui.components.notes.table.NoteTable;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;

public class NoteTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR))
			return false;
		
		Transferable t = support.getTransferable();
		
		try {
			ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
			
			if (!data.getType().equals(ModelType.NOTE))
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		NoteTable table = (NoteTable) c;
		Note[] notes = table.getSelectedNotes();
		
		List<ModelId> ids = new ArrayList<ModelId>();
		for (Note note : notes)
			ids.add(note.getModelId());
		
		return new ModelTransferable(new ModelTransferData(
				ModelType.NOTE,
				ids.toArray(new ModelId[0])));
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
		
		// Get Drag Note
		Transferable t = support.getTransferable();
		List<Note> dragNotes = new ArrayList<Note>();
		
		try {
			ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
			
			if (!data.getType().equals(ModelType.NOTE))
				return false;
			
			for (ModelId id : data.getIds())
				dragNotes.add(NoteFactory.getInstance().get(id));
		} catch (Exception e) {
			return false;
		}
		
		// Get Objects
		NoteTable table = (NoteTable) support.getComponent();
		
		List<Note> newNotes = new ArrayList<Note>();
		
		Synchronizing.setSynchronizing(true);
		
		for (Note dragNote : dragNotes)
			newNotes.add(NoteFactory.getInstance().create(dragNote));
		
		Synchronizing.setSynchronizing(false);
		
		table.getRowSorter().allRowsChanged();
		table.setSelectedNotes(newNotes.toArray(new Note[0]));
		
		return true;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}
	
}
