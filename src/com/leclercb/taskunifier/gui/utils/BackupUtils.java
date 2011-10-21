package com.leclercb.taskunifier.gui.utils;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class BackupUtils {
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyyMMdd_HHmmss");
	
	private BackupUtils() {
		
	}
	
	public static boolean checkBackupName(
			String backupName,
			boolean createIfNotExists) {
		if (!backupName.matches("[0-9]{8}_[0-9]{6}"))
			return false;
		
		String folder = Main.BACKUP_FOLDER + File.separator + backupName;
		
		File file = new File(folder);
		if (!file.exists()) {
			if (!createIfNotExists)
				return false;
			
			if (!file.mkdir()) {
				JOptionPane.showMessageDialog(
						null,
						Translations.getString(
								"error.folder_not_a_folder",
								folder),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} else if (!file.isDirectory()) {
			JOptionPane.showMessageDialog(
					null,
					Translations.getString("error.folder_not_a_folder", folder),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	public static Calendar backupNameToDate(String backupName) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(FORMAT.parse(backupName));
			return calendar;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void autoBackup(int nbDays) {
		List<String> list = getBackupList();
		
		if (list.size() == 0) {
			createNewBackup();
			return;
		}
		
		try {
			Calendar calendar = backupNameToDate(list.get(list.size() - 1));
			
			Calendar past = Calendar.getInstance();
			past.add(Calendar.DAY_OF_MONTH, -nbDays);
			
			if (calendar.compareTo(past) <= 0)
				createNewBackup();
		} catch (Exception e) {
			
		}
	}
	
	public static void createNewBackup() {
		String backupName = FORMAT.format(Calendar.getInstance().getTime());
		
		if (!checkBackupName(backupName, true))
			return;
		
		String folder = Main.BACKUP_FOLDER + File.separator + backupName;
		Main.saveAllData(folder);
		GuiLogger.getLogger().info("Backup created: " + backupName);
	}
	
	public static void restoreBackup(String backupName) {
		if (!checkBackupName(backupName, false))
			return;
		
		if (!Synchronizing.setSynchronizing(true))
			return;
		
		String folder = Main.BACKUP_FOLDER + File.separator + backupName;
		Main.loadAllData(folder);
		
		Synchronizing.setSynchronizing(false);
	}
	
	public static void removeBackup(String backupName) {
		if (!checkBackupName(backupName, false))
			return;
		
		String folder = Main.BACKUP_FOLDER + File.separator + backupName;
		
		try {
			FileUtils.deleteDirectory(new File(folder));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		GuiLogger.getLogger().info("Backup removed: " + backupName);
	}
	
	public static void cleanBackups(int nbToKeep) {
		List<String> list = getBackupList();
		
		if (nbToKeep < 1)
			throw new IllegalArgumentException();
		
		while (list.size() > nbToKeep) {
			removeBackup(list.get(0));
			list.remove(0);
		}
	}
	
	public static List<String> getBackupList() {
		File folder = new File(Main.BACKUP_FOLDER);
		File[] backups = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return checkBackupName(pathname.getName(), false);
			}
		});
		
		List<String> list = new ArrayList<String>();
		for (File file : backups) {
			list.add(file.getName());
		}
		
		Collections.sort(list);
		return list;
	}
	
}
