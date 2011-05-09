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
import com.leclercb.taskunifier.api.models.NoteFactory;
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
				Task.PROP_COMPLETED,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						Task task = (Task) evt.getSource();

						if (task == null || !task.isCompleted())
							return;

						getPlugin().getSynchronizerApi().createRepeatTask(task);
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
		NoteFactory.getInstance().deleteAll();
		TaskFactory.getInstance().deleteAll();

		SynchronizerUtils.getPlugin().getSynchronizerApi().resetSynchronizerParameters(
				Main.SETTINGS);

		Synchronizing.setSynchronizing(false);
	}

}
