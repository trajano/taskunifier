package com.leclercb.taskunifier.gui.swing;

import javax.swing.SwingWorker;

public abstract class TUStopableSwingWorker<T, V> extends SwingWorker<T, V> {
	
	private boolean stopped;
	
	public TUStopableSwingWorker() {
		this.stopped = false;
	}
	
	public synchronized boolean isStopped() {
		return this.stopped;
	}
	
	public synchronized void stop() {
		if (this.stopped)
			return;
		
		this.stopped = true;
		this.cancel(true);
	}
	
	protected void executeNonAtomicAction(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.start();
		
		while (!thread.isInterrupted() && !this.isStopped()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
}
