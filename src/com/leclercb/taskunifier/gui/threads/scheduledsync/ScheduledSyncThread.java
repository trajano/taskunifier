/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.threads.scheduledsync;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.main.Main;

public class ScheduledSyncThread extends Thread implements PropertyChangeSupported {
	
	public static final String PROP_REMAINING_SLEEP_TIME = "remainingSleepTime";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private long sleepTime;
	private long remainingSleepTime;
	private boolean paused;
	
	public ScheduledSyncThread() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.sleepTime = Main.SETTINGS.getLongProperty("synchronizer.scheduler_sleep_time");
		this.remainingSleepTime = this.sleepTime;
		this.paused = !Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled");
		
		Main.SETTINGS.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						"synchronizer.scheduler_sleep_time")) {
					ScheduledSyncThread.this.sleepTime = Main.SETTINGS.getLongProperty("synchronizer.scheduler_sleep_time");
					ScheduledSyncThread.this.setRemainingSleepTime(ScheduledSyncThread.this.sleepTime);
				}
				
				if (evt.getPropertyName().equals(
						"synchronizer.scheduler_enabled")) {
					ScheduledSyncThread.this.paused = !Main.SETTINGS.getBooleanProperty("synchronizer.scheduler_enabled");
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
				
				ActionSynchronize.synchronize(true);
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
