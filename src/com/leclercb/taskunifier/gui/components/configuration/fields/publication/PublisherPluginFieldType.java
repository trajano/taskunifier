package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class PublisherPluginFieldType extends JPanel implements ConfigurationFieldType<JPanel, String> {
	
	private boolean first;
	private PublisherPluginTable table;
	
	public PublisherPluginFieldType() {
		this.first = true;
		
		this.table = new PublisherPluginTable();
		
		this.setLayout(new BorderLayout());
		this.add(
				ComponentFactory.createJScrollPane(this.table, true),
				BorderLayout.CENTER);
	}
	
	@Override
	public void initializeFieldComponent() {
		this.table.refresh();
		
		if (this.first) {
			this.first = false;
			
			Main.getUserSettings().addPropertyChangeListener(
					"plugin.publisher.ids",
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							PublisherPluginFieldType.this.table.refresh();
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
	
	@Override
	public String getPropertyValue() {
		return Main.getUserSettings().getStringProperty("plugin.publisher.ids");
	}
	
	@Override
	public void saveAndApplyConfig() {
		
	}
	
}
