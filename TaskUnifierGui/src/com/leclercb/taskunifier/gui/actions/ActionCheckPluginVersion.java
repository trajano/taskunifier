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

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.threads.checkpluginversion.CheckPluginVersionThread;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionCheckPluginVersion extends AbstractAction {
	
	private boolean silent;
	
	public ActionCheckPluginVersion(int width, int height, boolean silent) {
		super(
				Translations.getString("action.check_plugin_version"),
				ImageUtils.getResourceImage("download.png", width, height));
		
		this.silent = silent;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.check_plugin_version"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionCheckPluginVersion.checkPluginVersion(this.silent);
	}
	
	public static void checkPluginVersion(boolean silent) {
		if (Synchronizing.getInstance().isSynchronizing()) {
			if (!silent)
				Synchronizing.getInstance().showSynchronizingMessage();
			
			return;
		}
		
		new CheckPluginVersionThread(silent).start();
	}
	
	public static void checkPluginVersion(
			SynchronizerGuiPlugin syncPlugin,
			boolean silent) {
		if (Synchronizing.getInstance().isSynchronizing()) {
			if (!silent)
				Synchronizing.getInstance().showSynchronizingMessage();
			
			return;
		}
		
		new CheckPluginVersionThread(silent, syncPlugin).start();
	}
	
	public static void checkAllPluginVersion(boolean silent) {
		if (Synchronizing.getInstance().isSynchronizing()) {
			if (!silent)
				Synchronizing.getInstance().showSynchronizingMessage();
			
			return;
		}
		
		new CheckPluginVersionThread(
				silent,
				Main.getApiPlugins().getPlugins().toArray(
						new SynchronizerGuiPlugin[0])).start();
	}
	
}
