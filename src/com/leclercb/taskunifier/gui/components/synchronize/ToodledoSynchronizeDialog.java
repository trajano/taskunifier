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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.awt.Frame;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerConnection;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoApi;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ToodledoSynchronizeDialog extends SynchronizeDialog {
	
	public ToodledoSynchronizeDialog(Frame frame, boolean modal) {
		super(frame, modal);
	}
	
	@Override
	protected void initializeApi() {
		ToodledoApi.INSTANCE.setApplicationId("taskunifier");
		ToodledoApi.INSTANCE.setVersion(Constants.VERSION);
		ToodledoApi.INSTANCE.setApiKey("api4cff3de7e1c00");
	}
	
	@Override
	protected SynchronizerConnection getConnection()
			throws SynchronizerException {
		return SynchronizerUtils.getApi().getConnection(
				new Object[] {
						Main.SETTINGS.getStringProperty("toodledo.email"),
						Main.SETTINGS.getStringProperty("toodledo.password") });
	}
	
}
