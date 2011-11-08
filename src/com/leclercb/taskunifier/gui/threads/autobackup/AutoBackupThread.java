package com.leclercb.taskunifier.gui.threads.autobackup;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class AutoBackupThread extends Thread {
	
	public AutoBackupThread() {
		
	}
	
	@Override
	public void run() {
		while (!this.isInterrupted()) {
			try {
				Thread.sleep(60000);
				
				int nbHours = Main.SETTINGS.getIntegerProperty("general.backup.auto_backup_every");
				BackupUtils.getInstance().autoBackup(nbHours);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
}
