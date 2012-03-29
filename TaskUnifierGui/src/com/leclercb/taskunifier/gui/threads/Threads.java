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
		started = false;
		
		autoBackupThread = new AutoBackupThread();
		autoSaveThread = new AutoSaveThread();
		communicatorThread = new CommunicatorThread();
		reminderThread = new ReminderThread();
		scheduledSyncThread = new ScheduledSyncThread();
	}
	
	private static boolean started;
	
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
		if (started)
			throw new RuntimeException("Threads are already started");
		
		started = true;
		
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
		
		started = false;
	}
	
}
