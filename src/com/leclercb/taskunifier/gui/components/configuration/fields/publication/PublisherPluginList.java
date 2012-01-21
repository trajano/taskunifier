package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.models.CheckboxSynchronizerGuiPluginModel;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUCheckBoxList;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PublisherPluginList extends TUCheckBoxList implements ConfigurationFieldType<TUCheckBoxList, String> {
	
	private boolean first;
	
	public PublisherPluginList() {
		this.first = true;
		
		this.setModel(new CheckboxSynchronizerGuiPluginModel(true, false));
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public SynchronizerGuiPlugin getSelectedPlugin() {
		CheckBox cb = (CheckBox) this.getSelectedValue();
		
		if (cb == null)
			return null;
		
		return SynchronizerUtils.getPlugin(cb.getActionCommand());
	}
	
	@Override
	public void initializeFieldComponent() {
		this.setFieldValue();
		
		if (this.first) {
			this.first = false;
			
			Main.getUserSettings().addPropertyChangeListener(
					"plugin.publisher.ids",
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							PublisherPluginList.this.setFieldValue();
						}
						
					});
		}
	}
	
	@Override
	public TUCheckBoxList getFieldComponent() {
		return this;
	}
	
	@Override
	public String getFieldValue() {
		return null;
	}
	
	public void setFieldValue() {
		SynchronizerGuiPlugin[] plugins = SynchronizerUtils.getPublisherPlugins();
		CheckboxSynchronizerGuiPluginModel model = (CheckboxSynchronizerGuiPluginModel) this.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			JCheckBox checkBox = (JCheckBox) model.getElementAt(i);
			
			checkBox.setSelected(false);
			for (SynchronizerGuiPlugin plugin : plugins) {
				if (checkBox.getActionCommand().equals(plugin.getId())) {
					checkBox.setSelected(true);
					break;
				}
			}
		}
	}
	
	@Override
	public String getPropertyValue() {
		return Main.getUserSettings().getStringProperty("plugin.publisher.ids");
	}
	
	@Override
	public void saveAndApplyConfig() {
		List<SynchronizerGuiPlugin> plugins = new ArrayList<SynchronizerGuiPlugin>();
		CheckboxSynchronizerGuiPluginModel model = (CheckboxSynchronizerGuiPluginModel) this.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			JCheckBox checkBox = (JCheckBox) model.getElementAt(i);
			
			if (checkBox.isSelected()) {
				plugins.add(SynchronizerUtils.getPlugin(checkBox.getActionCommand()));
			}
		}
		
		SynchronizerUtils.setPublisherPlugins(plugins.toArray(new SynchronizerGuiPlugin[0]));
	}
	
}
