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

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class BackupUtils {
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyyMMdd_HHmmss");
	
	private static BackupUtils INSTANCE;
	
	public static BackupUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BackupUtils();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private BackupUtils() {
		this.listChangeSupport = new ListChangeSupport(BackupUtils.class);
	}
	
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	public boolean checkBackupName(String backupName, boolean createIfNotExists) {
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
	
	public Calendar backupNameToDate(String backupName) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(FORMAT.parse(backupName));
			return calendar;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void autoBackup(int nbHours) {
		List<String> list = this.getBackupList();
		
		if (list.size() == 0) {
			this.createNewBackup();
			return;
		}
		
		try {
			Calendar calendar = this.backupNameToDate(list.get(list.size() - 1));
			
			Calendar past = Calendar.getInstance();
			past.add(Calendar.HOUR_OF_DAY, -nbHours);
			
			if (calendar.compareTo(past) <= 0)
				this.createNewBackup();
		} catch (Exception e) {
			
		}
	}
	
	public boolean createNewBackup() {
		String backupName = FORMAT.format(Calendar.getInstance().getTime());
		
		if (!this.checkBackupName(backupName, true))
			return false;
		
		String folder = Main.BACKUP_FOLDER + File.separator + backupName;
		
		try {
			if (!Synchronizing.setSynchronizing(true))
				return false;
			
			Main.saveAllData(folder);
		} finally {
			Synchronizing.setSynchronizing(false);
		}
		
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				0,
				backupName);
		GuiLogger.getLogger().info("Backup created: " + backupName);
		
		return true;
	}
	
	public boolean restoreBackup(String backupName) {
		if (!this.checkBackupName(backupName, false))
			return false;
		
		if (!Synchronizing.setSynchronizing(true))
			return false;
		
		SynchronizerUtils.resetSynchronizerAndDeleteModels();
		String folder = Main.BACKUP_FOLDER + File.separator + backupName;
		Main.loadAllData(folder);
		
		Synchronizing.setSynchronizing(false);
		
		return true;
	}
	
	public void removeBackup(String backupName) {
		if (!this.checkBackupName(backupName, false))
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
		
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_REMOVED,
				0,
				backupName);
		GuiLogger.getLogger().info("Backup removed: " + backupName);
	}
	
	public void cleanBackups(int nbToKeep) {
		List<String> list = this.getBackupList();
		
		if (nbToKeep < 1)
			throw new IllegalArgumentException();
		
		while (list.size() > nbToKeep) {
			this.removeBackup(list.get(0));
			list.remove(0);
		}
	}
	
	public List<String> getBackupList() {
		File folder = new File(Main.BACKUP_FOLDER);
		File[] backups = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return BackupUtils.this.checkBackupName(
						pathname.getName(),
						false);
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
