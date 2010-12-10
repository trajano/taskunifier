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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.leclercb.taskunifier.api.event.ListenerList;

public class UndoFireManager extends UndoManager {

	private ListenerList<IUndoListener> undoListenerList;
	private ListenerList<IRedoListener> redoListenerList;

	public UndoFireManager() {
		undoListenerList = new ListenerList<IUndoListener>();
		redoListenerList = new ListenerList<IRedoListener>();
	}

	public void addUndoListener(IUndoListener listener) {
		undoListenerList.addListener(listener);
	}

	public void removeUndoListener(IUndoListener listener) {
		undoListenerList.removeListener(listener);
	}

	protected void fireUndoPerformed() {
		for (IUndoListener listener : undoListenerList)
			listener.undoPerformed(new ActionEvent(this, 0, null));
	}

	public void addRedoListener(IRedoListener listener) {
		redoListenerList.addListener(listener);
	}

	public void removeRedoListener(IRedoListener listener) {
		redoListenerList.removeListener(listener);
	}

	protected void fireRedoPerformed() {
		for (IRedoListener listener : redoListenerList)
			listener.redoPerformed(new ActionEvent(this, 0, null));
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		fireUndoPerformed();
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		fireRedoPerformed();
	}

}
