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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionScheduledSync extends AbstractAction {
	
	private int width;
	private int height;
	
	public ActionScheduledSync() {
		this(32, 32);
	}
	
	public ActionScheduledSync(int width, int height) {
		super(Translations.getString("action.scheduled_sync"));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.scheduled_sync"));
		
		this.width = width;
		this.height = height;
		
		this.updateIcon();
		
		Main.SETTINGS.addPropertyChangeListener(
				"synchronizer.scheduler_enabled",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ActionScheduledSync.this.updateIcon();
					}
					
				});
	}
	
	private void updateIcon() {
		if (Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled"))
			this.putValue(SMALL_ICON, Images.getResourceImage(
					"synchronize_play.png",
					this.width,
					this.height));
		else
			this.putValue(SMALL_ICON, Images.getResourceImage(
					"synchronize_pause.png",
					this.width,
					this.height));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionScheduledSync.scheduledSync();
	}
	
	public static void scheduledSync() {
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.scheduler_enabled",
				!Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled"));
	}
	
}
