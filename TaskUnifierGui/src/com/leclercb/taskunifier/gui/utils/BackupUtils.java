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

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;

public final class BackupUtils implements ListChangeSupported {
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyyMMdd_HHmmss");
	
	private static BackupUtils INSTANCE;
	
	public static BackupUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BackupUtils();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private List<String> backups;
	
	private BackupUtils() {
		this.listChangeSupport = new ListChangeSupport(BackupUtils.class);
		
		this.backups = new ArrayList<String>();
		
		this.initialize();
	}
	
	public int getIndexOf(String backupName) {
		return this.backups.indexOf(backupName);
	}
	
	public int getBackupCount() {
		return this.backups.size();
	}
	
	public String getBackup(int index) {
		return this.backups.get(index);
	}
	
	public String[] getBackups() {
		return this.backups.toArray(new String[0]);
	}
	
	private void initialize() {
		File folder = new File(Main.getBackupFolder());
		File[] backups = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return BackupUtils.this.checkBackupName(
						pathname.getName(),
						false);
			}
		});
		
		for (File file : backups) {
			this.backups.add(file.getName());
		}
		
		Collections.sort(this.backups);
	}
	
	private Calendar backupNameToDate(String backupName) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(FORMAT.parse(backupName));
			return calendar;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean checkBackupName(String backupName, boolean createIfNotExists) {
		if (this.backupNameToDate(backupName) == null)
			return false;
		
		String folder = Main.getBackupFolder() + File.separator + backupName;
		
		File file = new File(folder);
		if (!file.exists()) {
			if (!createIfNotExists)
				return false;
			
			if (!file.mkdir()) {
				GuiLogger.getLogger().log(
						Level.WARNING,
						"Cannot create backup folder: " + backupName);
				
				return false;
			}
		} else if (!file.isDirectory()) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Backup folder is not a directory: " + backupName);
			
			return false;
		}
		
		return true;
	}
	
	public void autoBackup(int nbHours) {
		if (this.backups.size() == 0) {
			this.createNewBackup();
			return;
		}
		
		try {
			Calendar calendar = this.backupNameToDate(this.backups.get(this.backups.size() - 1));
			
			Calendar past = Calendar.getInstance();
			past.add(Calendar.HOUR_OF_DAY, -nbHours);
			
			if (calendar.compareTo(past) <= 0)
				this.createNewBackup();
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Cannot create auto backup",
					e);
		}
	}
	
	public boolean createNewBackup() {
		String backupName = FORMAT.format(Calendar.getInstance().getTime());
		
		if (!this.checkBackupName(backupName, true))
			return false;
		
		String folder = Main.getBackupFolder() + File.separator + backupName;
		
		Main.copyAllData(folder, null);
		
		this.backups.add(backupName);
		
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
		
		SynchronizerUtils.resetAllSynchronizersAndDeleteModels();
		
		Synchronizing.getInstance().setSynchronizing(true);
		
		try {
			String folder = Main.getBackupFolder()
					+ File.separator
					+ backupName;
			
			SynchronizerUtils.setTaskRepeatEnabled(false);
			
			Main.loadAllData(folder, null);
			
			SynchronizerUtils.setTaskRepeatEnabled(true);
		} finally {
			Synchronizing.getInstance().setSynchronizing(false);
		}
		
		return true;
	}
	
	public void removeBackup(String backupName) {
		if (!this.checkBackupName(backupName, false))
			return;
		
		String folder = Main.getBackupFolder() + File.separator + backupName;
		
		try {
			FileUtils.deleteDirectory(new File(folder));
			
			this.backups.remove(backupName);
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Cannot remove backup folder: " + backupName,
					e);
			
			return;
		}
		
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_REMOVED,
				0,
				backupName);
		
		GuiLogger.getLogger().info("Backup removed: " + backupName);
	}
	
	public void cleanBackups(int nbToKeep) {
		if (nbToKeep < 1)
			throw new IllegalArgumentException();
		
		while (this.backups.size() > nbToKeep) {
			this.removeBackup(this.backups.get(0));
			this.backups.remove(0);
		}
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
}
