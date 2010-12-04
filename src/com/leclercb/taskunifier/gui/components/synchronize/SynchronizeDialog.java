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
import java.net.Proxy;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.leclercb.taskunifier.api.event.listchange.ListChangeEvent;
import com.leclercb.taskunifier.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.progress.ProgressMessage;
import com.leclercb.taskunifier.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.toodledo.ToodledoConnection;
import com.leclercb.taskunifier.api.toodledo.ToodledoConnectionFactory;
import com.leclercb.taskunifier.api.toodledo.ToodledoProxy;
import com.leclercb.taskunifier.api.toodledo.ToodledoSynchronizer;
import com.leclercb.taskunifier.api.toodledo.ToodledoSynchronizerChoice;
import com.leclercb.taskunifier.api.toodledo.ToodledoSynchronizerFactory;
import com.leclercb.taskunifier.api.toodledo.exc.ToodledoException;
import com.leclercb.taskunifier.api.toodledo.progress.messages.ProgressMessageType;
import com.leclercb.taskunifier.api.toodledo.progress.messages.RetrieveModelsProgressMessage;
import com.leclercb.taskunifier.api.toodledo.progress.messages.SynchronizationProgressMessage;
import com.leclercb.taskunifier.api.toodledo.progress.messages.SynchronizeModelsProgressMessage;
import com.leclercb.taskunifier.gui.translations.Translations;

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

		this.progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		this.progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
		this.progressBar.setIndeterminate(true);
		this.progressBar.setString("");

		this.progressStatus = new JTextArea();
		this.progressStatus.setEditable(false);

		JScrollPane scrollStatus = new JScrollPane(this.progressStatus);
		scrollStatus.setAutoscrolls(true);
		scrollStatus.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  

			public void adjustmentValueChanged(AdjustmentEvent e) {  
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
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

				private ToodledoSynchronizer synchronizer;

				private String modelTypeToString(ModelType type, boolean plurial) {
					if (plurial) {
						switch (type) {
						case CONTEXT: return Translations.getString("general.contexts");
						case FOLDER: return Translations.getString("general.folders");
						case GOAL: return Translations.getString("general.goals");
						case TASK: return Translations.getString("general.tasks");
						}
					}

					switch (type) {
					case CONTEXT: return Translations.getString("general.context");
					case FOLDER: return Translations.getString("general.folder");
					case GOAL: return Translations.getString("general.goal");
					case TASK: return Translations.getString("general.task");
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

									if (m.getType().equals(ProgressMessageType.START))
										progressStatus.append(Translations.getString("synchronize.start_synchronization") + "\n");
									else
										progressStatus.append(Translations.getString("synchronize.synchronization_completed") + "\n");
								} else if (message instanceof RetrieveModelsProgressMessage) {
									RetrieveModelsProgressMessage m = (RetrieveModelsProgressMessage) message;

									if (m.getType().equals(ProgressMessageType.END))
										return;

									String type = modelTypeToString(m.getModelType(), true);
									progressStatus.append(String.format(Translations.getString("synchronize.retrieving_models") + "\n", type));
								} else if (message instanceof SynchronizeModelsProgressMessage) {
									SynchronizeModelsProgressMessage m = (SynchronizeModelsProgressMessage) message;

									if (m.getType().equals(ProgressMessageType.END) || m.getActionCount() == 0)
										return;

									String type = modelTypeToString(m.getModelType(), m.getActionCount() > 1);
									progressStatus.append(String.format(Translations.getString("synchronize.synchronizing") + "\n", m.getActionCount(), type));
								}
							}
						}

					});

					progressStatus.append(Translations.getString("synchronize.set_proxy") + "\n");
					initializeProxy();

					progressStatus.append(Translations.getString("synchronize.connecting_toodledo") + "\n");

					ToodledoConnection connection = null;

					try {
						connection = ToodledoConnectionFactory.getInstance().getConnection(
								Settings.getStringProperty("toodledo.email"),
								Settings.getStringProperty("toodledo.password"),
								Settings.getStringProperty("toodledo.userid"),
								Settings.getStringProperty("toodledo.token"));

						connection.connect();
					} catch (final Exception e) {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								JOptionPane.showMessageDialog(
										null, 
										e.getMessage(), 
										"Error during connection with Toodledo", 
										JOptionPane.ERROR_MESSAGE);
							}

						});

						return null;
					}

					Settings.setStringProperty("toodledo.userid", connection.getUserId());
					Settings.setStringProperty("toodledo.token", connection.getToken());

					synchronizer = ToodledoSynchronizerFactory.getInstance().getSynchronizer(connection);

					initializeSynchronizer(synchronizer);

					final ToodledoSynchronizerChoice choice = (ToodledoSynchronizerChoice) Settings.getEnumProperty(
							"synchronizer.choice", 
							ToodledoSynchronizerChoice.class);

					try {
						synchronizer.synchronize(choice, monitor);
					} catch (ToodledoException e) {
						JOptionPane.showMessageDialog(
								null, 
								e.getMessage(), 
								"Error during synchronization", 
								JOptionPane.ERROR_MESSAGE);
					}

					Thread.sleep(1000);

					return null;
				}

				@Override
				protected void done() {
					if (synchronizer != null)
						saveSynchronizerState(synchronizer);

					removeProxy();

					setCursor(null);
					dispose();
				}

			};

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			worker.execute();
		}

	}

	private void initializeProxy() {
		Boolean proxyEnabled = Settings.getBooleanProperty("proxy.enabled");
		if (proxyEnabled != null && proxyEnabled) {
			Proxy.Type type = (Proxy.Type) Settings.getEnumProperty("proxy.type", Proxy.Type.class);
			String host = Settings.getStringProperty("proxy.host");
			Integer port = Settings.getIntegerProperty("proxy.port");
			String login = Settings.getStringProperty("proxy.login");
			String password = Settings.getStringProperty("proxy.password");

			ToodledoProxy.setProxy(type, host, port, login, password);
		}
	}

	private void initializeSynchronizer(ToodledoSynchronizer synchronizer) {
		synchronizer.setKeepTasksCompletedForXDays(Settings.getIntegerProperty("synchronizer.keep_tasks_completed_for_x_days"));
		synchronizer.setLastContextEdit(Settings.getCalendarProperty("synchronizer.last_context_edit"));
		synchronizer.setLastFolderEdit(Settings.getCalendarProperty("synchronizer.last_folder_edit"));
		synchronizer.setLastGoalEdit(Settings.getCalendarProperty("synchronizer.last_goal_edit"));
		synchronizer.setLastTaskAddEdit(Settings.getCalendarProperty("synchronizer.last_task_add_edit"));
		synchronizer.setLastTaskDelete(Settings.getCalendarProperty("synchronizer.last_task_delete"));
	}

	private void saveSynchronizerState(ToodledoSynchronizer synchronizer) {
		Settings.setCalendarProperty("synchronizer.last_synchronization_date", GregorianCalendar.getInstance());
		Settings.setIntegerProperty("synchronizer.keep_tasks_completed_for_x_days", synchronizer.getKeepTasksCompletedForXDays());
		Settings.setCalendarProperty("synchronizer.last_context_edit", synchronizer.getLastContextEdit());
		Settings.setCalendarProperty("synchronizer.last_folder_edit", synchronizer.getLastFolderEdit());
		Settings.setCalendarProperty("synchronizer.last_goal_edit", synchronizer.getLastGoalEdit());
		Settings.setCalendarProperty("synchronizer.last_task_add_edit", synchronizer.getLastTaskAddEdit());
		Settings.setCalendarProperty("synchronizer.last_task_delete", synchronizer.getLastTaskDelete());
	}

	private void removeProxy() {
		ToodledoProxy.removeProxy();
	}

}
