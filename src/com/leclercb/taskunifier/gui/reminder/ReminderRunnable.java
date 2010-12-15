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
package com.leclercb.taskunifier.gui.reminder;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ReminderRunnable implements Runnable, PropertyChangeListener {

	private static final long SLEEP_TIME = 10000;

	private List<Task> notifiedTasks;

	public ReminderRunnable() {
		this.notifiedTasks = new ArrayList<Task>();

		TaskFactory.getInstance().addPropertyChangeListener(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {

			}

			List<Task> list = TaskFactory.getInstance().getList();
			for (final Task task : list) {
				if (this.notifiedTasks.contains(task))
					continue;

				if (task.getModelStatus().equals(ModelStatus.LOADED)
						|| task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
					if (task.getDueDate() != null) {
						long milliSeconds1 = task.getDueDate().getTimeInMillis();
						long milliSeconds2 = Calendar.getInstance().getTimeInMillis();
						long diff = milliSeconds1 - milliSeconds2;
						final double diffMinutes = diff / (60 * 1000.0);

						if (diffMinutes >= 0 && diffMinutes <= task.getReminder()) {
							this.notifiedTasks.remove(task);
							this.notifiedTasks.add(task);

							EventQueue.invokeLater(new Runnable() {

								@Override
								public void run() {
									Object[] options = { Translations.getString("general.show"),
											Translations.getString("general.cancel") };

									int n = JOptionPane.showOptionDialog(MainFrame.getInstance().getFrame(),
											Translations.getString("reminder.message",
													task.getTitle(),
													(int) diffMinutes),
											"Reminder",
											JOptionPane.YES_NO_OPTION,
											JOptionPane.INFORMATION_MESSAGE,
											null,
											options,
											options[0]);

									if (n == JOptionPane.YES_OPTION) {
										MainFrame.getInstance().selectDefaultTaskSearcher();
										MainFrame.getInstance().setSelectedTask(task);
									}
								}

							});
						}
					}
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Task.PROP_DUE_DATE) || evt.getPropertyName().equals(Task.PROP_REMINDER))
			this.notifiedTasks.remove(evt.getSource());
	}

}
