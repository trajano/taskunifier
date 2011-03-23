/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.synchronize;

import java.awt.Cursor;
import java.awt.Frame;
import java.util.Calendar;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.ProgressMessageType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.RetrieveModelsProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizationProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizeModelsProgressMessage;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.WaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizerDialog extends WaitDialog {
	
	public SynchronizerDialog(Frame frame) {
		super(frame, Translations.getString("general.synchronization"));
		this.setRunnable(new SynchronizeRunnable());
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (!Synchronizing.setSynchronizing(true))
			return;
		
		super.setVisible(visible);
	}
	
	public class SynchronizeRunnable implements Runnable {
		
		@Override
		public void run() {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				
				private Connection connection;
				private Synchronizer synchronizer;
				
				private String modelTypeToString(ModelType type, boolean plurial) {
					if (plurial) {
						switch (type) {
							case CONTEXT:
								return Translations.getString("general.contexts");
							case FOLDER:
								return Translations.getString("general.folders");
							case GOAL:
								return Translations.getString("general.goals");
							case LOCATION:
								return Translations.getString("general.locations");
							case TASK:
								return Translations.getString("general.tasks");
						}
					}
					
					switch (type) {
						case CONTEXT:
							return Translations.getString("general.context");
						case FOLDER:
							return Translations.getString("general.folder");
						case GOAL:
							return Translations.getString("general.goal");
						case LOCATION:
							return Translations.getString("general.location");
						case TASK:
							return Translations.getString("general.task");
					}
					
					return null;
				}
				
				@Override
				protected Void doInBackground() throws Exception {
					ProgressMonitor monitor = new ProgressMonitor();
					monitor.addListChangeListener(new ListChangeListener() {
						
						@Override
						public void listChange(ListChangeEvent event) {
							if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
								ProgressMessage message = (ProgressMessage) event.getValue();
								
								if (message instanceof SynchronizationProgressMessage) {
									SynchronizationProgressMessage m = (SynchronizationProgressMessage) message;
									
									if (m.getType().equals(
											ProgressMessageType.START))
										SynchronizerDialog.this.appendToProgressStatus(Translations.getString("synchronizer.start_synchronization")
												+ "\n");
									else
										SynchronizerDialog.this.appendToProgressStatus(Translations.getString("synchronizer.synchronization_completed")
												+ "\n");
								} else if (message instanceof RetrieveModelsProgressMessage) {
									RetrieveModelsProgressMessage m = (RetrieveModelsProgressMessage) message;
									
									if (m.getType().equals(
											ProgressMessageType.END))
										return;
									
									String type = modelTypeToString(
											m.getModelType(),
											true);
									SynchronizerDialog.this.appendToProgressStatus(Translations.getString(
											"synchronizer.retrieving_models",
											type) + "\n");
								} else if (message instanceof SynchronizeModelsProgressMessage) {
									SynchronizeModelsProgressMessage m = (SynchronizeModelsProgressMessage) message;
									
									if (m.getType().equals(
											ProgressMessageType.END)
											|| m.getActionCount() == 0)
										return;
									
									String type = modelTypeToString(
											m.getModelType(),
											m.getActionCount() > 1);
									SynchronizerDialog.this.appendToProgressStatus(Translations.getString(
											"synchronizer.synchronizing",
											m.getActionCount(),
											type) + "\n");
								}
							}
						}
						
					});
					
					try {
						SynchronizerDialog.this.appendToProgressStatus(Translations.getString("synchronizer.set_proxy")
								+ "\n");
						
						SynchronizerUtils.initializeProxy();
						
						if (SynchronizerUtils.getPlugin().needsLicense()) {
							SynchronizerDialog.this.appendToProgressStatus(Translations.getString("synchronizer.checking_license")
									+ "\n");
							
							if (!SynchronizerUtils.getPlugin().checkLicense()) {
								SynchronizerDialog.this.appendToProgressStatus(Translations.getString(
										"synchronizer.wait_no_license",
										Constants.WAIT_NO_LICENSE_TIME) + "\n");
								
								SynchronizerDialog.this.appendToProgressStatus(Translations.getString(
										"general.go_to_serial",
										SynchronizerUtils.getPlugin().getName())
										+ "\n");
								
								Thread.sleep(Constants.WAIT_NO_LICENSE_TIME * 1000);
							}
						}
						
						SynchronizerDialog.this.appendToProgressStatus(Translations.getString(
								"synchronizer.connecting",
								SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName())
								+ "\n");
						
						this.connection = SynchronizerUtils.getPlugin().getConnection();
						
						this.connection.loadParameters(Main.SETTINGS);
						this.connection.connect();
						this.connection.saveParameters(Main.SETTINGS);
						
						this.synchronizer = SynchronizerUtils.getPlugin().getSynchronizerApi().getSynchronizer(
								this.connection);
						
						SynchronizerChoice choice = (SynchronizerChoice) Main.SETTINGS.getEnumProperty(
								"synchronizer.choice",
								SynchronizerChoice.class);
						
						this.synchronizer.loadParameters(Main.SETTINGS);
						this.synchronizer.synchronize(choice, monitor);
						this.synchronizer.saveParameters(Main.SETTINGS);
						
						this.connection.disconnect();
						
						Main.SETTINGS.setCalendarProperty(
								"synchronizer.last_synchronization_date",
								Calendar.getInstance());
					} catch (final SynchronizerException e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								ErrorDialog errorDialog = new ErrorDialog(
										MainFrame.getInstance().getFrame(),
										null,
										e,
										!e.isExpected());
								errorDialog.setVisible(true);
							}
							
						});
						
						return null;
					} catch (final Throwable e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								ErrorDialog errorDialog = new ErrorDialog(
										MainFrame.getInstance().getFrame(),
										null,
										e,
										true);
								errorDialog.setVisible(true);
							}
							
						});
						
						return null;
					} finally {
						SynchronizerUtils.removeProxy();
					}
					
					Thread.sleep(1000);
					
					return null;
				}
				
				@Override
				protected void done() {
					SynchronizerUtils.removeOldCompletedTasks();
					
					Synchronizing.setSynchronizing(false);
					
					SynchronizerDialog.this.setCursor(null);
					SynchronizerDialog.this.dispose();
				}
				
			};
			
			SynchronizerDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			worker.execute();
		}
		
	}
	
}
