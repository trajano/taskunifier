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
package com.leclercb.taskunifier.gui.components.review;

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

public class ReviewDialog extends JDialog {
	
	public ReviewDialog(Frame frame, boolean modal) {
		super(frame, modal);
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.review"));
		this.setSize(600, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		ReviewPanel reviewPanel = new ReviewPanel();
		reviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(reviewPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonPanel);
	}
	
	private void initializeButtons(JPanel buttonPanel) {
		ActionListener actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ReviewDialog.this.setVisible(false);
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.addActionListener(actionListener);
		buttonPanel.add(okButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
