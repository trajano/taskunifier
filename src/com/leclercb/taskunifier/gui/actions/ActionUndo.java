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
package com.leclercb.taskunifier.gui.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEditSupport;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.undo.IRedoListener;
import com.leclercb.commons.gui.swing.undo.IUndoListener;
import com.leclercb.commons.gui.swing.undo.UndoFireManager;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionUndo extends AbstractAction implements UndoableEditListener, IUndoListener, IRedoListener {
	
	private UndoFireManager undoManager;
	private UndoableEditSupport editSupport;
	
	public ActionUndo(
			UndoFireManager undoManager,
			UndoableEditSupport editSupport) {
		this(undoManager, editSupport, 32, 32);
	}
	
	public ActionUndo(
			UndoFireManager undoManager,
			UndoableEditSupport editSupport,
			int width,
			int height) {
		super(Translations.getString("action.undo"), Images.getResourceImage(
				"undo.png",
				width,
				height));
		
		CheckUtils.isNotNull(undoManager, "Undo manager cannot be null");
		CheckUtils.isNotNull(editSupport, "Edit support cannot be null");
		
		this.undoManager = undoManager;
		this.editSupport = editSupport;
		
		this.putValue(SHORT_DESCRIPTION, Translations.getString("action.undo"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		this.updateAction();
		
		this.undoManager.addUndoListener(this);
		this.undoManager.addRedoListener(this);
		this.editSupport.addUndoableEditListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Constants.UNDO_MANAGER.undo();
	}
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		this.updateAction();
	}
	
	@Override
	public void undoPerformed(ActionEvent event) {
		this.updateAction();
	}
	
	@Override
	public void redoPerformed(ActionEvent event) {
		this.updateAction();
	}
	
	private void updateAction() {
		this.setEnabled(Constants.UNDO_MANAGER.canUndo());
		this.putValue(NAME, Constants.UNDO_MANAGER.getRedoPresentationName());
	}
	
}
