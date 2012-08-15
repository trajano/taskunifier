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

import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.components.synchronize.BackgroundSynchronizer;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerWorker;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerWorker.Type;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ActionSynchronize extends AbstractAction {
	
	private boolean background;
	
	public ActionSynchronize(int width, int height, boolean background) {
		super(
				Translations.getString("action.synchronize"),
				ImageUtils.getResourceImage("synchronize.png", width, height));
		
		this.background = background;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.synchronize"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionSynchronize.synchronize(this.background, true);
	}
	
	public static void synchronize(boolean background) {
		synchronize(background, false);
	}
	
	public static void synchronize(boolean background, boolean userAction) {
		if (Synchronizing.getInstance().isSynchronizing()) {
			if (!background)
				Synchronizing.getInstance().showSynchronizingMessage();
			
			return;
		}
		
		boolean isDummyPlugin = SynchronizerUtils.getSynchronizerPlugin().getId().equals(
				DummyGuiPlugin.getInstance().getId());
		
		if (isDummyPlugin) {
			if (background || !userAction)
				return;
			
			ActionManageSynchronizerPlugins.manageSynchronizerPlugins();
			return;
		}
		
		ViewUtils.commitAll();
		
		if (background) {
			SynchronizerWorker worker = BackgroundSynchronizer.getSynchronizer();
			
			worker.add(
					SynchronizerUtils.getSynchronizerPlugin(),
					Type.SYNCHRONIZE);
			
			BackgroundSynchronizer.execute(worker);
		} else {
			SynchronizerDialog dialog = new SynchronizerDialog();
			
			dialog.add(
					SynchronizerUtils.getSynchronizerPlugin(),
					Type.SYNCHRONIZE);
			
			dialog.setVisible(true);
		}
	}
	
}
