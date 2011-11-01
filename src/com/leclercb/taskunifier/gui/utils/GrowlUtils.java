package com.leclercb.taskunifier.gui.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;

public final class GrowlUtils {
	
	private GrowlUtils() {
		
	}
	
	public static enum GrowlNotificationList {
		
		COMMUNICATOR("Communicator", true),
		REMINDER("Reminder", true),
		SYNCHRONIZATION("Synchronization", true);
		
		private String notificationList;
		private boolean enabled;
		
		private GrowlNotificationList(String notificationList, boolean enabled) {
			this.notificationList = notificationList;
			this.enabled = enabled;
		}
		
		public String getNotificationList() {
			return this.notificationList;
		}
		
		public boolean isEnabled() {
			return this.enabled;
		}
		
		@Override
		public String toString() {
			return this.notificationList;
		}
		
		public static String[] getAllNotificationsList() {
			List<String> list = new ArrayList<String>();
			for (GrowlNotificationList g : GrowlNotificationList.values()) {
				list.add(g.getNotificationList());
			}
			
			return list.toArray(new String[0]);
		}
		
		public static String[] getEnabledNotificationsList() {
			List<String> list = new ArrayList<String>();
			for (GrowlNotificationList g : GrowlNotificationList.values()) {
				if (g.isEnabled())
					list.add(g.getNotificationList());
			}
			
			return list.toArray(new String[0]);
		}
		
	}
	
	private static Growl GROWL;
	
	static {
		if (!SystemUtils.IS_OS_MAC
				|| !Main.SETTINGS.getBooleanProperty("general.growl.enabled")) {
			GROWL = null;
		} else {
			try {
				GROWL = new Growl(
						Constants.TITLE,
						GrowlNotificationList.getAllNotificationsList(),
						GrowlNotificationList.getEnabledNotificationsList());
				
				GuiLogger.getLogger().info("Growl support enabled");
			} catch (Throwable t) {
				GROWL = null;
				GuiLogger.getLogger().warning("Cannot initialize Growl");
			}
		}
	}
	
	public static void notify(GrowlNotificationList list, String title) {
		if (GROWL == null)
			return;
		
		try {
			GROWL.registerApplication();
			GROWL.notify(list.getNotificationList(), title);
		} catch (Throwable t) {
			
		}
	}
	
	public static void notify(
			GrowlNotificationList list,
			String title,
			String description) {
		if (GROWL == null)
			return;
		
		try {
			GROWL.registerApplication();
			GROWL.notify(list.getNotificationList(), title, description);
		} catch (Throwable t) {
			
		}
	}
	
}
