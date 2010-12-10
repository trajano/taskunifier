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
package com.leclercb.taskunifier.gui.components.models;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.leclercb.taskunifier.gui.translations.Translations;

public class ModelConfigurationDialog extends JDialog {

	public ModelConfigurationDialog(Frame frame, boolean modal) {
		super(frame, modal);

		this.initialize();
	}

	private void initialize() {
		this.setTitle(Translations.getString("general.manage_models"));
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLayout(new BorderLayout());

		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);

		this.initializeButtonsPanel(buttonsPanel);

		tabbedPane.addTab(
				Translations.getString("general.contexts"), 
				new ContextConfigurationPanel());

		tabbedPane.addTab(
				Translations.getString("general.folders"), 
				new FolderConfigurationPanel());

		tabbedPane.addTab(
				Translations.getString("general.goals"), 
				new GoalConfigurationPanel());

		tabbedPane.addTab(
				Translations.getString("general.locations"), 
				new LocationConfigurationPanel());
	}

	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					ModelConfigurationDialog.this.dispose();
				}
			}

		};

		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
	}

}
