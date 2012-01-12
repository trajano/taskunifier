package com.leclercb.taskunifier.gui.swing;

import java.awt.Frame;
import java.awt.Point;

import javax.swing.JDialog;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.utils.ScreenUtils;
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
		
		int width = Main.getSettings().getIntegerProperty(
				windowProperty + ".width");
		int height = Main.getSettings().getIntegerProperty(
				windowProperty + ".height");
		int locationX = Main.getSettings().getIntegerProperty(
				windowProperty + ".location_x");
		int locationY = Main.getSettings().getIntegerProperty(
				windowProperty + ".location_y");
		
		this.setSize(width, height);
		
		if (ScreenUtils.isLocationInScreen(new Point(locationX, locationY)))
			this.setLocation(locationX, locationY);
		else if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		else
			this.setLocation(0, 0);
		
		Main.getSettings().addSavePropertiesListener(
				new SavePropertiesListener() {
					
					@Override
					public void saveProperties() {
						Main.getSettings().setIntegerProperty(
								windowProperty + ".width",
								TUDialog.this.getWidth());
						Main.getSettings().setIntegerProperty(
								windowProperty + ".height",
								TUDialog.this.getHeight());
						Main.getSettings().setIntegerProperty(
								windowProperty + ".location_x",
								TUDialog.this.getX());
						Main.getSettings().setIntegerProperty(
								windowProperty + ".location_y",
								TUDialog.this.getY());
					}
					
				});
	}
	
}
