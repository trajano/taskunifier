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
package com.leclercb.taskunifier.gui.components.plugins;

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

public class PluginsDialog extends JDialog {
	
	public PluginsDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.manage_plugins"));
		this.setSize(650, 400);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(mainPanel, BorderLayout.CENTER);
		
		PluginsPanel pluginsPanel = new PluginsPanel();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		mainPanel.add(pluginsPanel, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "CLOSE") {
					PluginsDialog.this.dispose();
				}
			}
			
		};
		
		JButton closeButton = new JButton(
				Translations.getString("general.close"));
		closeButton.setActionCommand("CLOSE");
		closeButton.addActionListener(listener);
		buttonsPanel.add(closeButton);
		
		this.getRootPane().setDefaultButton(closeButton);
	}
	
}
