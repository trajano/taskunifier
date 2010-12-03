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
package com.leclercb.taskunifier.gui.components.about;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.translations.Translations;

public class AboutDialog extends JDialog {

	public AboutDialog(Frame parent, boolean modal) {
		super(parent, modal);
		this.initialize();
	}

	private void initialize() {
		this.setTitle(Translations.getString("general.about"));
		this.setSize(400, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());

		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());

		AboutPanel aboutPanel = new AboutPanel();
		aboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(aboutPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}

		});

		buttonPanel.add(okButton, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

}
