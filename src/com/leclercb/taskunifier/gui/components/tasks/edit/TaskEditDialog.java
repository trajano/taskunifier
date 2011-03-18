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
package com.leclercb.taskunifier.gui.components.tasks.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskEditDialog extends JDialog {
	
	private boolean cancelled;
	
	public TaskEditDialog(Task task, Frame frame, boolean modal) {
		super(frame, modal);
		this.cancelled = false;
		this.initialize(task);
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	private void initialize(Task task) {
		this.setTitle(Translations.getString("task_edit"));
		this.setSize(750, 500);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.CENTER);
		
		TaskEditPanel taskEditPanel = new TaskEditPanel(task);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		JLabel label = new JLabel(
				Translations.getString("task_edit.how_to_edit"));
		label.setForeground(Color.GRAY);
		
		panel.add(label, BorderLayout.NORTH);
		panel.add(taskEditPanel, BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					TaskEditDialog.this.cancelled = false;
					TaskEditDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					TaskEditDialog.this.cancelled = true;
					TaskEditDialog.this.dispose();
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
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
