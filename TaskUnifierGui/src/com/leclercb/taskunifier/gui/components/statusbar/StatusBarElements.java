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
package com.leclercb.taskunifier.gui.components.statusbar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.taskunifier.gui.components.notes.NoteTableView;
import com.leclercb.taskunifier.gui.components.synchronize.progress.SynchronizerProgressMessageListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.views.NoteView;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frame.FrameUtils;
import com.leclercb.taskunifier.gui.main.frame.FrameView;
import com.leclercb.taskunifier.gui.threads.communicator.progress.CommunicatorProgressMessageListener;
import com.leclercb.taskunifier.gui.threads.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;

final class StatusBarElements {
	
	private StatusBarElements() {
		
	}
	
	public static final JLabel createSynchronizerStatus() {
		final JLabel element = new JLabel();
		
		Constants.PROGRESS_MONITOR.addListChangeListener(new SynchronizerProgressMessageListener() {
			
			@Override
			public void showMessage(ProgressMessage message, String content) {
				element.setText(Translations.getString("synchronizer.status")
						+ ": "
						+ content);
			}
			
		});
		
		Constants.PROGRESS_MONITOR.addListChangeListener(new CommunicatorProgressMessageListener() {
			
			@Override
			public void showMessage(ProgressMessage message, String content) {
				element.setText(content);
			}
			
		});
		
		element.setText(Translations.getString("synchronizer.status") + ": ");
		
		return element;
	}
	
	public static final JLabel createLastSynchronizationDate() {
		final JLabel element = new JLabel();
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				Main.getSettings().getStringProperty("date.date_format")
						+ " "
						+ Main.getSettings().getStringProperty(
								"date.time_format"));
		
		String date = Translations.getString("statusbar.never");
		
		if (Main.getUserSettings().getCalendarProperty(
				"synchronizer.last_synchronization_date") != null)
			date = dateFormat.format(Main.getUserSettings().getCalendarProperty(
					"synchronizer.last_synchronization_date").getTime());
		
		element.setText(Translations.getString("statusbar.last_synchronization_date")
				+ ": "
				+ date);
		
		Main.getUserSettings().addPropertyChangeListener(
				"synchronizer.last_synchronization_date",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						String date = Translations.getString("statusbar.never");
						
						if (Main.getUserSettings().getCalendarProperty(
								"synchronizer.last_synchronization_date") != null)
							date = dateFormat.format(Main.getUserSettings().getCalendarProperty(
									"synchronizer.last_synchronization_date").getTime());
						
						element.setText(Translations.getString("statusbar.last_synchronization_date")
								+ ": "
								+ date);
					}
					
				});
		
		return element;
	}
	
	public static final JLabel createScheduledSyncStatus(
			final ScheduledSyncThread thread) {
		final JLabel element = new JLabel();
		
		updateScheduledSyncStatus(element, thread);
		
		Main.getUserSettings().addPropertyChangeListener(
				"synchronizer.scheduler_enabled",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						updateScheduledSyncStatus(element, thread);
					}
					
				});
		
		thread.addPropertyChangeListener(
				ScheduledSyncThread.PROP_REMAINING_SLEEP_TIME,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						updateScheduledSyncStatus(element, thread);
					}
					
				});
		
		return element;
	}
	
	private static final void updateScheduledSyncStatus(
			JLabel element,
			ScheduledSyncThread thread) {
		String text = null;
		
		if (Main.getUserSettings().getBooleanProperty(
				"synchronizer.scheduler_enabled")) {
			long sleep = thread.getRemainingSleepTime();
			sleep = sleep / 1000;
			
			String time = "";
			time += (sleep / 3600) + "h ";
			time += ((sleep % 3600) / 60) + "m";
			
			text = Translations.getString("statusbar.next_scheduled_sync", time);
		} else {
			text = Translations.getString(
					"statusbar.next_scheduled_sync",
					Translations.getString("statusbar.never"));
		}
		
		element.setText(text);
	}
	
	public static final JLabel createRowCount(int frameId) {
		final JLabel element = new JLabel();
		
		updateRowCount(element);
		
		final PropertyChangeListener listener = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateRowCount(element);
			}
			
		};
		
		FrameUtils.getFrameView(frameId).addPropertyChangeListener(
				FrameView.PROP_SELECTED_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						if (event != null && event.getOldValue() != null) {
							ViewItem oldView = (ViewItem) event.getOldValue();
							
							if (oldView.getViewType() == ViewType.NOTES) {
								((NoteView) oldView.getView()).getNoteTableView().removePropertyChangeListener(
										listener);
							}
							
							if (oldView.getViewType() == ViewType.TASKS) {
								((TaskView) oldView.getView()).getTaskTableView().removePropertyChangeListener(
										listener);
							}
						}
						
						if (ViewList.getInstance().getCurrentView().isLoaded()) {
							if (ViewUtils.getCurrentViewType() == ViewType.NOTES) {
								ViewUtils.getCurrentNoteView().getNoteTableView().addPropertyChangeListener(
										NoteTableView.PROP_NOTE_COUNT,
										listener);
							}
							
							if (ViewUtils.getCurrentViewType() == ViewType.TASKS) {
								ViewUtils.getCurrentTaskView().getTaskTableView().addPropertyChangeListener(
										TaskTableView.PROP_TASK_COUNT,
										listener);
							}
						}
					}
				});
		
		return element;
	}
	
	private static final void updateRowCount(JLabel element) {
		ViewType viewType = ViewUtils.getCurrentViewType();
		
		switch (viewType) {
			case NOTES:
				element.setText(ViewUtils.getCurrentNoteView().getNoteTableView().getNoteCount()
						+ " "
						+ Translations.getString("general.notes"));
				break;
			case TASKS:
				element.setText(ViewUtils.getCurrentTaskView().getTaskTableView().getTaskCount()
						+ " "
						+ Translations.getString("general.tasks"));
				break;
			default:
				element.setText("");
				break;
		}
	}
	
	public static final JLabel createCurrentDateTime() {
		String dateFormat = Main.getSettings().getStringProperty(
				"date.date_format");
		String timeFormat = Main.getSettings().getStringProperty(
				"date.time_format");
		
		final SimpleDateFormat format = new SimpleDateFormat(dateFormat
				+ " "
				+ timeFormat);
		final JLabel element = new JLabel();
		
		updateCurrentDateTime(element, format);
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				updateCurrentDateTime(element, format);
			}
			
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, 10000, 10000);
		
		return element;
	}
	
	private static final void updateCurrentDateTime(
			JLabel element,
			SimpleDateFormat format) {
		element.setText(format.format(Calendar.getInstance().getTime()));
	}
	
}
