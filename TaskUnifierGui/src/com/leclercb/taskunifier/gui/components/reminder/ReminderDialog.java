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
package com.leclercb.taskunifier.gui.components.reminder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.main.frame.FrameUtils;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCloseButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ReminderDialog extends JFrame {
	
	private static ReminderDialog INSTANCE;
	
	public static ReminderDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ReminderDialog();
		
		return INSTANCE;
	}
	
	private ReminderPanel reminderPanel;
	
	private ReminderDialog() {
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.setLocationRelativeTo(FrameUtils.getCurrentFrameView().getFrame());
		}
		
		super.setVisible(visible);
	}
	
	public ReminderPanel getReminderPanel() {
		return this.reminderPanel;
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.task.reminder"));
		this.setIconImage(ImageUtils.getResourceImage("logo.png", 16, 16).getImage());
		this.setSize(700, 400);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		this.reminderPanel = new ReminderPanel();
		this.reminderPanel.setBorder(BorderFactory.createEmptyBorder(
				10,
				10,
				0,
				10));
		this.add(this.reminderPanel, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(ReminderPanel.ACTION_SNOOZE)
						|| e.getActionCommand().equals(
								ReminderPanel.ACTION_DISMISS)) {
					if (ReminderDialog.this.reminderPanel.getReminderList().getTasks().length == 0)
						ReminderDialog.this.setVisible(false);
					
					return;
				}
				
				ReminderDialog.this.setVisible(false);
			}
			
		};
		
		this.reminderPanel.addActionListener(listener);
		
		JButton closeButton = new TUCloseButton(listener);
		JPanel panel = new TUButtonsPanel(closeButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(closeButton);
	}
	
}
