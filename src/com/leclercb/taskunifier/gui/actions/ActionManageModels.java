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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.models.ModelConfigurationDialog;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionManageModels extends AbstractAction {

	public ActionManageModels() {
		this(32, 32);
	}

	public ActionManageModels(int width, int height) {
		super(
				Translations.getString("action.name.manage_models"), 
				Images.getImage("folder.png", 32, 32));

		putValue(SHORT_DESCRIPTION, Translations.getString("action.description.manage_models"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_M);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ModelConfigurationDialog config = new ModelConfigurationDialog(
				MainFrame.getInstance().getFrame(),
				true);

		config.setVisible(true);
	}

}
