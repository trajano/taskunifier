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
package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import java.util.Properties;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;

public class DummyApi extends SynchronizerApi {
	
	private static DummyApi INSTANCE;
	
	public static final DummyApi getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DummyApi();
		
		return INSTANCE;
	}
	
	protected DummyApi() {
		super("DUMMY", "No Service", "http://www.taskunifier.net");
	}
	
	@Override
	public String[] getDefaultRepeatValues() {
		return new String[] {
				"Daily",
				"Weekly",
				"Biweekly",
				"Monthly",
				"Bimonthly",
				"Quarterly",
				"Semiannually",
				"Yearly" };
	}
	
	@Override
	public boolean isValidRepeatValue(String repeat) {
		return RepeatUtils.isValidRepeatValue(repeat);
	}
	
	@Override
	public void createRepeatTask(Task task) {
		RepeatUtils.createRepeatTask(task);
	}
	
	@Override
	public void createAccount(Object[] parameters) throws SynchronizerException {

	}
	
	@Override
	public Connection getConnection(Object[] parameters)
			throws SynchronizerException {
		return null;
	}
	
	@Override
	public Synchronizer getSynchronizer(Connection connection) {
		return null;
	}
	
	@Override
	public void resetConnectionParameters(Properties properties) {

	}
	
	@Override
	public void resetSynchronizerParameters(Properties properties) {

	}
	
}
