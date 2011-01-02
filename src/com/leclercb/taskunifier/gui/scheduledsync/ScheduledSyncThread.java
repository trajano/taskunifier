package com.leclercb.taskunifier.gui.scheduledsync;

import com.leclercb.taskunifier.gui.actions.ActionSynchronize;

public class ScheduledSyncThread extends Thread {
	
	private ActionSynchronize synchronizeAction;
	private long sleepTime;
	private long remainingSleepTime;
	private boolean pause;
	
	public ScheduledSyncThread(long sleepTime) {
		this.synchronizeAction = new ActionSynchronize();
		this.sleepTime = sleepTime;
		this.remainingSleepTime = sleepTime;
		this.pause = false;
	}
	
	public long getRemainingSleepTime() {
		return this.remainingSleepTime;
	}
	
	public long getSleepTime() {
		return this.sleepTime;
	}
	
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public boolean isPause() {
		return this.pause;
	}
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				for (this.remainingSleepTime = this.sleepTime; this.remainingSleepTime > 0; this.remainingSleepTime = (this.pause ? this.remainingSleepTime : this.remainingSleepTime - 1))
					Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
			this.synchronizeAction.synchronize();
		}
	}
	
}
