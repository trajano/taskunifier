package com.leclercb.taskunifier.gui.scheduledsync;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.settings.Settings;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;

public class ScheduledSyncThread extends Thread implements PropertyChangeSupported {
	
	public static final String PROP_REMAINING_SLEEP_TIME = "remainingSleepTime";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private ActionSynchronize synchronizeAction;
	private long sleepTime;
	private long remainingSleepTime;
	private boolean paused;
	
	public ScheduledSyncThread() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.synchronizeAction = new ActionSynchronize();
		this.sleepTime = Settings.getLongProperty("synchronizer.scheduler_sleep_time");
		this.remainingSleepTime = this.sleepTime;
		this.paused = !Settings.getBooleanProperty("synchronizer.scheduler_enabled");
		
		Settings.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						"synchronizer.scheduler_sleep_time")) {
					ScheduledSyncThread.this.sleepTime = Settings.getLongProperty("synchronizer.scheduler_sleep_time");
					ScheduledSyncThread.this.setRemainingSleepTime(ScheduledSyncThread.this.sleepTime);
				}
				
				if (evt.getPropertyName().equals(
						"synchronizer.scheduler_enabled")) {
					ScheduledSyncThread.this.paused = !Settings.getBooleanProperty("synchronizer.scheduler_enabled");
				}
			}
			
		});
	}
	
	public synchronized long getRemainingSleepTime() {
		return this.remainingSleepTime;
	}
	
	private synchronized void setRemainingSleepTime(long remainingSleepTime) {
		long oldRemainingSleepTime = this.remainingSleepTime;
		this.remainingSleepTime = remainingSleepTime;
		this.propertyChangeSupport.firePropertyChange(
				PROP_REMAINING_SLEEP_TIME,
				oldRemainingSleepTime,
				remainingSleepTime);
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				for (this.setRemainingSleepTime(this.sleepTime); this.getRemainingSleepTime() > 0; this.setRemainingSleepTime(this.isPaused() ? this.getRemainingSleepTime() : this.getRemainingSleepTime() - 1000))
					Thread.sleep(1000);
				
				this.synchronizeAction.synchronize();
			}
		} catch (InterruptedException e) {}
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
