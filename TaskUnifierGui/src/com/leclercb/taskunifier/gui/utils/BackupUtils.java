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

import org.apache.commons.io.FileUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
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
		
		String folder = Main.getBackupFolder() + File.separator + backupName;
		
		File file = new File(folder);
		if (!file.exists()) {
			if (!createIfNotExists)
				return false;
			
			if (!file.mkdir()) {
				ErrorInfo info = new ErrorInfo(
						Translations.getString("general.error"),
						Translations.getString(
								"error.folder_not_a_folder",
								folder), null, null, null, null, null);
				
				JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
				
				return false;
			}
		} else if (!file.isDirectory()) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.folder_not_a_folder", folder),
					null,
					null,
					null,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
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
		
		String folder = Main.getBackupFolder() + File.separator + backupName;
		
		Main.copyAllData(folder);
		
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
		
		Synchronizing.setSynchronizing(true);
		
		try {
			String folder = Main.getBackupFolder()
					+ File.separator
					+ backupName;
			SynchronizerUtils.setTaskRepeatEnabled(false);
			Main.loadAllData(folder);
			SynchronizerUtils.setTaskRepeatEnabled(true);
		} finally {
			Synchronizing.setSynchronizing(false);
		}
		
		return true;
	}
	
	public void removeBackup(String backupName) {
		if (!this.checkBackupName(backupName, false))
			return;
		
		String folder = Main.getBackupFolder() + File.separator + backupName;
		
		try {
			FileUtils.deleteDirectory(new File(folder));
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					e.getMessage(),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
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
		File folder = new File(Main.getBackupFolder());
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
