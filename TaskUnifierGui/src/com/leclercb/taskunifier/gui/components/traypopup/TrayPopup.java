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
package com.leclercb.taskunifier.gui.components.traypopup;

import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import com.leclercb.taskunifier.gui.actions.ActionCreateNoteFromClipboard;
import com.leclercb.taskunifier.gui.actions.ActionCreateTaskFromClipboard;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TrayPopup extends PopupMenu {
	
	public TrayPopup() {
		this(true);
	}
	
	public TrayPopup(boolean showQuitAction) {
		this.initialize(showQuitAction);
	}
	
	private void initialize(boolean showQuitAction) {
		Action action = null;
		MenuItem item = null;
		
		item = new MenuItem(Translations.getString("general.open"));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				MainFrame.getInstance().getFrame().setVisible(true);
				MainFrame.getInstance().getFrame().setState(Frame.NORMAL);
			}
			
		});
		this.add(item);
		
		this.addSeparator();
		
		action = new ActionCreateTaskFromClipboard();
		item = new MenuItem((String) action.getValue(Action.NAME));
		item.addActionListener(action);
		this.add(item);
		
		action = new ActionCreateNoteFromClipboard();
		item = new MenuItem((String) action.getValue(Action.NAME));
		item.addActionListener(action);
		this.add(item);
		
		if (showQuitAction) {
			this.addSeparator();
			
			action = new ActionQuit();
			item = new MenuItem((String) action.getValue(Action.NAME));
			item.addActionListener(action);
			this.add(item);
		}
	}
	
}
