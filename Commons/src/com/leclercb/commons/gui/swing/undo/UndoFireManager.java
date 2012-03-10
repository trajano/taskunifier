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
package com.leclercb.commons.gui.swing.undo;

import java.awt.event.ActionEvent;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.leclercb.commons.api.event.ListenerList;

public class UndoFireManager extends UndoManager {
	
	private ListenerList<IUndoListener> undoListenerList;
	private ListenerList<IRedoListener> redoListenerList;
	private ListenerList<IDiscardAllEditsListener> discardAllEditsListenerList;
	
	public UndoFireManager() {
		this.setLimit(10);
		
		this.undoListenerList = new ListenerList<IUndoListener>();
		this.redoListenerList = new ListenerList<IRedoListener>();
		this.discardAllEditsListenerList = new ListenerList<IDiscardAllEditsListener>();
	}
	
	public void addUndoListener(IUndoListener listener) {
		this.undoListenerList.addListener(listener);
	}
	
	public void removeUndoListener(IUndoListener listener) {
		this.undoListenerList.removeListener(listener);
	}
	
	protected void fireUndoPerformed() {
		for (IUndoListener listener : this.undoListenerList)
			listener.undoPerformed(new ActionEvent(this, 0, null));
	}
	
	public void addRedoListener(IRedoListener listener) {
		this.redoListenerList.addListener(listener);
	}
	
	public void removeRedoListener(IRedoListener listener) {
		this.redoListenerList.removeListener(listener);
	}
	
	protected void fireRedoPerformed() {
		for (IRedoListener listener : this.redoListenerList)
			listener.redoPerformed(new ActionEvent(this, 0, null));
	}
	
	public void addDiscardAllEditsListener(IDiscardAllEditsListener listener) {
		this.discardAllEditsListenerList.addListener(listener);
	}
	
	public void removeDiscardAllEditsListener(IDiscardAllEditsListener listener) {
		this.discardAllEditsListenerList.removeListener(listener);
	}
	
	protected void fireDiscardAllEditsPerformed() {
		for (IDiscardAllEditsListener listener : this.discardAllEditsListenerList)
			listener.discardAllEditsPerformed(new ActionEvent(this, 0, null));
	}
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.fireUndoPerformed();
	}
	
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.fireRedoPerformed();
	}
	
	@Override
	public void discardAllEdits() {
		super.discardAllEdits();
		this.fireDiscardAllEditsPerformed();
	}
	
}
