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
package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.leclercb.taskunifier.gui.translations.Translations;

public class JTextAreaDialog extends JDialog {
	
	private String question;
	private String initialAnswer;
	private String answer;
	
	private JTextArea answerTextArea;
	
	public JTextAreaDialog(Frame frame, String title, String question) {
		this(frame, title, question, "");
	}
	
	public JTextAreaDialog(
			Frame frame,
			String title,
			String question,
			String initialAnswer) {
		super(frame, title, true);
		
		if (initialAnswer == null)
			initialAnswer = "";
		
		this.question = question;
		this.initialAnswer = initialAnswer;
		this.initialize();
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	private void initialize() {
		this.setSize(500, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = null;
		
		panel = new JPanel(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(panel, BorderLayout.NORTH);
		
		JLabel icon = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
		panel.add(icon, BorderLayout.WEST);
		
		JLabel question = new JLabel(this.question);
		panel.add(question, BorderLayout.CENTER);
		
		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		this.answerTextArea = new JTextArea(this.initialAnswer);
		this.answerTextArea.setEditable(true);
		panel.add(new JScrollPane(this.answerTextArea), BorderLayout.CENTER);
		this.add(panel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					JTextAreaDialog.this.answer = JTextAreaDialog.this.answerTextArea.getText();
					JTextAreaDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					JTextAreaDialog.this.answer = null;
					JTextAreaDialog.this.dispose();
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
