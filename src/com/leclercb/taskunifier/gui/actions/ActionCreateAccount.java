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

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoConnectionFactory;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ActionCreateAccount extends AbstractAction {

	public ActionCreateAccount() {
		this(32, 32);
	}

	public ActionCreateAccount(int width, int height) {
		super(
				Translations.getString("action.name.create_account"),
				Images.getResourceImage("user.png", width, height));

		putValue(SHORT_DESCRIPTION, Translations.getString("action.description.create_account"));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String email = Settings.getStringProperty("toodledo.email");
		String password = Settings.getStringProperty("toodledo.password");

		try {
			if (email == null)
				throw new Exception(Translations.getString("error.empty_email"));

			if (password == null)
				throw new Exception(Translations.getString("error.empty_password"));

			SynchronizerUtils.initializeProxy();
			ToodledoConnectionFactory.getInstance().createAccount(email, password);

			JOptionPane.showMessageDialog(
					null, 
					Translations.getString("action.create_account.account_created"), 
					Translations.getString("general.information"), 
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null, 
					e.getMessage(), 
					Translations.getString("error.account_not_created"), 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

}
