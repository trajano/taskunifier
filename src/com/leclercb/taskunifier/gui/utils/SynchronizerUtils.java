package com.leclercb.taskunifier.gui.utils;

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
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.synchronizer.dummy.DummyGuiPlugin;

public final class SynchronizerUtils {
	
	private SynchronizerUtils() {

	}
	
	public static SynchronizerGuiPlugin getPlugin() {
		String apiId = Main.SETTINGS.getStringProperty("api.id");
		String apiVersion = Main.SETTINGS.getStringProperty("api.version");
		
		if (apiId == null || apiVersion == null)
			return DummyGuiPlugin.getInstance();
		
		List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			if (EqualsUtils.equals(apiId, plugin.getId())
					&& EqualsUtils.equals(apiVersion, plugin.getVersion()))
				return plugin;
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
	
	public static void resetSynchronizer() {
		Main.SETTINGS.setCalendarProperty(
				"synchronizer.last_synchronization_date",
				null);
		
		SynchronizerUtils.getPlugin().getSynchronizerApi().resetSynchronizerParameters(
				Main.SETTINGS);
	}
	
	public static void resetSynchronizerAndDeleteModels() {
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
	}
	
}
