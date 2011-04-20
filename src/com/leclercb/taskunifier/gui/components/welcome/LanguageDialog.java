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
package com.leclercb.taskunifier.gui.components.welcome;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.leclercb.taskunifier.gui.components.configuration.GeneralConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class LanguageDialog extends JDialog {
	
	private GeneralConfigurationPanel generalConfiruationPanel;
	
	public LanguageDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.welcome"));
		this.setSize(400, 120);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.generalConfiruationPanel = new GeneralConfigurationPanel(true);
		this.add(this.generalConfiruationPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonsPanel);
	}
	
	private void initializeButtons(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				LanguageDialog.this.generalConfiruationPanel.saveAndApplyConfig();
				LanguageDialog.this.dispose();
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
	}
	
}
