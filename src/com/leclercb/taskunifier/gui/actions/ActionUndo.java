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

import com.leclercb.commons.gui.swing.undo.IRedoListener;
import com.leclercb.commons.gui.swing.undo.IUndoListener;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ActionUndo extends AbstractAction implements UndoableEditListener, IUndoListener, IRedoListener {
	
	public ActionUndo() {
		this(32, 32);
	}
	
	public ActionUndo(int width, int height) {
		super(
				Translations.getString("action.name.undo"),
				Images.getResourceImage("undo.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.undo"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		this.updateAction();
		
		Constants.UNDO_MANAGER.addUndoListener(this);
		Constants.UNDO_MANAGER.addRedoListener(this);
		Constants.UNDO_EDIT_SUPPORT.addUndoableEditListener(this);
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
		this.putValue(NAME, Constants.UNDO_MANAGER.getUndoPresentationName());
	}
	
}
