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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.toedter.calendar.JCalendar;

public class JDatePicker extends JDialog {
	
	public static enum Action {
		
		OK,
		CANCEL;
		
	}
	
	private JFormattedTextField timeField;
	private JCalendar calendarPane;
	private Action action;
	private Calendar calendar;
	
	public JDatePicker(Frame frame) {
		super(frame);
		
		this.initialize();
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public Calendar getValue() {
		return this.calendar;
	}
	
	public void setValue(Calendar calendar) {
		this.calendar = calendar;
		
		if (calendar != null) {
			this.timeField.setValue(calendar.getTime());
			this.calendarPane.setDate(calendar.getTime());
		}
	}
	
	private void initialize() {
		this.setModal(true);
		this.setResizable(false);
		this.setTitle(Translations.getString("general.date_picker"));
		this.setSize(400, 300);
		this.setLayout(new BorderLayout());
		
		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BorderLayout());
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		this.add(datePanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
		this.initializeTimeField(datePanel);
		this.initializeDatePanel(datePanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					JDatePicker.this.action = Action.OK;
					
					if (JDatePicker.this.calendarPane.getDate() == null) {
						JDatePicker.this.calendar = null;
					} else {
						Calendar time = Calendar.getInstance();
						
						if (timeField.getValue() != null)
							time.setTime((Date) JDatePicker.this.timeField.getValue());
						
						Calendar date = Calendar.getInstance();
						date.setTime(JDatePicker.this.calendarPane.getDate());
						
						JDatePicker.this.calendar = date;
						JDatePicker.this.calendar.set(
								Calendar.HOUR_OF_DAY,
								time.get(Calendar.HOUR_OF_DAY));
						JDatePicker.this.calendar.set(
								Calendar.MINUTE,
								time.get(Calendar.MINUTE));
						JDatePicker.this.calendar.set(
								Calendar.SECOND,
								time.get(Calendar.SECOND));
					}
					
					JDatePicker.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					JDatePicker.this.action = Action.CANCEL;
					JDatePicker.this.calendar = null;
					JDatePicker.this.dispose();
				}
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
	}
	
	private void initializeTimeField(JPanel datePanel) {
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		timePanel.add(new JLabel(Translations.getString("general.time") + ": "));
		
		SimpleDateFormat format = new SimpleDateFormat(
				Main.SETTINGS.getStringProperty("date.time_format"));
		
		this.timeField = new JFormattedTextField(format);
		this.timeField.setColumns(15);
		
		timePanel.add(this.timeField);
		timePanel.add(new JLabel(" ("
				+ format.format(Calendar.getInstance().getTime())
				+ ")"));
		
		datePanel.add(timePanel, BorderLayout.NORTH);
	}
	
	private void initializeDatePanel(JPanel datePanel) {
		this.calendarPane = new JCalendar();
		this.calendarPane.setLocale(Translations.getLocale());
		datePanel.add(this.calendarPane, BorderLayout.CENTER);
	}
	
}
