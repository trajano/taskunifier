package com.leclercb.taskunifier.gui.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class UserUtils {
	
	private UserUtils() {
		
	}
	
	public static String[] getUserIds() {
		File file = new File(Main.getDataFolder() + File.separator + "users");
		
		File[] folders = file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
			
		});
		
		List<String> userIds = new ArrayList<String>();
		for (File folder : folders) {
			userIds.add(folder.getName());
		}
		
		return userIds.toArray(new String[0]);
	}
	
	public static String createNewUser() throws Exception {
		String userId = UUID.randomUUID().toString();
		String userFolder = Main.getUserFolder(userId);
		Main.loadFolder(userFolder);
		return userId;
	}
	
	public static boolean deleteUser(String userId) {
		if (EqualsUtils.equals(Main.getUserId(), userId))
			return false;
		
		String userFolder = Main.getUserFolder(userId);
		
		try {
			File file = new File(userFolder);
			if (!file.exists() || !file.isDirectory())
				return true;
			
			FileUtils.deleteDirectory(file);
		} catch (Exception e) {
			
		}
		
		return true;
	}
	
}
