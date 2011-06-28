package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerGuiPluginModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueSynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ApiFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public ApiFieldType() {
		super(new SynchronizerGuiPluginModel(), Main.SETTINGS, "api.id");
		
		this.setRenderer(new DefaultListRenderer(
				StringValueSynchronizerGuiPlugin.INSTANCE));
		
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
