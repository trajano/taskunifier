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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.main.frame.FrameUtils;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TULinkDialog extends JDialog {
	
	private TUFileField fileField;
	private TUTextField labelField;
	private boolean cancelled;
	
	public TULinkDialog(boolean open, String title) {
		this.initialize(open, title);
	}
	
	public String getFile() {
		return this.fileField.getFile();
	}
	
	public void setFile(String file) {
		this.fileField.setFile(file);
	}
	
	public String getLabel() {
		return this.labelField.getText();
	}
	
	public void setLabel(String label) {
		this.labelField.setText(label);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.cancelled = false;
			this.setLocationRelativeTo(FrameUtils.getCurrentFrameView().getFrame());
		}
		
		super.setVisible(visible);
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	private void initialize(boolean open, String title) {
		this.setModal(true);
		this.setTitle(title);
		this.setSize(400, 150);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(
				new JLabel(Translations.getString("general.link")),
				BorderLayout.WEST);
		
		this.fileField = new TUFileField(
				open,
				null,
				JFileChooser.FILES_AND_DIRECTORIES,
				null,
				null);
		this.fileField.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panel.add(this.fileField, BorderLayout.CENTER);
		
		this.labelField = new TUTextField(
				Translations.getString("general.label"),
				"");
		this.labelField.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panel.add(this.labelField, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					TULinkDialog.this.cancelled = false;
					TULinkDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("CANCEL")) {
					TULinkDialog.this.cancelled = true;
					TULinkDialog.this.setVisible(false);
				}
			}
			
		};
		
		JButton okButton = new TUOkButton(listener);
		JButton cancelButton = new TUCancelButton(listener);
		
		JPanel panel = new TUButtonsPanel(okButton, cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
