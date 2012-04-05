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
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.taskunifier.gui.actions.ActionCreateNewBackup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.backup.AutoBackupEveryFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.backup.BackupListFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.backup.KeepBackupsFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class BackupConfigurationPanel extends DefaultConfigurationPanel {
	
	public BackupConfigurationPanel(ConfigurationGroup configuration) {
		super(configuration, "configuration_backup");
		this.initialize();
		this.pack();
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"CREATE_NEW_BACKUP",
				null,
				new ConfigurationFieldType.Button(new ActionCreateNewBackup(
						22,
						22))));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"BACKUP_BEFORE_SYNC",
				null,
				new ConfigurationFieldType.CheckBox(
						Main.getSettings(),
						"backup.backup_before_sync",
						Translations.getString("configuration.backup.backup_before_sync"),
						false)));
		
		this.addField(new ConfigurationField(
				"AUTO_BACKUP",
				Translations.getString("configuration.backup.auto_backup_every"),
				new AutoBackupEveryFieldType()));
		
		this.addField(new ConfigurationField(
				"KEEP_BACKUPS",
				Translations.getString("configuration.backup.keep_backups"),
				new KeepBackupsFieldType()));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"BACKUP_LIST",
				Translations.getString("configuration.backup.backup_list"),
				new BackupListFieldType()));
		
		final JComboBox backupList = (JComboBox) this.getField("BACKUP_LIST").getType().getFieldComponent();
		
		this.addField(new ConfigurationField(
				"RESTORE_BACKUP",
				null,
				new ConfigurationFieldType.Button(
						Translations.getString("configuration.backup.restore_backup"),
						new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								if (backupList.getSelectedItem() == null) {
									ErrorInfo info = new ErrorInfo(
											Translations.getString("general.error"),
											Translations.getString("error.no_backup_selected"),
											null,
											null,
											null,
											null,
											null);
									
									JXErrorPane.showDialog(
											FrameUtils.getCurrentFrameView().getFrame(),
											info);
									
									return;
								}
								
								BackupUtils.getInstance().createNewBackup();
								BackupUtils.getInstance().restoreBackup(
										(String) backupList.getSelectedItem());
							}
							
						})));
	}
	
}
