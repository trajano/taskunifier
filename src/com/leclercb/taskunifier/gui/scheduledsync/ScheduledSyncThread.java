package com.leclercb.taskunifier.gui.scheduledsync;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.leclercb.taskunifier.api.event.ListenerList;
import com.leclercb.taskunifier.api.event.propertychange.PropertyChangeModel;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;

public class ScheduledSyncThread extends Thread implements PropertyChangeModel {
	
	public static final String PROP_REMAINING_SLEEP_TIME = "SCHEDULED_SYNC_THREAD_REMAINING_SLEEP_TIME";
	
	private ListenerList<PropertyChangeListener> propertyChangeListenerList;
	
	private ActionSynchronize synchronizeAction;
	private long sleepTime;
	private long remainingSleepTime;
	private boolean paused;
	
	public ScheduledSyncThread() {
		this.propertyChangeListenerList = new ListenerList<PropertyChangeListener>();
		
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
		this.firePropertyChange(
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
		this.propertyChangeListenerList.addListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeListenerList.removeListener(listener);
	}
	
	protected void firePropertyChange(PropertyChangeEvent event) {
		for (PropertyChangeListener listener : this.propertyChangeListenerList)
			listener.propertyChange(event);
	}
	
	protected void firePropertyChange(
			String property,
			Object oldValue,
			Object newValue) {
		this.firePropertyChange(new PropertyChangeEvent(
				this,
				property,
				oldValue,
				newValue));
	}
	
}
