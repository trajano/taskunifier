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
package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.leclercb.taskunifier.gui.translations.Translations;
import com.michaelbaranov.microba.calendar.CalendarPane;

public class JDatePicker extends JDialog {

	public static enum Action {

		OK, 
		CANCEL;

	}

	private JSpinner timeSpinner;
	private CalendarPane calendarPane;
	private Action action;
	private Calendar calendar;

	public JDatePicker(Frame frame) {
		super(frame);

		this.initialize();
	}

	public Action getAction() {
		return action;
	}

	public Calendar getValue() {
		return calendar;
	}

	public void setValue(Calendar calendar) {
		this.calendar = calendar;

		if (calendar != null) {
			timeSpinner.setValue(calendar.getTime());

			try {
				calendarPane.setDate(calendar.getTime());
			} catch (PropertyVetoException e) {}
		}
	}

	private void initialize() {
		this.setModal(true);
		this.setResizable(false);
		this.setTitle(Translations.getString("general.date_picker"));
		this.setSize(300, 300);
		this.setLayout(new BorderLayout());

		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BorderLayout());

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

		this.add(datePanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);

		this.initializeButtonsPanel(buttonsPanel);
		this.initializeTimeSpinner(datePanel);
		this.initializeDatePanel(datePanel);
	}

	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					action = Action.OK;

					if (calendarPane.getDate() == null) {
						calendar = null;
					} else {
						Calendar time = GregorianCalendar.getInstance();
						time.setTime((Date) timeSpinner.getValue());

						Calendar date = GregorianCalendar.getInstance();
						date.setTime(calendarPane.getDate());

						calendar = date;
						calendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
						calendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
						calendar.set(Calendar.SECOND, time.get(Calendar.SECOND));
					}

					JDatePicker.this.dispose();
				}

				if (event.getActionCommand() == "CANCEL") {
					action = Action.CANCEL;
					calendar = null;
					JDatePicker.this.dispose();
				}
			}

		};

		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);

		JButton cancelButton = new JButton(Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
	}

	private void initializeTimeSpinner(JPanel datePanel) {
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new FlowLayout());

		timeSpinner = new JSpinner();
		timeSpinner.setModel(new SpinnerDateModel());
		timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));

		timePanel.add(timeSpinner);

		datePanel.add(timePanel, BorderLayout.EAST);
	}

	private void initializeDatePanel(JPanel datePanel) {
		calendarPane = new CalendarPane();
		datePanel.add(calendarPane, BorderLayout.CENTER);
	}

}
