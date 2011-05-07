package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerGuiPluginModel;
import com.leclercb.taskunifier.gui.commons.renderers.SynchronizerGuiPluginListCellRenderer;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ApiFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public ApiFieldType() {
		super(new SynchronizerGuiPluginModel(), Main.SETTINGS, "api.id");
		
		this.setRenderer(new SynchronizerGuiPluginListCellRenderer());
		
		this.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Main.SETTINGS.setStringProperty(
						"api.id",
						((SynchronizerGuiPlugin) ApiFieldType.this.getFieldValue()).getId());
			}
			
		});
	}
	
	@Override
	public Object getPropertyValue() {
		return SynchronizerUtils.getPlugin();
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty(
				"api.id",
				((SynchronizerGuiPlugin) this.getFieldValue()).getId());
	}
	
}
