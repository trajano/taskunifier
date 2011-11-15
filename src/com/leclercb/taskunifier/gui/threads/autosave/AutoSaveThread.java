package com.leclercb.taskunifier.gui.threads.autosave;

import com.leclercb.taskunifier.gui.actions.ActionSave;

public class AutoSaveThread extends Thread {
	
	public AutoSaveThread() {
		
	}
	
	@Override
	public void run() {
		while (!this.isInterrupted()) {
			try {
				Thread.sleep(10 * 60 * 1000);
				
				ActionSave.save();
			} catch (InterruptedException e) {
				
			}
		}
	}
	
}
