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
package com.leclercb.taskunifier.gui.constants;

import javax.swing.undo.UndoableEditSupport;

import com.leclercb.taskunifier.gui.undo.TransferActionListener;
import com.leclercb.taskunifier.gui.undo.UndoFireManager;

public final class Constants {
	
	private Constants() {

	}
	
	public static final String TITLE = "TaskUnifier";
	public static final String VERSION = "0.6.2";
	
	public static final String VERSION_FILE = "http://taskunifier.sourceforge.net/version.txt";
	public static final String DOWNLOAD_URL = "http://sourceforge.net/projects/taskunifier/files/";
	
	public static final UndoFireManager UNDO_MANAGER = new UndoFireManager();
	public static final UndoableEditSupport UNDO_EDIT_SUPPORT = new UndoableEditSupport();
	
	public static final TransferActionListener TRANSFER_ACTION_LISTENER = new TransferActionListener();
	
	static {
		UNDO_EDIT_SUPPORT.addUndoableEditListener(UNDO_MANAGER);
	}
	
}
