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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionCreateNewBackup extends AbstractAction {
	
	public ActionCreateNewBackup() {
		this(32, 32);
	}
	
	public ActionCreateNewBackup(int width, int height) {
		super(
				Translations.getString("action.create_new_backup"),
				Images.getResourceImage("save.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.create_new_backup"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionCreateNewBackup.createNewBackup();
	}
	
	public static void createNewBackup() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String folder = format.format(Calendar.getInstance().getTime());
		folder = Main.BACKUP_FOLDER + File.separator + folder;
		
		File file = new File(folder);
		if (!file.exists()) {
			if (!file.mkdir()) {
				JOptionPane.showMessageDialog(
						null,
						Translations.getString(
								"error.folder_not_a_folder",
								folder),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (!file.isDirectory()) {
			JOptionPane.showMessageDialog(
					null,
					Translations.getString("error.folder_not_a_folder", folder),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Main.saveAll(folder);
	}
	
	public static void restoreBackup(String folder) {
		if (!Synchronizing.setSynchronizing(true))
			return;
		
		folder = Main.BACKUP_FOLDER + File.separator + folder;
		Main.loadAll(folder);
		
		Synchronizing.setSynchronizing(false);
	}
	
	public static List<String> getBackupList() {
		File folder = new File(Main.BACKUP_FOLDER);
		File[] backups = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (!pathname.isDirectory())
					return false;
				
				if (!pathname.getName().matches("[0-9]{8}_[0-9]{6}"))
					return false;
				
				return true;
			}
		});
		
		List<String> list = new ArrayList<String>();
		for (File file : backups) {
			list.add(file.getName());
		}
		
		Collections.sort(list);
		return list;
	}
	
	public static void removeBackup(String folder) {
		String path = Main.BACKUP_FOLDER + File.separator + folder;
		
		try {
			FileUtils.deleteDirectory(new File(path));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		GuiLogger.getLogger().info("Backup removed: " + folder);
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
	
}
