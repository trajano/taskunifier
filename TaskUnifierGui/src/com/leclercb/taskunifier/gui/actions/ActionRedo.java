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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEditSupport;

import org.apache.commons.lang3.SystemUtils;

import com.leclercb.commons.api.properties.events.WeakUndoableEditListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.undo.UndoFireManager;
import com.leclercb.commons.gui.swing.undo.events.DiscardAllEditsListener;
import com.leclercb.commons.gui.swing.undo.events.RedoListener;
import com.leclercb.commons.gui.swing.undo.events.UndoListener;
import com.leclercb.commons.gui.swing.undo.events.WeakDiscardAllEditsListener;
import com.leclercb.commons.gui.swing.undo.events.WeakRedoListener;
import com.leclercb.commons.gui.swing.undo.events.WeakUndoListener;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionRedo extends AbstractAction implements UndoableEditListener, UndoListener, RedoListener, DiscardAllEditsListener {
	
	private UndoFireManager undoManager;
	private UndoableEditSupport editSupport;
	
	public ActionRedo(
			int width,
			int height,
			UndoFireManager undoManager,
			UndoableEditSupport editSupport) {
		super(
				Translations.getString("action.redo"),
				ImageUtils.getResourceImage("redo.png", width, height));
		
		CheckUtils.isNotNull(undoManager);
		CheckUtils.isNotNull(editSupport);
		
		this.undoManager = undoManager;
		this.editSupport = editSupport;
		
		this.putValue(SHORT_DESCRIPTION, Translations.getString("action.redo"));
		
		if (SystemUtils.IS_OS_MAC) {
			this.putValue(
					ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(
							KeyEvent.VK_Z,
							InputEvent.SHIFT_DOWN_MASK
									| Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		} else {
			this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_Y,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		}
		
		this.updateAction();
		
		this.undoManager.addUndoListener(new WeakUndoListener(
				this.undoManager,
				this));
		this.undoManager.addRedoListener(new WeakRedoListener(
				this.undoManager,
				this));
		this.undoManager.addDiscardAllEditsListener(new WeakDiscardAllEditsListener(
				this.undoManager,
				this));
		this.editSupport.addUndoableEditListener(new WeakUndoableEditListener(
				this.editSupport,
				this));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.undoManager.canRedo())
			this.undoManager.redo();
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
	
	@Override
	public void discardAllEditsPerformed(ActionEvent event) {
		this.updateAction();
	}
	
	private void updateAction() {
		this.setEnabled(this.undoManager.canRedo());
		this.putValue(NAME, this.undoManager.getRedoPresentationName());
	}
	
}
