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

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.main.View;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ActionChangeView extends AbstractAction {
	
	private MainView view;
	
	private int width;
	private int height;
	
	public ActionChangeView(MainView view) {
		this(view, 32, 32);
	}
	
	public ActionChangeView(MainView view, int width, int height) {
		super(Translations.getString("action.change_view"));
		
		this.view = view;
		
		this.width = width;
		this.height = height;
		
		this.putValue(
				ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_MASK));
		
		this.updateIcon();
		
		view.addPropertyChangeListener(
				MainView.PROP_SELECTED_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ActionChangeView.this.updateIcon();
					}
					
				});
	}
	
	private void updateIcon() {
		View view = this.view.getSelectedView();
		switch (view) {
			case NOTES:
				this.putValue(SMALL_ICON, Images.getResourceImage(
						"note.png",
						this.width,
						this.height));
				break;
			case TASKS:
				this.putValue(SMALL_ICON, Images.getResourceImage(
						"task.png",
						this.width,
						this.height));
				break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionChangeView.changeView();
	}
	
	public static void changeView() {
		int nextView = MainFrame.getInstance().getSelectedView().ordinal();
		nextView = (nextView + 1) % View.values().length;
		MainFrame.getInstance().setSelectedView(View.values()[nextView]);
	}
	
}
