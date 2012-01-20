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
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
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
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizingException;
import com.leclercb.taskunifier.gui.main.Main;

public final class SynchronizerUtils {
	
	private SynchronizerUtils() {
		
	}
	
	private static boolean TASK_REPEAT_ENABLED = false;
	
	static {
		TaskFactory.getInstance().addPropertyChangeListener(
				Task.PROP_COMPLETED,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (!TASK_REPEAT_ENABLED)
							return;
						
						Task task = (Task) evt.getSource();
						
						if (task == null || !task.isCompleted())
							return;
						
						boolean set = false;
						
						try {
							set = Synchronizing.setSynchronizing(true);
						} catch (SynchronizingException e) {
							
						}
						
						getSynchronizerPlugin().getSynchronizerApi().createRepeatTask(
								task);
						
						if (set) {
							try {
								Synchronizing.setSynchronizing(false);
							} catch (SynchronizingException e) {
								
							}
						}
					}
					
				});
	}
	
	public static void setTaskRepeatEnabled(boolean enabled) {
		TASK_REPEAT_ENABLED = enabled;
	}
	
	public static SynchronizerGuiPlugin getPlugin(String pluginId) {
		if (pluginId == null)
			return null;
		
		List<SynchronizerGuiPlugin> plugins = Main.getApiPlugins().getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			if (EqualsUtils.equals(pluginId, plugin.getId())) {
				return plugin;
			}
		}
		
		return null;
	}
	
	public static SynchronizerGuiPlugin[] getPublisherPlugins() {
		String value = Main.getUserSettings().getStringProperty(
				"plugin.publisher.ids");
		
		if (value == null)
			return new SynchronizerGuiPlugin[0];
		
		String[] ids = StringUtils.split(value, ";");
		List<SynchronizerGuiPlugin> plugins = new ArrayList<SynchronizerGuiPlugin>();
		for (String id : ids) {
			SynchronizerGuiPlugin plugin = getPlugin(id.trim());
			
			if (plugin == null || !plugin.isPublisher())
				continue;
			
			plugins.add(plugin);
		}
		
		return plugins.toArray(new SynchronizerGuiPlugin[0]);
	}
	
	public static void addPublisherPlugin(SynchronizerGuiPlugin plugin) {
		if (plugin == null || !plugin.isPublisher())
			return;
		
		String value = Main.getUserSettings().getStringProperty(
				"plugin.publisher.ids");
		
		if (value == null)
			value = "";
		
		String[] ids = StringUtils.split(value, ";");
		List<String> listIds = new ArrayList<String>();
		
		for (String id : ids) {
			if (id.trim().length() == 0)
				continue;
			
			listIds.add(id);
		}
		
		listIds.add(plugin.getId());
		
		Main.getUserSettings().setStringProperty(
				"plugin.publisher.ids",
				StringUtils.join(listIds, ";"));
	}
	
	public static void removePublisherPlugin(SynchronizerGuiPlugin plugin) {
		if (plugin == null || !plugin.isPublisher())
			return;
		
		String value = Main.getUserSettings().getStringProperty(
				"api.publisher.ids");
		
		if (value == null)
			value = "";
		
		String[] ids = StringUtils.split(value, ";");
		List<String> listIds = new ArrayList<String>();
		
		for (String id : ids) {
			if (id.trim().length() == 0)
				continue;
			
			listIds.add(id);
		}
		
		listIds.remove(plugin.getId());
		
		Main.getUserSettings().setStringProperty(
				"api.publisher.ids",
				StringUtils.join(listIds, ";"));
	}
	
	public static SynchronizerGuiPlugin getSynchronizerPlugin() {
		SynchronizerGuiPlugin plugin = getPlugin(Main.getUserSettings().getStringProperty(
				"plugin.synchronizer.id"));
		
		if (plugin == null || !plugin.isSynchronizer())
			return DummyGuiPlugin.getInstance();
		
		return plugin;
	}
	
	public static void setSynchronizerPlugin(SynchronizerGuiPlugin plugin) {
		if (plugin == null || !plugin.isSynchronizer())
			return;
		
		Main.getUserSettings().setStringProperty(
				"plugin.synchronizer.id",
				plugin.getId());
	}
	
	public static void initializeProxy() {
		SynchronizerGuiPlugin plugin = getSynchronizerPlugin();
		
		if (!Main.getSettings().getBooleanProperty("proxy.use_system_proxies")
				&& Main.getSettings().getBooleanProperty("proxy.enabled")) {
			final String host = Main.getSettings().getStringProperty(
					"proxy.host",
					"");
			final Integer port = Main.getSettings().getIntegerProperty(
					"proxy.port",
					0);
			final String login = Main.getSettings().getStringProperty(
					"proxy.login",
					"");
			final String password = Main.getSettings().getStringProperty(
					"proxy.password",
					"");
			
			try {
				System.setProperty("http.proxyHost", host);
				System.setProperty("http.proxyPort", port + "");
				System.setProperty("http.proxyUser", login);
				System.setProperty("http.proxyPassword", password);
				
				System.setProperty("https.proxyHost", host);
				System.setProperty("https.proxyPort", port + "");
				System.setProperty("https.proxyUser", login);
				System.setProperty("https.proxyPassword", password);
				
				if (login.length() != 0) {
					Authenticator.setDefault(new Authenticator() {
						
						@Override
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									login,
									password.toCharArray());
						}
						
					});
				}
			} catch (Throwable t) {
				GuiLogger.getLogger().log(
						Level.WARNING,
						"Cannot set java proxy",
						t);
			}
			
			plugin.getSynchronizerApi().setProxyHost(host);
			plugin.getSynchronizerApi().setProxyPort(port);
			plugin.getSynchronizerApi().setProxyUsername(login);
			plugin.getSynchronizerApi().setProxyPassword(password);
		} else {
			removeProxy();
		}
	}
	
	public static void removeProxy() {
		SynchronizerGuiPlugin plugin = getSynchronizerPlugin();
		
		plugin.getSynchronizerApi().setProxyHost(null);
		plugin.getSynchronizerApi().setProxyPort(0);
		plugin.getSynchronizerApi().setProxyUsername(null);
		plugin.getSynchronizerApi().setProxyPassword(null);
	}
	
	public static void removeOldCompletedTasks() {
		int keep = Main.getUserSettings().getIntegerProperty(
				"synchronizer.keep_tasks_completed_for_x_days");
		
		Calendar completedAfter = Calendar.getInstance();
		completedAfter.add(Calendar.DAY_OF_MONTH, -keep);
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			if (task.isCompleted()
					&& task.getCompletedOn().compareTo(completedAfter) < 0) {
				Task[] children = task.getAllChildren();
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
		SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().resetConnectionParameters(
				Main.getUserSettings());
	}
	
	public static void resetSynchronizer() {
		Main.getUserSettings().setCalendarProperty(
				"synchronizer.last_synchronization_date",
				null);
		
		SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().resetSynchronizerParameters(
				Main.getUserSettings());
	}
	
	public static void resetSynchronizerAndDeleteModels() {
		boolean set = false;
		
		try {
			set = Synchronizing.setSynchronizing(true);
		} catch (SynchronizingException e) {
			
		}
		
		if (!set) {
			return;
		}
		
		Main.getUserSettings().setCalendarProperty(
				"synchronizer.last_synchronization_date",
				null);
		
		// TODO: Delete all contacts ?
		// ContactFactory.getInstance().deleteAll();
		ContextFactory.getInstance().deleteAll();
		FolderFactory.getInstance().deleteAll();
		GoalFactory.getInstance().deleteAll();
		LocationFactory.getInstance().deleteAll();
		NoteFactory.getInstance().deleteAll();
		TaskFactory.getInstance().deleteAll();
		
		SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().resetSynchronizerParameters(
				Main.getUserSettings());
		
		if (set) {
			try {
				Synchronizing.setSynchronizing(false);
			} catch (SynchronizingException e) {
				
			}
		}
	}
	
}
