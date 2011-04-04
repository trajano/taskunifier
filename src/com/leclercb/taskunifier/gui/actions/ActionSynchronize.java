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
package com.leclercb.taskunifier.gui.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.synchronize.BackgroundSynchronizer;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionSynchronize extends AbstractAction {
	
	private boolean background;
	
	public ActionSynchronize(boolean background) {
		this(background, 32, 32);
	}
	
	public ActionSynchronize(boolean background, int width, int height) {
		super(
				Translations.getString("action.name.synchronize"),
				Images.getResourceImage("synchronize.png", width, height));
		
		this.background = background;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.synchronize"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionSynchronize.synchronize(this.background);
	}
	
	public static void synchronize(boolean background) {
		if (background) {
			BackgroundSynchronizer synchronizer = new BackgroundSynchronizer();
			synchronizer.synchronize();
		} else {
			Task[] tasks = MainFrame.getInstance().getTaskView().getSelectedTasks();
			
			SynchronizerDialog dialog = new SynchronizerDialog(
					MainFrame.getInstance().getFrame());
			dialog.setVisible(true);
			
			// MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
			MainFrame.getInstance().getTaskView().setSelectedTasks(tasks);
		}
	}
	
}
