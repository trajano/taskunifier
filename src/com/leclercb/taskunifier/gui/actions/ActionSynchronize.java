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

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.configuration.ConfigurationDialog.ConfigurationTab;
import com.leclercb.taskunifier.gui.components.synchronize.BackgroundSynchronizer;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.plugins.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ActionSynchronize extends AbstractAction {
	
	private boolean background;
	
	public ActionSynchronize(boolean background) {
		this(background, 32, 32);
	}
	
	public ActionSynchronize(boolean background, int width, int height) {
		super(
				Translations.getString("action.synchronize"),
				Images.getResourceImage("synchronize.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.synchronize"));
		
		this.background = background;
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionSynchronize.synchronize(this.background);
	}
	
	public static void synchronize(boolean background) {
		if (SynchronizerUtils.getPlugin().getId().equals(
				DummyGuiPlugin.getInstance().getId())) {
			ActionManagePlugins.managePlugins();
			ActionConfiguration.configuration(ConfigurationTab.PLUGIN);
			return;
		}
		
		ViewType.getNoteView().getNoteTableView().commitChanges();
		ViewType.getTaskView().getTaskTableView().commitChanges();
		
		if (background) {
			BackgroundSynchronizer.synchronize();
		} else {
			Note[] notes = ViewType.getNoteView().getNoteTableView().getSelectedNotes();
			Task[] tasks = ViewType.getTaskView().getTaskTableView().getSelectedTasks();
			
			SynchronizerDialog dialog = new SynchronizerDialog();
			dialog.setVisible(true);
			
			ViewType.getNoteView().getNoteTableView().setSelectedNotes(notes);
			ViewType.getTaskView().getTaskTableView().setSelectedTasks(tasks);
		}
	}
	
}
