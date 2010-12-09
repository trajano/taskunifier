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
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionCut extends AbstractAction {

	public ActionCut() {
		this(32, 32);
	}

	public ActionCut(int width, int height) {
		super(
				Translations.getString("action.name.cut"),
				Images.getResourceImage("cut.png", width, height));

		putValue(SHORT_DESCRIPTION, Translations.getString("action.description.cut"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_T);
		putValue(ACTION_COMMAND_KEY, (String) TransferHandler.getCutAction().getValue(Action.NAME));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Constants.TRANSFER_ACTION_LISTENER.actionPerformed(event);
	}

}
