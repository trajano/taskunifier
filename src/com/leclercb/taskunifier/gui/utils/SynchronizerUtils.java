package com.leclercb.taskunifier.gui.utils;

import java.awt.Frame;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.utils.ProxyUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoApi;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizeDialog;
import com.leclercb.taskunifier.gui.components.synchronize.ToodledoSynchronizeDialog;

public final class SynchronizerUtils {
	
	private SynchronizerUtils() {

	}
	
	public static SynchronizerApi getApi() {
		return ToodledoApi.INSTANCE;
	}
	
	public static SynchronizeDialog getSynchronizeDialog(
			Frame frame,
			boolean modal) {
		return new ToodledoSynchronizeDialog(frame, modal);
	}
	
	public static void initializeProxy() {
		Boolean proxyEnabled = Main.SETTINGS.getBooleanProperty("proxy.enabled");
		if (proxyEnabled != null && proxyEnabled) {
			Boolean useSystemProxy = Main.SETTINGS.getBooleanProperty("proxy.use_system_proxy");
			if (useSystemProxy != null && useSystemProxy) {
				ProxyUtils.useSystemProxy();
			} else {
				Proxy.Type type = (Proxy.Type) Main.SETTINGS.getEnumProperty(
						"proxy.type",
						Proxy.Type.class);
				String host = Main.SETTINGS.getStringProperty("proxy.host");
				Integer port = Main.SETTINGS.getIntegerProperty("proxy.port");
				String login = Main.SETTINGS.getStringProperty("proxy.login");
				String password = Main.SETTINGS.getStringProperty("proxy.password");
				
				ProxyUtils.setProxy(type, host, port, login, password);
			}
		} else {
			removeProxy();
		}
	}
	
	public static void removeProxy() {
		ProxyUtils.removeProxy();
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
	
}
