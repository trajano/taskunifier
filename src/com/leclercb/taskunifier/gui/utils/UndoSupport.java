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
package com.leclercb.taskunifier.gui.utils;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import org.apache.commons.lang3.SystemUtils;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.undo.UndoFireManager;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionUndo;

public class UndoSupport implements UndoableEditListener {
	
	private UndoFireManager undoManager;
	private UndoableEditSupport editSupport;
	
	private ActionUndo undoAction;
	private ActionRedo redoAction;
	
	public UndoSupport() {
		this(new UndoFireManager(), new UndoableEditSupport());
	}
	
	public UndoSupport(
			UndoFireManager undoManager,
			UndoableEditSupport editSupport) {
		CheckUtils.isNotNull(undoManager);
		CheckUtils.isNotNull(editSupport);
		
		this.undoManager = undoManager;
		this.editSupport = editSupport;
		this.editSupport.addUndoableEditListener(this.undoManager);
		
		this.undoAction = new ActionUndo(
				this.undoManager,
				this.editSupport,
				16,
				16);
		
		this.redoAction = new ActionRedo(
				this.undoManager,
				this.editSupport,
				16,
				16);
	}
	
	public ActionUndo getUndoAction() {
		return this.undoAction;
	}
	
	public ActionRedo getRedoAction() {
		return this.redoAction;
	}
	
	public void initializeMaps(JComponent component) {
		ActionMap amap = component.getActionMap();
		amap.put("undo", this.undoAction);
		amap.put("redo", this.redoAction);
		
		InputMap imap = component.getInputMap();
		imap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "undo");
		
		if (SystemUtils.IS_OS_MAC) {
			imap.put(KeyStroke.getKeyStroke(
					KeyEvent.VK_Z,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
							| KeyEvent.VK_ALT), "redo");
		} else {
			imap.put(
					KeyStroke.getKeyStroke(
							KeyEvent.VK_Y,
							Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
					"redo");
		}
	}
	
	public void postEdit(UndoableEdit e) {
		this.editSupport.postEdit(e);
	}
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		this.editSupport.postEdit(e.getEdit());
	}
	
	public void discardAllEdits() {
		this.undoManager.discardAllEdits();
	}
	
}
