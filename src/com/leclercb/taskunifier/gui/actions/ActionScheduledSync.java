package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionScheduledSync extends AbstractAction {
	
	private int width;
	private int height;
	
	public ActionScheduledSync() {
		this(32, 32);
	}
	
	public ActionScheduledSync(int width, int height) {
		super(Translations.getString("action.name.scheduled_sync"));
		
		this.width = width;
		this.height = height;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.scheduled_sync"));
		
		this.updateIcon();
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						"synchronizer.scheduler_enabled"))
					ActionScheduledSync.this.updateIcon();
			}
			
		});
	}
	
	private void updateIcon() {
		if (Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled"))
			this.putValue(
					SMALL_ICON,
					Images.getResourceImage("play.png", this.width, this.height));
		else
			this.putValue(SMALL_ICON, Images.getResourceImage(
					"pause.png",
					this.width,
					this.height));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Main.SETTINGS.setBooleanProperty(
				"synchronizer.scheduler_enabled",
				!Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled"));
	}
	
}
