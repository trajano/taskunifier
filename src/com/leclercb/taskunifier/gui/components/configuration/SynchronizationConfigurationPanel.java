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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.synchronization.ApiFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.synchronization.ChoiceFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.synchronization.KeepTasksForFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.synchronization.SchedulerSleepTimeFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.synchronization.SyncAtExitFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.synchronization.SyncAtStartFieldType;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class SynchronizationConfigurationPanel extends DefaultConfigurationPanel {
	
	private boolean welcome;
	
	public SynchronizationConfigurationPanel(boolean welcome) {
		super(Help.getHelpFile("configuration_synchronization.html"));
		
		this.welcome = welcome;
		
		this.initialize();
		this.pack();
		this.disableFields();
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"API",
				Translations.getString("configuration.synchronization.synchronize_with"),
				new ApiFieldType()));
		
		this.addField(new ConfigurationField(
				"MANAGE_PLUGINS",
				null,
				new ConfigurationFieldTypeExt.Button(new ActionManagePlugins(
						22,
						22) {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						ConfigurationDialog.getInstance().saveAndApplyConfig();
						
						super.actionPerformed(event);
					}
					
				})));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldTypeExt.Separator()));
		
		this.addField(new ConfigurationField(
				"CHOICE",
				Translations.getString("configuration.synchronization.choice"),
				new ChoiceFieldType()));
		
		this.addField(new ConfigurationField(
				"KEEP",
				Translations.getString("configuration.synchronization.keep_tasks_for"),
				new KeepTasksForFieldType()));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldTypeExt.Separator()));
		
		this.addField(new ConfigurationField(
				"SCHEDULER_SLEEP_TIME",
				Translations.getString("configuration.synchronization.scheduler_sleep_time"),
				new SchedulerSleepTimeFieldType()));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_3",
				null,
				new ConfigurationFieldTypeExt.Separator()));
		
		this.addField(new ConfigurationField(
				"SYNC_START",
				Translations.getString("configuration.synchronization.sync_start"),
				new SyncAtStartFieldType()));
		
		this.addField(new ConfigurationField(
				"SYNC_EXIT",
				Translations.getString("configuration.synchronization.sync_exit"),
				new SyncAtExitFieldType()));
		
		if (!this.welcome) {
			this.addField(new ConfigurationField(
					"SEPARATOR_4",
					null,
					new ConfigurationFieldTypeExt.Separator()));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL_LABEL",
					null,
					new ConfigurationFieldTypeExt.Label(
							Translations.getString(
									"configuration.synchronization.synchronize_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()))));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL",
					null,
					new ConfigurationFieldTypeExt.Button(new ActionSynchronize(
							false,
							22,
							22) {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							ConfigurationDialog.getInstance().saveAndApplyConfig();
							
							SynchronizerUtils.resetSynchronizer();
							
							super.actionPerformed(event);
						}
						
					})));
			
			this.addField(new ConfigurationField(
					"RESET_ALL_LABEL",
					null,
					new ConfigurationFieldTypeExt.Label(
							Translations.getString(
									"configuration.synchronization.reset_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()))));
			
			this.addField(new ConfigurationField(
					"RESET_ALL",
					null,
					new ConfigurationFieldTypeExt.Button(new ActionSynchronize(
							false,
							22,
							22) {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							try {
								ConfigurationDialog.getInstance().saveAndApplyConfig();
								
								SynchronizerUtils.resetSynchronizerAndDeleteModels();
								
								super.actionPerformed(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					})));
		}
		
		Main.SETTINGS.addPropertyChangeListener(
				"api.id",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						SynchronizationConfigurationPanel.this.disableFields();
						
						if (!SynchronizationConfigurationPanel.this.welcome) {
							JLabel synchronizeAllLabel = (JLabel) SynchronizationConfigurationPanel.this.getField(
									"SYNCHRONIZE_ALL_LABEL").getType().getFieldComponent();
							JLabel resetAllLabel = (JLabel) SynchronizationConfigurationPanel.this.getField(
									"RESET_ALL_LABEL").getType().getFieldComponent();
							
							synchronizeAllLabel.setText(Translations.getString(
									"configuration.synchronization.synchronize_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()));
							
							resetAllLabel.setText(Translations.getString(
									"configuration.synchronization.reset_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()));
						}
					}
					
				});
	}
	
	private void disableFields() {
		// Disable fields for DUMMY service
		boolean enabled = !SynchronizerUtils.getPlugin().getId().equals(
				DummyGuiPlugin.getInstance().getId());
		
		if (this.containsId("CHOICE"))
			this.setEnabled("CHOICE", enabled);
		
		if (this.containsId("KEEP"))
			this.setEnabled("KEEP", enabled);
		
		if (this.containsId("SCHEDULER_SLEEP_TIME"))
			this.setEnabled("SCHEDULER_SLEEP_TIME", enabled);
		
		if (this.containsId("SYNC_START"))
			this.setEnabled("SYNC_START", enabled);
		
		if (this.containsId("SYNC_EXIT"))
			this.setEnabled("SYNC_EXIT", enabled);
		
		if (this.containsId("SYNCHRONIZE_ALL"))
			this.setEnabled("SYNCHRONIZE_ALL", enabled);
		
		if (this.containsId("RESET_ALL"))
			this.setEnabled("RESET_ALL", enabled);
	}
	
}
