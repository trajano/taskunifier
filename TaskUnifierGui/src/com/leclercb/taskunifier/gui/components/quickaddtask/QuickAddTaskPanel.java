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
package com.leclercb.taskunifier.gui.components.quickaddtask;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.actions.ActionAddQuickTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTaskMenu;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class QuickAddTaskPanel extends JPanel {
	
	private JTextField textField;
	private JButton button;
	private JButton buttonTemplate;
	
	public QuickAddTaskPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setOpaque(false);
		
		this.textField = new JTextField();
		this.textField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					QuickAddTaskPanel.this.addQuickTask();
				}
			}
			
		});
		
		this.button = new JButton(ImageUtils.getResourceImage(
				"task.png",
				16,
				16));
		this.button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				QuickAddTaskPanel.this.addQuickTask();
			}
			
		});
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionAddTemplateTask action = (ActionAddTemplateTask) e.getSource();
				QuickAddTaskPanel.this.addQuickTask(action.getTemplate());
			}
			
		};
		
		this.buttonTemplate = new JButton(new ActionAddTemplateTaskMenu(
				16,
				16,
				listener));
		this.buttonTemplate.setText(null);
		
		this.setLayout(new BorderLayout(3, 3));
		this.add(this.textField, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new BorderLayout(3, 3));
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(this.button, BorderLayout.WEST);
		buttonsPanel.add(this.buttonTemplate, BorderLayout.EAST);
		
		this.add(buttonsPanel, BorderLayout.EAST);
	}
	
	private void addQuickTask() {
		String title = this.textField.getText();
		ActionAddQuickTask.addQuickTask(title, false);
		this.textField.setText("");
	}
	
	private void addQuickTask(TaskTemplate template) {
		String title = this.textField.getText();
		ActionAddQuickTask.addQuickTask(template, title, false);
		this.textField.setText("");
	}
	
}
