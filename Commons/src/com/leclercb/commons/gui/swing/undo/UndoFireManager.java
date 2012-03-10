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
import com.leclercb.commons.gui.swing.undo.events.DiscardAllEditsListener;
import com.leclercb.commons.gui.swing.undo.events.DiscardAllEditsSupported;
import com.leclercb.commons.gui.swing.undo.events.RedoListener;
import com.leclercb.commons.gui.swing.undo.events.RedoSupported;
import com.leclercb.commons.gui.swing.undo.events.UndoListener;
import com.leclercb.commons.gui.swing.undo.events.UndoSupported;

public class UndoFireManager extends UndoManager implements UndoSupported, RedoSupported, DiscardAllEditsSupported {
	
	private ListenerList<UndoListener> undoListenerList;
	private ListenerList<RedoListener> redoListenerList;
	private ListenerList<DiscardAllEditsListener> discardAllEditsListenerList;
	
	public UndoFireManager() {
		this.setLimit(10);
		
		this.undoListenerList = new ListenerList<UndoListener>();
		this.redoListenerList = new ListenerList<RedoListener>();
		this.discardAllEditsListenerList = new ListenerList<DiscardAllEditsListener>();
	}
	
	@Override
	public void addUndoListener(UndoListener listener) {
		this.undoListenerList.addListener(listener);
	}
	
	@Override
	public void removeUndoListener(UndoListener listener) {
		this.undoListenerList.removeListener(listener);
	}
	
	protected void fireUndoPerformed() {
		for (UndoListener listener : this.undoListenerList)
			listener.undoPerformed(new ActionEvent(this, 0, null));
	}
	
	@Override
	public void addRedoListener(RedoListener listener) {
		this.redoListenerList.addListener(listener);
	}
	
	@Override
	public void removeRedoListener(RedoListener listener) {
		this.redoListenerList.removeListener(listener);
	}
	
	protected void fireRedoPerformed() {
		for (RedoListener listener : this.redoListenerList)
			listener.redoPerformed(new ActionEvent(this, 0, null));
	}
	
	@Override
	public void addDiscardAllEditsListener(DiscardAllEditsListener listener) {
		this.discardAllEditsListenerList.addListener(listener);
	}
	
	@Override
	public void removeDiscardAllEditsListener(DiscardAllEditsListener listener) {
		this.discardAllEditsListenerList.removeListener(listener);
	}
	
	protected void fireDiscardAllEditsPerformed() {
		for (DiscardAllEditsListener listener : this.discardAllEditsListenerList)
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
