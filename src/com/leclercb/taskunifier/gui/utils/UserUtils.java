package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.utils.EqualsUtils;
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
		
		Main.getUserSettings().addPropertyChangeListener("general.user.name", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setUserName(Main.getUserId(), Main.getUserSettings().getStringProperty("general.user.name")); 
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
			if (EqualsUtils.equals(userName, Main.getUserSettings().getStringProperty("general.user.name")))
				return;
			
			Main.getUserSettings().setStringProperty("general.user.name", userName);
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
			
			return userId;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean deleteUser(String userId) {
		if (EqualsUtils.equals(Main.getUserId(), userId))
			return false;
		
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
		} catch (Exception e) {
			
		}
		
		return true;
	}
	
}
