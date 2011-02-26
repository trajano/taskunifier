package com.leclercb.taskunifier.gui.components.synchronize;

public class Synchronizing {
	
	private static boolean synchronizing = false;
	
	public synchronized static boolean isSynchronizing() {
		return synchronizing;
	}
	
	public synchronized static void setSynchronizing(boolean synchronizing) {
		Synchronizing.synchronizing = synchronizing;
	}
	
}
