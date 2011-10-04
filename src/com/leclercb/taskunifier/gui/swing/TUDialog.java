package com.leclercb.taskunifier.gui.swing;

import java.awt.Frame;

import javax.swing.JDialog;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.taskunifier.gui.main.Main;

public class TUDialog extends JDialog {
	
	public TUDialog() {
		super();
	}
	
	public TUDialog(Frame owner) {
		super(owner);
	}
	
	public void loadWindowSettings(final String windowProperty) {
		if (windowProperty == null)
			return;
		
		int width = Main.SETTINGS.getIntegerProperty(windowProperty + ".width");
		int height = Main.SETTINGS.getIntegerProperty(windowProperty
				+ ".height");
		int locationX = Main.SETTINGS.getIntegerProperty(windowProperty
				+ ".location_x");
		int locationY = Main.SETTINGS.getIntegerProperty(windowProperty
				+ ".location_y");
		
		this.setSize(width, height);
		this.setLocation(locationX, locationY);
		
		Main.SETTINGS.addSavePropertiesListener(new SavePropertiesListener() {
			
			@Override
			public void saveProperties() {
				Main.SETTINGS.setIntegerProperty(
						windowProperty + ".width",
						TUDialog.this.getWidth());
				Main.SETTINGS.setIntegerProperty(
						windowProperty + ".height",
						TUDialog.this.getHeight());
				Main.SETTINGS.setIntegerProperty(
						windowProperty + ".location_x",
						TUDialog.this.getX());
				Main.SETTINGS.setIntegerProperty(
						windowProperty + ".location_y",
						TUDialog.this.getY());
			}
			
		});
	}
	
}
