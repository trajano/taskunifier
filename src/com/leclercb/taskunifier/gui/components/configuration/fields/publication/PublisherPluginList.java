package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.models.CheckboxSynchronizerGuiPluginModel;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUCheckBoxList;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PublisherPluginList extends JPanel implements ConfigurationFieldType<JPanel, String> {
	
	private boolean first;
	private TUCheckBoxList list;
	
	public PublisherPluginList() {
		this.first = true;
		
		this.list = new TUCheckBoxList();
		this.list.setModel(new CheckboxSynchronizerGuiPluginModel(true, false));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.setLayout(new BorderLayout());
		this.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
	}
	
	public SynchronizerGuiPlugin getSelectedPlugin() {
		CheckBox cb = (CheckBox) this.list.getSelectedValue();
		
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
	public JPanel getFieldComponent() {
		return this;
	}
	
	@Override
	public String getFieldValue() {
		return null;
	}
	
	public void setFieldValue() {
		SynchronizerGuiPlugin[] plugins = SynchronizerUtils.getPublisherPlugins();
		CheckboxSynchronizerGuiPluginModel model = (CheckboxSynchronizerGuiPluginModel) this.list.getModel();
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
		CheckboxSynchronizerGuiPluginModel model = (CheckboxSynchronizerGuiPluginModel) this.list.getModel();
		for (int i = 0; i < model.getSize(); i++) {
			JCheckBox checkBox = (JCheckBox) model.getElementAt(i);
			
			if (checkBox.isSelected()) {
				plugins.add(SynchronizerUtils.getPlugin(checkBox.getActionCommand()));
			}
		}
		
		SynchronizerUtils.setPublisherPlugins(plugins.toArray(new SynchronizerGuiPlugin[0]));
	}
	
}
