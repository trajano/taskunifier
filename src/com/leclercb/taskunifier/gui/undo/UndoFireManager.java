/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.undo;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.leclercb.taskunifier.api.utils.CheckUtils;

public class UndoFireManager extends UndoManager {

	private List<IUndoListener> undoListeners;
	private List<IRedoListener> redoListeners;

	public UndoFireManager() {
		undoListeners = new ArrayList<IUndoListener>();
		redoListeners = new ArrayList<IRedoListener>();
	}

	public void addUndoListener(IUndoListener listener) {
		CheckUtils.isNotNull(listener, "Listener cannot be null");

		if (!undoListeners.contains(listener))
			undoListeners.add(listener);
	}

	public void removeUndoListener(IUndoListener listener) {
		undoListeners.remove(listener);
	}

	protected void fireUndoPerformed() {
		for (IUndoListener listener : undoListeners)
			listener.undoPerformed(new ActionEvent(this, 0, null));
	}

	public void addRedoListener(IRedoListener listener) {
		CheckUtils.isNotNull(listener, "Listener cannot be null");

		if (!redoListeners.contains(listener))
			redoListeners.add(listener);
	}

	public void removeRedoListener(IRedoListener listener) {
		redoListeners.remove(listener);
	}

	protected void fireRedoPerformed() {
		for (IRedoListener listener : redoListeners)
			listener.redoPerformed(new ActionEvent(this, 0, null));
	}

	@Override
	public synchronized void undo() throws CannotUndoException {
		super.undo();
		fireUndoPerformed();
	}

	@Override
	public synchronized void redo() throws CannotRedoException {
		super.redo();
		fireRedoPerformed();
	}

}
