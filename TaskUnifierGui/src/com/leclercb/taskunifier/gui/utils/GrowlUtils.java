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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

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
				|| !Main.getSettings().getBooleanProperty(
						"general.growl.enabled")) {
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
		notify(list, title, "");
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
