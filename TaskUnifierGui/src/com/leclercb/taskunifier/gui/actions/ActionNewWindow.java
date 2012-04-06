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

import com.leclercb.taskunifier.gui.components.views.DefaultCalendarView;
import com.leclercb.taskunifier.gui.components.views.DefaultNoteView;
import com.leclercb.taskunifier.gui.components.views.DefaultTaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.main.frames.FrameView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionNewWindow extends AbstractAction {
	
	public ActionNewWindow(int width, int height) {
		super(
				Translations.getString("action.new_window"),
				ImageUtils.getResourceImage("window_add.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.new_window"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_A,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionNewWindow.newWindow();
	}
	
	public static void newWindow() {
		newWindow(true);
	}
	
	public static void newWindow(boolean createTabs) {
		FrameView frameView = FrameUtils.createFrameView();
		
		if (createTabs) {
			ViewItem viewItem = null;
			
			viewItem = new ViewItem(
					ViewType.TASKS,
					Translations.getString("general.tasks"),
					ImageUtils.getResourceImage("task.png", 16, 16),
					frameView.getFrameId());
			viewItem.setView(new DefaultTaskView());
			
			ViewList.getInstance().addView(viewItem);
			ViewList.getInstance().setCurrentView(viewItem);
			
			viewItem = new ViewItem(
					ViewType.NOTES,
					Translations.getString("general.notes"),
					ImageUtils.getResourceImage("note.png", 16, 16),
					frameView.getFrameId());
			viewItem.setView(new DefaultNoteView());
			
			ViewList.getInstance().addView(viewItem);
			
			viewItem = new ViewItem(
					ViewType.CALENDAR,
					Translations.getString("general.calendar"),
					ImageUtils.getResourceImage("calendar.png", 16, 16),
					frameView.getFrameId());
			viewItem.setView(new DefaultCalendarView());
			
			ViewList.getInstance().addView(viewItem);
		}
	}
	
}
