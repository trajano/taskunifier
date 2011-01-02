package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionScheduledSync extends AbstractAction {
	
	private ScheduledSyncThread thread;
	
	public ActionScheduledSync(ScheduledSyncThread thread) {
		this(thread, 32, 32);
	}
	
	public ActionScheduledSync(ScheduledSyncThread thread, int width, int height) {
		super(
				Translations.getString("action.name.scheduledsync"),
				Images.getResourceImage("synchronize.png", width, height));
		
		this.thread = thread;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.scheduledsync"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.thread.setPause(!this.thread.isPause());
	}
	
}
