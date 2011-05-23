package com.leclercb.taskunifier.gui.components.configuration.fields.synchronization;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.models.SynchronizerGuiPluginModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueSynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ApiFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public ApiFieldType() {
		super(new SynchronizerGuiPluginModel(), Main.SETTINGS, "api.id");
		
		this.setRenderer(new DefaultListRenderer(
				new StringValueSynchronizerGuiPlugin()));
		
		this.getModel().addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {

			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
				ApiFieldType.this.setSelectedItem(ApiFieldType.this.getModel().getElementAt(
						e.getIndex0()));
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

			}
			
		});
		
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
