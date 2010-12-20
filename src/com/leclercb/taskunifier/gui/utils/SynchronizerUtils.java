package com.leclercb.taskunifier.gui.utils;

import java.net.Proxy;
import java.util.Calendar;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoSynchronizer;
import com.leclercb.taskunifier.api.utils.ProxyUtils;

public final class SynchronizerUtils {
	
	private SynchronizerUtils() {

	}
	
	public static void initializeProxy() {
		Boolean proxyEnabled = Settings.getBooleanProperty("proxy.enabled");
		if (proxyEnabled != null && proxyEnabled) {
			Proxy.Type type = (Proxy.Type) Settings.getEnumProperty(
					"proxy.type",
					Proxy.Type.class);
			String host = Settings.getStringProperty("proxy.host");
			Integer port = Settings.getIntegerProperty("proxy.port");
			String login = Settings.getStringProperty("proxy.login");
			String password = Settings.getStringProperty("proxy.password");
			
			ProxyUtils.setProxy(type, host, port, login, password);
		} else {
			removeProxy();
		}
	}
	
	public static void removeProxy() {
		ProxyUtils.removeProxy();
	}
	
	public static void keepTasksCompletedForXDaysHasChanged() {
		Settings.setCalendarProperty("synchronizer.last_task_edit", null);
	}
	
	public static void initializeSynchronizer(ToodledoSynchronizer synchronizer) {
		synchronizer.setKeepTasksCompletedForXDays(Settings.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days"));
		
		synchronizer.setLastContextEdit(Settings.getCalendarProperty("synchronizer.last_context_edit"));
		synchronizer.setLastFolderEdit(Settings.getCalendarProperty("synchronizer.last_folder_edit"));
		synchronizer.setLastGoalEdit(Settings.getCalendarProperty("synchronizer.last_goal_edit"));
		synchronizer.setLastLocationEdit(Settings.getCalendarProperty("synchronizer.last_location_edit"));
		synchronizer.setLastTaskEdit(Settings.getCalendarProperty("synchronizer.last_task_edit"));
		synchronizer.setLastTaskDelete(Settings.getCalendarProperty("synchronizer.last_task_delete"));
	}
	
	public static void saveSynchronizerState(ToodledoSynchronizer synchronizer) {
		Settings.setCalendarProperty(
				"synchronizer.last_synchronization_date",
				Calendar.getInstance());
		
		Settings.setIntegerProperty(
				"synchronizer.keep_tasks_completed_for_x_days",
				synchronizer.getKeepTasksCompletedForXDays());
		
		Settings.setCalendarProperty(
				"synchronizer.last_context_edit",
				synchronizer.getLastContextEdit());
		Settings.setCalendarProperty(
				"synchronizer.last_folder_edit",
				synchronizer.getLastFolderEdit());
		Settings.setCalendarProperty(
				"synchronizer.last_goal_edit",
				synchronizer.getLastGoalEdit());
		Settings.setCalendarProperty(
				"synchronizer.last_location_edit",
				synchronizer.getLastLocationEdit());
		Settings.setCalendarProperty(
				"synchronizer.last_task_edit",
				synchronizer.getLastTaskEdit());
		Settings.setCalendarProperty(
				"synchronizer.last_task_delete",
				synchronizer.getLastTaskDelete());
	}
	
	public static void resetSynchronizerSettings() {
		Settings.setCalendarProperty(
				"synchronizer.last_synchronization_date",
				null);
		
		Settings.setCalendarProperty("synchronizer.last_context_edit", null);
		Settings.setCalendarProperty("synchronizer.last_folder_edit", null);
		Settings.setCalendarProperty("synchronizer.last_goal_edit", null);
		Settings.setCalendarProperty("synchronizer.last_location_edit", null);
		Settings.setCalendarProperty("synchronizer.last_task_edit", null);
		Settings.setCalendarProperty("synchronizer.last_task_delete", null);
	}
	
}
