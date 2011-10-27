package com.leclercb.taskunifier.gui.threads.autobackup;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class AutoBackupThread extends Thread {
	
	public AutoBackupThread() {
		
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				
			}
			
			int nbHours = Main.SETTINGS.getIntegerProperty("general.backup.auto_backup_every");
			BackupUtils.getInstance().autoBackup(nbHours);
		}
	}
	
}
