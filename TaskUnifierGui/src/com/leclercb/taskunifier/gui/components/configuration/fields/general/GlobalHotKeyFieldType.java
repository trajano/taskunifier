package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.GlobalHotKey;

public class GlobalHotKeyFieldType extends ConfigurationFieldType.Panel {
	
	private boolean first;
	private PropertyMap settings;
	private String propertyName;
	
	private GlobalHotKey globalHotKey;
	private JTextField globalHotKeyField;
	
	public GlobalHotKeyFieldType(PropertyMap settings, String propertyName) {
		this.first = true;
		this.settings = settings;
		this.propertyName = propertyName;
		
		this.globalHotKey = null;
		
		this.initialize();
	}
	
	private void initialize() {
		this.globalHotKeyField = new JTextField();
		this.globalHotKeyField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent event) {}
			
			@Override
			public void keyReleased(KeyEvent event) {}
			
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getModifiers() == 0)
					return;
				
				GlobalHotKey globalHotKey = new GlobalHotKey(
						event.getKeyCode(),
						event.getModifiers());
				
				GlobalHotKeyFieldType.this.setGlobalHotKey(globalHotKey);
			}
			
		});
		
		JPanel panel = new JPanel();
		this.setPanel(panel);
		
		panel.setLayout(new BorderLayout());
		panel.add(this.globalHotKeyField, BorderLayout.CENTER);
	}
	
	public GlobalHotKey getGlobalHotKey() {
		return this.globalHotKey;
	}
	
	public void setGlobalHotKey(GlobalHotKey globalHotKey) {
		this.globalHotKey = globalHotKey;
		
		if (globalHotKey == null)
			this.globalHotKeyField.setText(null);
		else
			this.globalHotKeyField.setText(KeyEvent.getKeyModifiersText(globalHotKey.getModifiers())
					+ " + "
					+ KeyEvent.getKeyText(globalHotKey.getKeyCode()));
	}
	
	@Override
	public void initializeFieldComponent() {
		this.setGlobalHotKey(Main.getSettings().getObjectProperty(
				this.propertyName,
				GlobalHotKey.class));
		
		if (this.first) {
			this.first = false;
			
			this.settings.addPropertyChangeListener(
					this.propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							GlobalHotKeyFieldType.this.setGlobalHotKey(Main.getSettings().getObjectProperty(
									GlobalHotKeyFieldType.this.propertyName,
									GlobalHotKey.class));
						}
						
					});
		}
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.getSettings().setObjectProperty(
				this.propertyName,
				GlobalHotKey.class,
				this.getGlobalHotKey());
	}
	
}
