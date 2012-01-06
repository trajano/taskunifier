package com.leclercb.taskunifier.gui.utils;

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
			users.put(folder.getName(), getUserNameFromSettings(folder.getName()));
		}
	}
	
	private String getUserNameFromSettings(String userId) {
		String userFolder = Main.getUserFolder(userId);
		
		try {
			File file = new File(userFolder + File.separator + "settings.properties");
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			return properties.getProperty("general.user.name");
		} catch (Exception e) {
			return null;
		}
	}
	
	public String[] getUserIds() {
		return users.keySet().toArray(new String[0]);
	}
	
	public String getUserName(String userId) {
		return users.get(userId);
	}
	
	public void setUserName(String userId, String userName) {
		setUserName(userId, userName, true);
	}
	
	private void setUserName(String userId, String userName, boolean fire) {
		String userFolder = Main.getUserFolder(userId);
		
		try {
			File file = new File(userFolder + File.separator + "settings.properties");
			
			if (!file.exists())
				file.createNewFile();
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			
			properties.setProperty("general.user.name", userName);
			
			properties.store(new FileOutputStream(file), Constants.TITLE + " User Settings");
			
			users.put(userId, userName);
			
			if (fire)
				listChangeSupport.fireListChange(ListChangeEvent.VALUE_CHANGED, -1, userId);
		} catch (Exception e) {
			
		}
	}
	
	public String createNewUser(String userName) {
		String userId = UUID.randomUUID().toString();
		String userFolder = Main.getUserFolder(userId);
		
		try {
			Main.loadFolder(userFolder);
			
			setUserName(userId, userName, false);
			
			listChangeSupport.fireListChange(ListChangeEvent.VALUE_ADDED, -1, userId);
			
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
			users.remove(userId);
			
			listChangeSupport.fireListChange(ListChangeEvent.VALUE_REMOVED, -1, userId);
		} catch (Exception e) {
			
		}
		
		return true;
	}
	
}
