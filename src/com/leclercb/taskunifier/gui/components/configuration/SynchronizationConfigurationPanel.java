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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.swing.formatters.RegexFormatter;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerChoiceModel;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerGuiPluginModel;
import com.leclercb.taskunifier.gui.commons.renderers.SynchronizerChoiceListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.SynchronizerGuiPluginListCellRenderer;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizationConfigurationPanel extends DefaultConfigurationPanel {
	
	public SynchronizationConfigurationPanel(boolean welcome) {
		super(Help.getHelpFile("configuration_synchronization.html"));
		this.initialize(welcome);
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty(
				"api.id",
				((SynchronizerGuiPlugin) this.getValue("API")).getId());
		
		Main.SETTINGS.setEnumProperty(
				"synchronizer.choice",
				SynchronizerChoice.class,
				(SynchronizerChoice) this.getValue("CHOICE"));
		
		if (!EqualsUtils.equals(
				Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days"),
				this.getValue("KEEP")))
			SynchronizerUtils.resetSynchronizer();
		
		Main.SETTINGS.setStringProperty(
				"synchronizer.keep_tasks_completed_for_x_days",
				(String) this.getValue("KEEP"));
		
		Main.SETTINGS.setLongProperty(
				"synchronizer.scheduler_sleep_time",
				((Integer) this.getValue("SCHEDULER_SLEEP_TIME")) * 1000l);
		
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_start",
				(Boolean) this.getValue("SYNC_START"));
		
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.sync_exit",
				(Boolean) this.getValue("SYNC_EXIT"));
	}
	
	private void initialize(boolean welcome) {
		/*
		 * this.addField(new ConfigurationField(
		 * "API_RESET_ALL",
		 * null,
		 * new ConfigurationFieldType.Label(
		 * Translations.getString("configuration.synchronization.api_reset_all"))
		 * ));
		 */

		ConfigurationFieldType.ComboBox comboBox = null;
		
		comboBox = new ConfigurationFieldType.ComboBox(
				new SynchronizerGuiPluginModel(),
				Main.SETTINGS,
				"api.id") {
			
			@Override
			public Object getPropertyValue() {
				return SynchronizerUtils.getPlugin();
			}
			
		};
		
		comboBox.setRenderer(new SynchronizerGuiPluginListCellRenderer());
		
		this.addField(new ConfigurationField(
				"API",
				Translations.getString("general.api"),
				comboBox));
		
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Main.SETTINGS.setStringProperty(
						"api.id",
						((SynchronizerGuiPlugin) SynchronizationConfigurationPanel.this.getValue("API")).getId());
			}
			
		});
		
		this.addField(new ConfigurationField(
				"MANAGE_PLUGINS",
				null,
				new ConfigurationFieldType.Button(new ActionManagePlugins(
						22,
						22))));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_1",
				null,
				new ConfigurationFieldType.Separator()));
		
		comboBox = new ConfigurationFieldType.ComboBox(
				new SynchronizerChoiceModel(),
				Main.SETTINGS,
				"synchronizer.choice") {
			
			@Override
			public Object getPropertyValue() {
				if (Main.SETTINGS.getEnumProperty(
						"synchronizer.choice",
						SynchronizerChoice.class) != null)
					return Main.SETTINGS.getEnumProperty(
							"synchronizer.choice",
							SynchronizerChoice.class);
				else
					return SynchronizerChoice.KEEP_LAST_UPDATED;
			}
			
		};
		
		comboBox.setRenderer(new SynchronizerChoiceListCellRenderer());
		
		this.addField(new ConfigurationField(
				"CHOICE",
				Translations.getString("configuration.synchronization.choice"),
				comboBox));
		
		this.addField(new ConfigurationField(
				"KEEP",
				Translations.getString("configuration.synchronization.keep_tasks_for"),
				new ConfigurationFieldType.FormattedTextField(
						new RegexFormatter("^[0-9]{1,4}$"),
						Main.SETTINGS,
						"synchronizer.keep_tasks_completed_for_x_days") {
					
					@Override
					public String getPropertyValue() {
						if (Main.SETTINGS.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days") != null)
							return Main.SETTINGS.getStringProperty("synchronizer.keep_tasks_completed_for_x_days");
						else
							return "14";
					}
					
				}));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_2",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SCHEDULER_SLEEP_TIME",
				Translations.getString("configuration.synchronization.scheduler_sleep_time"),
				new ConfigurationFieldType.Spinner(
						Main.SETTINGS,
						"synchronizer.scheduler_sleep_time") {
					
					@Override
					public Object getPropertyValue() {
						if (Main.SETTINGS.getLongProperty("synchronizer.scheduler_sleep_time") != null)
							return (int) (Main.SETTINGS.getLongProperty("synchronizer.scheduler_sleep_time") / 1000);
						else
							return 600;
					}
					
				}));
		
		JSpinner spinner = (JSpinner) this.getField("SCHEDULER_SLEEP_TIME").getType().getFieldComponent();
		spinner.setModel(new SpinnerNumberModel(
				(Number) this.getField("SCHEDULER_SLEEP_TIME").getType().getPropertyValue(),
				10,
				5 * 3600,
				60));
		spinner.setEditor(new JSpinner.NumberEditor(spinner));
		
		this.addField(new ConfigurationField(
				"SEPARATOR_3",
				null,
				new ConfigurationFieldType.Separator()));
		
		this.addField(new ConfigurationField(
				"SYNC_START",
				Translations.getString("configuration.synchronization.sync_start"),
				new ConfigurationFieldType.CheckBox(
						Main.SETTINGS,
						"synchronizer.sync_start") {
					
					@Override
					public Boolean getPropertyValue() {
						if (Main.SETTINGS.getBooleanProperty("synchronizer.sync_start") != null)
							return Main.SETTINGS.getBooleanProperty("synchronizer.sync_start");
						else
							return false;
					}
					
				}));
		
		this.addField(new ConfigurationField(
				"SYNC_EXIT",
				Translations.getString("configuration.synchronization.sync_exit"),
				new ConfigurationFieldType.CheckBox(
						Main.SETTINGS,
						"synchronizer.sync_exit") {
					
					@Override
					public Boolean getPropertyValue() {
						if (Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit") != null)
							return Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit");
						else
							return false;
					}
					
				}));
		
		if (!welcome) {
			this.addField(new ConfigurationField(
					"SEPARATOR_4",
					null,
					new ConfigurationFieldType.Separator()));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString(
									"configuration.synchronization.synchronize_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()))));
			
			this.addField(new ConfigurationField(
					"SYNCHRONIZE_ALL",
					null,
					new ConfigurationFieldType.Button(new ActionSynchronize(
							false,
							22,
							22) {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							SynchronizationConfigurationPanel.this.saveAndApplyConfig();
							
							SynchronizerUtils.resetSynchronizer();
							
							super.actionPerformed(event);
						}
						
					})));
			
			this.addField(new ConfigurationField(
					"RESET_ALL_LABEL",
					null,
					new ConfigurationFieldType.Label(
							Translations.getString(
									"configuration.synchronization.reset_all",
									SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName()))));
			
			this.addField(new ConfigurationField(
					"RESET_ALL",
					null,
					new ConfigurationFieldType.Button(new ActionSynchronize(
							false,
							22,
							22) {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							try {
								SynchronizationConfigurationPanel.this.saveAndApplyConfig();
								
								SynchronizerUtils.resetSynchronizerAndDeleteModels();
								
								super.actionPerformed(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					})));
		}
		
		this.disableFields();
		
		Main.SETTINGS.addPropertyChangeListener(
				"api.id",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						SynchronizationConfigurationPanel.this.disableFields();
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
