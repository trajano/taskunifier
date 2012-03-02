package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;

public class PublisherPluginFieldType extends ConfigurationFieldType.Panel {
	
	private boolean first;
	
	private PublisherPluginTable table;
	
	public PublisherPluginFieldType() {
		this.first = true;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.table = new PublisherPluginTable();
		
		panel.add(this.table.getTableHeader(), BorderLayout.NORTH);
		panel.add(this.table, BorderLayout.CENTER);
		
		this.setPanel(panel);
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
	
}
