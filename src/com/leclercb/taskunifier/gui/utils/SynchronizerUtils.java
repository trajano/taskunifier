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
package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;

public final class SynchronizerUtils {
	
	private SynchronizerUtils() {

	}
	
	public static void initializeTaskRepeat() {
		TaskFactory.getInstance().addPropertyChangeListener(
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (Task.PROP_COMPLETED.equals(evt.getPropertyName())) {
							Task task = (Task) evt.getSource();
							
							if (task == null)
								return;
							
							getPlugin().getSynchronizerApi().createRepeatTask(
									task);
						}
					}
					
				});
	}
	
	public static SynchronizerGuiPlugin getPlugin() {
		String apiId = Main.SETTINGS.getStringProperty("api.id");
		
		if (apiId == null)
			return DummyGuiPlugin.getInstance();
		
		List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			if (EqualsUtils.equals(apiId, plugin.getId())) {
				return plugin;
			}
		}
		
		return DummyGuiPlugin.getInstance();
	}
	
	public static void initializeProxy() {
		Boolean proxyEnabled = Main.SETTINGS.getBooleanProperty("proxy.enabled");
		if (proxyEnabled != null && proxyEnabled) {
			String host = Main.SETTINGS.getStringProperty("proxy.host");
			Integer port = Main.SETTINGS.getIntegerProperty("proxy.port");
			String login = Main.SETTINGS.getStringProperty("proxy.login");
			String password = Main.SETTINGS.getStringProperty("proxy.password");
			
			getPlugin().getSynchronizerApi().setProxyHost(host);
			getPlugin().getSynchronizerApi().setProxyPort(port);
			getPlugin().getSynchronizerApi().setProxyUsername(login);
			getPlugin().getSynchronizerApi().setProxyPassword(password);
		} else {
			removeProxy();
		}
	}
	
	public static void removeProxy() {
		getPlugin().getSynchronizerApi().setProxyHost(null);
		getPlugin().getSynchronizerApi().setProxyPort(0);
		getPlugin().getSynchronizerApi().setProxyUsername(null);
		getPlugin().getSynchronizerApi().setProxyPassword(null);
	}
	
	public static void removeOldCompletedTasks() {
		Integer keep = Main.SETTINGS.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days");
		
		if (keep == null)
			return;
		
		Calendar completedAfter = Calendar.getInstance();
		completedAfter.add(Calendar.DAY_OF_MONTH, -keep);
		
		List<Task> tasks = new ArrayList<Task>(
				TaskFactory.getInstance().getList());
		
		for (Task task : tasks) {
			if (task.isCompleted()
					&& task.getCompletedOn().compareTo(completedAfter) < 0) {
				List<Task> children = TaskFactory.getInstance().getChildren(
						task);
				boolean delete = true;
				
				for (Task child : children) {
					if (!(child.isCompleted() && child.getCompletedOn().compareTo(
							completedAfter) < 0)) {
						delete = false;
						break;
					}
				}
				
				if (delete)
					TaskFactory.getInstance().markDeleted(task);
			}
		}
	}
	
	public static void resetConnection() {
		SynchronizerUtils.getPlugin().getSynchronizerApi().resetConnectionParameters(
				Main.SETTINGS);
	}
	
	public static void resetSynchronizer() {
		Main.SETTINGS.setCalendarProperty(
				"synchronizer.last_synchronization_date",
				null);
		
		SynchronizerUtils.getPlugin().getSynchronizerApi().resetSynchronizerParameters(
				Main.SETTINGS);
	}
	
	public static void resetSynchronizerAndDeleteModels() {
		Synchronizing.setSynchronizing(true);
		
		Main.SETTINGS.setCalendarProperty(
				"synchronizer.last_synchronization_date",
				null);
		
		ContextFactory.getInstance().deleteAll();
		FolderFactory.getInstance().deleteAll();
		GoalFactory.getInstance().deleteAll();
		LocationFactory.getInstance().deleteAll();
		TaskFactory.getInstance().deleteAll();
		
		SynchronizerUtils.getPlugin().getSynchronizerApi().resetSynchronizerParameters(
				Main.SETTINGS);
		
		Synchronizing.setSynchronizing(false);
	}
	
}
