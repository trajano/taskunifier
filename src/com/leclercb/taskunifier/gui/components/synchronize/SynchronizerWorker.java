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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.util.Calendar;
import java.util.logging.Level;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerSettingsException;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerDefaultProgressMessage;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionSave;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.ConfigurationDialog.ConfigurationTab;
import com.leclercb.taskunifier.gui.components.synchronize.progress.SynchronizerProgressMessageListener;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.plugins.PluginLogger;
import com.leclercb.taskunifier.gui.swing.TUStopableSwingWorker;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.BackupUtils;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizerWorker extends TUStopableSwingWorker<Void, Void> {
	
	private static int SYNCHRONIZE_COUNT = 0;
	
	private boolean silent;
	private SynchronizerProgressMessageListener handler;
	
	public SynchronizerWorker(boolean silent) {
		this(silent, null);
	}
	
	public SynchronizerWorker(
			boolean silent,
			SynchronizerProgressMessageListener handler) {
		this.silent = silent;
		this.handler = handler;
	}
	
	@Override
	public synchronized void stop() {
		if (!this.isStopped()) {
			Constants.PROGRESS_MONITOR.addMessage(new SynchronizerDefaultProgressMessage(
					Translations.getString("synchronizer.cancelled_by_user")));
		}
		
		super.stop();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		ProgressMonitor monitor = Constants.PROGRESS_MONITOR;
		Connection connection = null;
		Synchronizer synchronizer = null;
		
		if (this.handler != null)
			monitor.addListChangeListener(this.handler);
		
		boolean set = false;
		
		try {
			try {
				set = Synchronizing.setSynchronizing(true);
			} catch (SynchronizingException e) {
				
			}
			
			if (!set) {
				return null;
			}
			
			if (Main.getSettings().getBooleanProperty(
					"general.backup.backup_before_sync"))
				BackupUtils.getInstance().createNewBackup();
			
			ActionSave.save();
			
			SynchronizerUtils.setTaskRepeatEnabled(false);
			
			monitor.addMessage(new SynchronizerDefaultProgressMessage(
					Translations.getString("synchronizer.set_proxy")));
			
			SynchronizerUtils.initializeProxy();
			
			SynchronizerGuiPlugin plugin = SynchronizerUtils.getSynchronizerPlugin();
			
			if (plugin.needsLicense()) {
				monitor.addMessage(new SynchronizerDefaultProgressMessage(
						Translations.getString("synchronizer.checking_license")));
				
				if (!plugin.checkLicense()) {
					int waitTime = Constants.WAIT_NO_LICENSE_TIME;
					waitTime += SYNCHRONIZE_COUNT
							* Constants.WAIT_NO_LICENSE_ADDED_TIME;
					
					monitor.addMessage(new SynchronizerDefaultProgressMessage(
							Translations.getString(
									"synchronizer.wait_no_license",
									waitTime)));
					
					Thread.sleep(waitTime * 1000);
					
					if (this.isStopped())
						return null;
				}
			}
			
			monitor.addMessage(new SynchronizerDefaultProgressMessage(
					Translations.getString(
							"synchronizer.connecting",
							plugin.getSynchronizerApi().getApiName())));
			
			connection = plugin.getSynchronizerApi().getConnection(
					Main.getUserSettings());
			
			connection.loadParameters(Main.getUserSettings());
			
			final Connection finalConnection = connection;
			this.executeNonAtomicAction(new Runnable() {
				
				@Override
				public void run() {
					try {
						finalConnection.connect();
					} catch (final SynchronizerException e) {
						if (SynchronizerWorker.this.isStopped())
							return;
						
						SynchronizerWorker.this.handleSynchronizerException(e);
						SynchronizerWorker.this.stop();
					} catch (final Throwable t) {
						if (SynchronizerWorker.this.isStopped())
							return;
						
						SynchronizerWorker.this.handleThrowable(t);
						SynchronizerWorker.this.stop();
					}
				};
				
			});
			
			if (this.isStopped())
				return null;
			
			connection.saveParameters(Main.getUserSettings());
			
			synchronizer = plugin.getSynchronizerApi().getSynchronizer(
					Main.getUserSettings(),
					connection);
			
			SynchronizerChoice choice = Main.getUserSettings().getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
			
			synchronizer.loadParameters(Main.getUserSettings());
			synchronizer.synchronize(choice, monitor);
			synchronizer.saveParameters(Main.getUserSettings());
			
			connection.disconnect();
			
			Main.getUserSettings().setCalendarProperty(
					"synchronizer.last_synchronization_date",
					Calendar.getInstance());
			
			SYNCHRONIZE_COUNT++;
		} catch (InterruptedException e) {
			return null;
		} catch (SynchronizerException e) {
			this.handleSynchronizerException(e);
			return null;
		} catch (Throwable t) {
			this.handleThrowable(t);
			return null;
		} finally {
			SynchronizerUtils.removeProxy();
			
			if (this.handler != null)
				monitor.removeListChangeListener(this.handler);
			
			Constants.PROGRESS_MONITOR.clear();
			SynchronizerUtils.removeOldCompletedTasks();
			SynchronizerUtils.setTaskRepeatEnabled(true);
			
			if (set) {
				try {
					Synchronizing.setSynchronizing(false);
				} catch (SynchronizingException e) {
					
				}
			}
		}
		
		Thread.sleep(1000);
		
		Main.getUserSettings().setStringProperty(
				"synchronizer.scheduler_sleep_time",
				Main.getUserSettings().getStringProperty(
						"synchronizer.scheduler_sleep_time"),
				true);
		
		return null;
	}
	
	private void handleSynchronizerException(final SynchronizerException e) {
		if (this.isStopped())
			return;
		
		Constants.PROGRESS_MONITOR.addMessage(new SynchronizerDefaultProgressMessage(
				e.getMessage()));
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				PluginLogger.getLogger().log(
						(e.isExpected() ? Level.INFO : Level.WARNING),
						e.getMessage(),
						e);
				
				ErrorInfo info = new ErrorInfo(
						Translations.getString("general.error"),
						e.getMessage(),
						null,
						null,
						(e.isExpected() ? e : null),
						null,
						null);
				
				JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
				
				if (e instanceof SynchronizerSettingsException)
					ActionConfiguration.configuration(ConfigurationTab.PLUGIN);
			}
			
		});
	}
	
	private void handleThrowable(final Throwable t) {
		if (this.isStopped())
			return;
		
		Constants.PROGRESS_MONITOR.addMessage(new SynchronizerDefaultProgressMessage(
				t.getMessage()));
		
		if (!this.silent) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					PluginLogger.getLogger().log(
							Level.WARNING,
							t.getMessage(),
							t);
					
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							t.getMessage(),
							null,
							null,
							t,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
				
			});
		}
	}
	
}
