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
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.apache.commons.io.FileUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionManageUsers;
import com.leclercb.taskunifier.gui.actions.ActionSwitchToUser;
import com.leclercb.taskunifier.gui.commons.comparators.UserComparator;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;

public final class UserUtils {
	
	private static UserUtils INSTANCE;
	
	public static UserUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new UserUtils();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private Map<String, String> users;
	
	private UserUtils() {
		this.listChangeSupport = new ListChangeSupport(BackupUtils.class);
		this.users = new HashMap<String, String>();
		this.loadUsers();
	}
	
	private void loadUsers() {
		File file = new File(Main.getDataFolder() + File.separator + "users");
		
		File[] folders = file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
			
		});
		
		for (File folder : folders) {
			this.users.put(
					folder.getName(),
					this.getUserNameFromSettings(folder.getName()));
		}
		
		Main.getUserSettings().addPropertyChangeListener(
				"general.user.name",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						UserUtils.this.setUserName(
								Main.getUserId(),
								Main.getUserSettings().getStringProperty(
										"general.user.name"));
					}
					
				});
	}
	
	private String getUserNameFromSettings(String userId) {
		String userFolder = Main.getUserFolder(userId);
		
		try {
			File file = new File(userFolder
					+ File.separator
					+ "settings.properties");
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			return properties.getProperty("general.user.name");
		} catch (Exception e) {
			return null;
		}
	}
	
	public String[] getUserIds() {
		return this.users.keySet().toArray(new String[0]);
	}
	
	public String getUserName(String userId) {
		return this.users.get(userId);
	}
	
	public void setUserName(String userId, String userName) {
		this.setUserName(userId, userName, true);
	}
	
	private void setUserName(String userId, String userName, boolean fire) {
		if (EqualsUtils.equals(Main.getUserId(), userId)) {
			if (EqualsUtils.equals(
					userName,
					Main.getUserSettings().getStringProperty(
							"general.user.name")))
				return;
			
			Main.getUserSettings().setStringProperty(
					"general.user.name",
					userName);
			this.users.put(userId, userName);
			
			if (fire)
				this.listChangeSupport.fireListChange(
						ListChangeEvent.VALUE_CHANGED,
						-1,
						userId);
			
			return;
		}
		
		String userFolder = Main.getUserFolder(userId);
		
		try {
			File file = new File(userFolder
					+ File.separator
					+ "settings.properties");
			
			if (!file.exists())
				file.createNewFile();
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			
			properties.setProperty("general.user.name", userName);
			
			properties.store(new FileOutputStream(file), Constants.TITLE
					+ " User Settings");
			
			this.users.put(userId, userName);
			
			if (fire)
				this.listChangeSupport.fireListChange(
						ListChangeEvent.VALUE_CHANGED,
						-1,
						userId);
		} catch (Exception e) {
			
		}
	}
	
	public String createNewUser(String userName) {
		String userId = UUID.randomUUID().toString();
		String userFolder = Main.getUserFolder(userId);
		
		try {
			Main.loadFolder(userFolder);
			
			this.setUserName(userId, userName, false);
			
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_ADDED,
					-1,
					userId);
			
			GuiLogger.getLogger().info("User \"" + userName + "\" created");
			
			return userId;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean deleteUser(String userId) {
		if (EqualsUtils.equals(Main.getUserId(), userId))
			return false;
		
		String userName = this.getUserName(userId);
		String userFolder = Main.getUserFolder(userId);
		
		try {
			File file = new File(userFolder);
			if (!file.exists() || !file.isDirectory())
				return true;
			
			FileUtils.deleteDirectory(file);
			this.users.remove(userId);
			
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					-1,
					userId);
			
			GuiLogger.getLogger().info("User \"" + userName + "\" deleted");
		} catch (Exception e) {
			
		}
		
		return true;
	}
	
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	public void fireSwitchedUser() {
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_CHANGED,
				-1,
				Main.getUserId());
	}
	
	public static void updateUserList(JMenu menu) {
		updateUserList(menu, null);
	}
	
	public static void updateUserList(JPopupMenu popupMenu) {
		updateUserList(null, popupMenu);
	}
	
	private static void updateUserList(JMenu menu, JPopupMenu popupMenu) {
		if (menu != null)
			menu.removeAll();
		
		if (popupMenu != null)
			popupMenu.removeAll();
		
		String[] users = UserUtils.getInstance().getUserIds();
		Arrays.sort(users, UserComparator.INSTANCE);
		
		for (String user : users) {
			if (menu != null)
				menu.add(new ActionSwitchToUser(16, 16, user));
			
			if (popupMenu != null)
				popupMenu.add(new ActionSwitchToUser(16, 16, user));
		}
		
		if (menu != null) {
			menu.addSeparator();
			menu.add(new ActionManageUsers(16, 16));
		}
		
		if (popupMenu != null) {
			popupMenu.addSeparator();
			popupMenu.add(new ActionManageUsers(16, 16));
		}
	}
	
}
