package com.leclercb.taskunifier.gui.utils;

import java.net.Proxy;
import java.util.GregorianCalendar;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.toodledo.ToodledoProxy;
import com.leclercb.taskunifier.api.toodledo.ToodledoSynchronizer;

public final class SynchronizerUtils {

	private SynchronizerUtils() {

	}

	public static void initializeProxy() {
		Boolean proxyEnabled = Settings.getBooleanProperty("proxy.enabled");
		if (proxyEnabled != null && proxyEnabled) {
			Proxy.Type type = (Proxy.Type) Settings.getEnumProperty("proxy.type", Proxy.Type.class);
			String host = Settings.getStringProperty("proxy.host");
			Integer port = Settings.getIntegerProperty("proxy.port");
			String login = Settings.getStringProperty("proxy.login");
			String password = Settings.getStringProperty("proxy.password");

			ToodledoProxy.setProxy(type, host, port, login, password);
		}
	}

	public static void initializeSynchronizer(ToodledoSynchronizer synchronizer) {
		synchronizer.setKeepTasksCompletedForXDays(Settings.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days"));
		synchronizer.setLastContextEdit(Settings.getCalendarProperty("synchronizer.last_context_edit"));
		synchronizer.setLastFolderEdit(Settings.getCalendarProperty("synchronizer.last_folder_edit"));
		synchronizer.setLastGoalEdit(Settings.getCalendarProperty("synchronizer.last_goal_edit"));
		synchronizer.setLastTaskAddEdit(Settings.getCalendarProperty("synchronizer.last_task_add_edit"));
		synchronizer.setLastTaskDelete(Settings.getCalendarProperty("synchronizer.last_task_delete"));
	}

	public static void saveSynchronizerState(ToodledoSynchronizer synchronizer) {
		Settings.setCalendarProperty("synchronizer.last_synchronization_date", GregorianCalendar.getInstance());
		Settings.setIntegerProperty("synchronizer.keep_tasks_completed_for_x_days", synchronizer.getKeepTasksCompletedForXDays());
		Settings.setCalendarProperty("synchronizer.last_context_edit", synchronizer.getLastContextEdit());
		Settings.setCalendarProperty("synchronizer.last_folder_edit", synchronizer.getLastFolderEdit());
		Settings.setCalendarProperty("synchronizer.last_goal_edit", synchronizer.getLastGoalEdit());
		Settings.setCalendarProperty("synchronizer.last_task_add_edit", synchronizer.getLastTaskAddEdit());
		Settings.setCalendarProperty("synchronizer.last_task_delete", synchronizer.getLastTaskDelete());
	}

	public static void removeProxy() {
		ToodledoProxy.removeProxy();
	}

	public static void resetSynchronizerSettings() {
		Settings.setCalendarProperty("synchronizer.last_context_edit", null);
		Settings.setCalendarProperty("synchronizer.last_folder_edit", null);
		Settings.setCalendarProperty("synchronizer.last_goal_edit", null);
		Settings.setCalendarProperty("synchronizer.last_task_add_edit", null);
		Settings.setCalendarProperty("synchronizer.last_task_delete", null);
		Settings.setCalendarProperty("synchronizer.last_synchronization_date", null);
	}

}
