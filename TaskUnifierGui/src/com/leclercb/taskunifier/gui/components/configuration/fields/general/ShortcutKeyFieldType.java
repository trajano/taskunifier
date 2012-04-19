package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.ShortcutKey;
import com.leclercb.taskunifier.gui.swing.TUShortcutField;

public class ShortcutKeyFieldType extends ConfigurationFieldType.Panel {
	
	private boolean first;
	private PropertyMap settings;
	private String propertyName;
	
	private TUShortcutField shortcutField;
	
	public ShortcutKeyFieldType(PropertyMap settings, String propertyName) {
		this.first = true;
		this.settings = settings;
		this.propertyName = propertyName;
		
		this.initialize();
	}
	
	private void initialize() {
		this.shortcutField = new TUShortcutField();
		
		JPanel panel = new JPanel();
		this.setPanel(panel);
		
		panel.setLayout(new BorderLayout());
		panel.add(this.shortcutField, BorderLayout.CENTER);
	}
	
	@Override
	public void initializeFieldComponent() {
		this.shortcutField.setShortcutKey(Main.getSettings().getObjectProperty(
				this.propertyName,
				ShortcutKey.class));
		
		if (this.first) {
			this.first = false;
			
			this.settings.addPropertyChangeListener(
					this.propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							ShortcutKeyFieldType.this.shortcutField.setShortcutKey(Main.getSettings().getObjectProperty(
									ShortcutKeyFieldType.this.propertyName,
									ShortcutKey.class));
						}
						
					});
		}
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.getSettings().setObjectProperty(
				this.propertyName,
				ShortcutKey.class,
				this.shortcutField.getShortcutKey());
	}
	
}
