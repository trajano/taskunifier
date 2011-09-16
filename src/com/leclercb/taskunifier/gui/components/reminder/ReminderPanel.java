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
package com.leclercb.taskunifier.gui.components.reminder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ReminderPanel extends JPanel {
	
	public static final String ACTION_SNOOZE = "ACTION_SNOOZE";
	public static final String ACTION_DISMISS = "ACTION_DISMISS";
	
	private ActionSupport actionSupport;
	
	private ReminderList reminderList;
	
	public ReminderPanel() {
		this.actionSupport = new ActionSupport(this);
		
		this.initialize();
	}
	
	public ReminderList getReminderList() {
		return this.reminderList;
	}
	
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
	private void snooze(Task[] tasks, int minutes) {
		for (Task task : tasks) {
			if (TaskUtils.isInDueDateReminderZone(task)) {
				task.setDueDateReminder(minutes);
			}
			
			if (TaskUtils.isInStartDateReminderZone(task)) {
				task.setStartDateReminder(minutes);
			}
		}
		
		this.actionSupport.fireActionPerformed(0, ACTION_SNOOZE);
	}
	
	public void snooze(int minutes) {
		Task[] tasks = this.reminderList.getSelectedTasks();
		this.snooze(tasks, minutes);
	}
	
	public void snoozeAll(int minutes) {
		Task[] tasks = this.reminderList.getTasks();
		this.snooze(tasks, minutes);
	}
	
	private void dismiss(Task[] tasks) {
		for (Task task : tasks) {
			this.reminderList.removeTask(task);
		}
		
		this.actionSupport.fireActionPerformed(0, ACTION_DISMISS);
	}
	
	public void dismiss() {
		Task[] tasks = this.reminderList.getSelectedTasks();
		this.dismiss(tasks);
	}
	
	public void dismissAll() {
		Task[] tasks = this.reminderList.getTasks();
		this.dismiss(tasks);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 10));
		
		this.reminderList = new ReminderList();
		this.add(this.reminderList, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		final JPopupMenu snoozePopup = new JPopupMenu();
		snoozePopup.add(new SnoozeAction(5));
		snoozePopup.add(new SnoozeAction(10));
		snoozePopup.add(new SnoozeAction(15));
		snoozePopup.add(new SnoozeAction(20));
		snoozePopup.add(new SnoozeAction(30));
		snoozePopup.add(new SnoozeAction(60));
		
		final JPopupMenu snoozeAllPopup = new JPopupMenu();
		snoozeAllPopup.add(new SnoozeAllAction(5));
		snoozeAllPopup.add(new SnoozeAllAction(10));
		snoozeAllPopup.add(new SnoozeAllAction(15));
		snoozeAllPopup.add(new SnoozeAllAction(20));
		snoozeAllPopup.add(new SnoozeAllAction(30));
		snoozeAllPopup.add(new SnoozeAllAction(60));
		
		final JButton snoozeButton = new JButton(
				Translations.getString("general.snooze"));
		snoozeButton.setActionCommand("SNOOZE");
		
		final JButton snoozeAllButton = new JButton(
				Translations.getString("general.snooze_all"));
		snoozeAllButton.setActionCommand("SNOOZE_ALL");
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("SNOOZE")) {
					snoozePopup.show(snoozeButton, 0, 0);
					return;
				}
				
				if (event.getActionCommand().equals("SNOOZE_ALL")) {
					snoozeAllPopup.show(snoozeButton, 0, 0);
					return;
				}
				
				if (event.getActionCommand().equals("DISMISS")) {
					ReminderPanel.this.dismiss();
					return;
				}
				
				if (event.getActionCommand().equals("DISMISS_ALL")) {
					ReminderPanel.this.dismissAll();
					return;
				}
			}
			
		};
		
		snoozeButton.addActionListener(listener);
		snoozeAllButton.addActionListener(listener);
		
		JButton dismissButton = new JButton(
				Translations.getString("general.dismiss"));
		dismissButton.setActionCommand("DISMISS");
		dismissButton.addActionListener(listener);
		
		JButton dismissAllButton = new JButton(
				Translations.getString("general.dismiss_all"));
		dismissAllButton.setActionCommand("DISMISS_ALL");
		dismissAllButton.addActionListener(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				snoozeButton,
				snoozeAllButton,
				dismissButton,
				dismissAllButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
	private class SnoozeAction extends AbstractAction {
		
		private int minutes;
		
		public SnoozeAction(int minutes) {
			super(Translations.getString(
					"general.task.reminder.remind_x_minutes_before",
					minutes));
			this.minutes = minutes;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			ReminderPanel.this.snooze(this.minutes);
		}
		
	}
	
	private class SnoozeAllAction extends AbstractAction {
		
		private int minutes;
		
		public SnoozeAllAction(int minutes) {
			super(Translations.getString(
					"general.task.reminder.x_minutes",
					minutes));
			this.minutes = minutes;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			ReminderPanel.this.snoozeAll(this.minutes);
		}
		
	}
	
}
