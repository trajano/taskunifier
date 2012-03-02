package com.leclercb.taskunifier.gui.threads;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.threads.autobackup.AutoBackupThread;
import com.leclercb.taskunifier.gui.threads.autosave.AutoSaveThread;
import com.leclercb.taskunifier.gui.threads.communicator.CommunicatorThread;
import com.leclercb.taskunifier.gui.threads.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.threads.scheduledsync.ScheduledSyncThread;

public final class Threads {
	
	private Threads() {
		
	}
	
	static {
		autoBackupThread = new AutoBackupThread();
		autoSaveThread = new AutoSaveThread();
		communicatorThread = new CommunicatorThread();
		reminderThread = new ReminderThread();
		scheduledSyncThread = new ScheduledSyncThread();
	}
	
	private static AutoBackupThread autoBackupThread;
	private static AutoSaveThread autoSaveThread;
	private static CommunicatorThread communicatorThread;
	private static ReminderThread reminderThread;
	private static ScheduledSyncThread scheduledSyncThread;
	
	public static AutoBackupThread getAutoBackupThread() {
		return autoBackupThread;
	}
	
	public static AutoSaveThread getAutoSaveThread() {
		return autoSaveThread;
	}
	
	public static CommunicatorThread getCommunicatorThread() {
		return communicatorThread;
	}
	
	public static ReminderThread getReminderThread() {
		return reminderThread;
	}
	
	public static ScheduledSyncThread getScheduledSyncThread() {
		return scheduledSyncThread;
	}
	
	public static void startAll() {
		autoBackupThread.start();
		autoSaveThread.start();
		
		if (Main.getSettings().getBooleanProperty(
				"general.communicator.enabled"))
			communicatorThread.start();
		
		reminderThread.start();
		scheduledSyncThread.start();
	}
	
	public static void stopAll() {
		autoBackupThread.interrupt();
		autoSaveThread.interrupt();
		communicatorThread.interrupt();
		reminderThread.interrupt();
		scheduledSyncThread.interrupt();
	}
	
}
