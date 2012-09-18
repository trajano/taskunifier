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
package com.leclercb.taskunifier.gui.components.welcome.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WelcomePanel extends CardPanel {
	
	private JCheckBox messageReadAgree;
	
	public WelcomePanel(String[] messages, TUButtonsPanel messageButtons) {
		this.initialize(messages, messageButtons);
	}
	
	private void initialize(String[] messages, TUButtonsPanel messageButtons) {
		this.setLayout(new BorderLayout(0, 30));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 30));
		panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
		
		panel.add(new JLabel(
				ImageUtils.getResourceImage("logo.png", 64, 64),
				SwingConstants.CENTER));
		
		panel.add(new JLabel(
				Translations.getString("general.welcome_message"),
				SwingConstants.CENTER), BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.NORTH);
		
		if (messages != null && messages.length > 0) {
			JPanel messagePanel = new JPanel(new BorderLayout());
			
			StringBuffer m = new StringBuffer("<html><ul>");
			
			for (String message : messages) {
				m.append("<li>" + message + "</li>");
			}
			
			m.append("</ul></html>");
			
			JPanel messageAreaPanel = new JPanel(new BorderLayout());
			
			JXEditorPane messageArea = new JXEditorPane(
					"text/html",
					m.toString());
			messageArea.setEditable(false);
			
			this.messageReadAgree = new JCheckBox(
					Translations.getString("welcome.read_and_understand"));
			
			messageAreaPanel.add(
					ComponentFactory.createJScrollPane(messageArea, true),
					BorderLayout.CENTER);
			messageAreaPanel.add(this.messageReadAgree, BorderLayout.SOUTH);
			
			messagePanel.add(messageAreaPanel, BorderLayout.CENTER);
			
			if (messageButtons != null)
				messagePanel.add(messageButtons, BorderLayout.SOUTH);
			
			this.add(messagePanel, BorderLayout.CENTER);
		}
	}
	
	@Override
	public boolean next() {
		if (this.messageReadAgree != null) {
			if (!this.messageReadAgree.isSelected()) {
				this.messageReadAgree.setForeground(Color.RED);
				
				JOptionPane.showMessageDialog(
						null,
						Translations.getString("welcome.check.read_and_understand"),
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
			
			return this.messageReadAgree.isSelected();
		}
		
		return true;
	}
	
	@Override
	public boolean displayInScrollPane() {
		return false;
	}
	
	@Override
	public void saveAndApplyConfig() {
		
	}
	
	@Override
	public void cancelConfig() {
		
	}
	
}
