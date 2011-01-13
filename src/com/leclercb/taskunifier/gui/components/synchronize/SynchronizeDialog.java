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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.settings.Settings;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerApiException;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.ProgressMessageType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.RetrieveModelsProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizationProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizeModelsProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoConnection;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoConnectionFactory;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoSynchronizer;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoSynchronizerFactory;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizeDialog extends JDialog {
	
	private JProgressBar progressBar;
	private JTextArea progressStatus;
	
	public SynchronizeDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.synchronization"));
		this.setSize(400, 180);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		this.progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
		this.progressBar.setIndeterminate(true);
		this.progressBar.setString("");
		
		this.progressStatus = new JTextArea();
		this.progressStatus.setEditable(false);
		
		JScrollPane scrollStatus = new JScrollPane(this.progressStatus);
		scrollStatus.setAutoscrolls(true);
		scrollStatus.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					
					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						e.getAdjustable().setValue(
								e.getAdjustable().getMaximum());
					}
					
				});
		
		panel.add(this.progressBar, BorderLayout.NORTH);
		panel.add(scrollStatus, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public void setVisible(boolean visible) {
		Thread synchronizeThread = new Thread(new SynchronizeRunnable());
		synchronizeThread.start();
		super.setVisible(visible);
	}
	
	public class SynchronizeRunnable implements Runnable {
		
		@Override
		public void run() {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				
				private ToodledoConnection connection;
				private ToodledoSynchronizer synchronizer;
				
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
										SynchronizeDialog.this.progressStatus.append(Translations.getString("synchronize.start_synchronization")
												+ "\n");
									else
										SynchronizeDialog.this.progressStatus.append(Translations.getString("synchronize.synchronization_completed")
												+ "\n");
								} else if (message instanceof RetrieveModelsProgressMessage) {
									RetrieveModelsProgressMessage m = (RetrieveModelsProgressMessage) message;
									
									if (m.getType().equals(
											ProgressMessageType.END))
										return;
									
									String type = modelTypeToString(
											m.getModelType(),
											true);
									SynchronizeDialog.this.progressStatus.append(Translations.getString(
											"synchronize.retrieving_models",
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
									SynchronizeDialog.this.progressStatus.append(Translations.getString(
											"synchronize.synchronizing",
											m.getActionCount(),
											type) + "\n");
								}
							}
						}
						
					});
					
					try {
						SynchronizeDialog.this.progressStatus.append(Translations.getString("synchronize.set_proxy")
								+ "\n");
						
						SynchronizerUtils.initializeProxy();
						
						SynchronizeDialog.this.progressStatus.append(Translations.getString("synchronize.connecting_toodledo")
								+ "\n");
						
						if (Settings.getStringProperty("toodledo.email") == null)
							throw new Exception("Please fill in your email");
						
						if (Settings.getStringProperty("toodledo.password") == null)
							throw new Exception("Please fill in your password");
						
						this.connection = ToodledoConnectionFactory.getInstance().getConnection(
								Settings.getStringProperty("toodledo.email"),
								Settings.getStringProperty("toodledo.password"),
								Settings.getStringProperty("toodledo.userid"),
								Settings.getStringProperty("toodledo.token"),
								Settings.getCalendarProperty("toodledo.token_creation_date="));
						
						this.connection.connect();
						
						Settings.setStringProperty(
								"toodledo.userid",
								this.connection.getUserId());
						Settings.setStringProperty(
								"toodledo.token",
								this.connection.getToken());
						
						this.synchronizer = ToodledoSynchronizerFactory.getInstance().getSynchronizer(
								this.connection);
						
						SynchronizerUtils.initializeSynchronizer(this.synchronizer);
						
						SynchronizerChoice choice = (SynchronizerChoice) Settings.getEnumProperty(
								"synchronizer.choice",
								SynchronizerChoice.class);
						
						this.synchronizer.synchronize(choice, monitor);
					} catch (final SynchronizerApiException e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								// TODO : replace e.getMessage() by translated
								// error message
								ErrorDialog errorDialog = new ErrorDialog(
										MainFrame.getInstance().getFrame(),
										e.getMessage(),
										e);
								errorDialog.setVisible(true);
							}
							
						});
						
						return null;
					} catch (final Exception e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								ErrorDialog errorDialog = new ErrorDialog(
										MainFrame.getInstance().getFrame(),
										e,
										true);
								errorDialog.setVisible(true);
							}
							
						});
						
						return null;
					}
					
					Thread.sleep(1000);
					
					return null;
				}
				
				@Override
				protected void done() {
					if (this.connection != null)
						Settings.setCalendarProperty(
								"toodledo.token_creation_date",
								this.connection.getTokenCreationDate());
					
					if (this.synchronizer != null)
						SynchronizerUtils.saveSynchronizerState(this.synchronizer);
					
					SynchronizerUtils.removeProxy();
					
					SynchronizeDialog.this.setCursor(null);
					SynchronizeDialog.this.dispose();
				}
				
			};
			
			SynchronizeDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			worker.execute();
		}
	}
	
}
