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
package com.leclercb.taskunifier.gui.components.taskedit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXHeader;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

@Deprecated
public class TaskEditDialog extends JDialog {
	
	private static TaskEditDialog INSTANCE;
	
	public static TaskEditDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskEditDialog();
		
		return INSTANCE;
	}
	
	private TaskEditPanel taskEditPanel;
	private boolean cancelled;
	
	private JButton cancelButton;
	
	private TaskEditDialog() {
		super(MainFrame.getInstance().getFrame());
		this.initialize();
	}
	
	public void showCancelButton(boolean b) {
		this.cancelButton.setVisible(b);
	}
	
	public Task getTask() {
		return this.taskEditPanel.getTask();
	}
	
	public void setTask(Task task) {
		this.taskEditPanel.setTask(task);
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b)
			this.cancelled = false;
		
		super.setVisible(b);
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("task_edit"));
		this.setSize(750, 600);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JXHeader header = new JXHeader();
		header.setTitle(Translations.getString("header.title.edit_task"));
		header.setDescription(Translations.getString("header.description.edit_task"));
		header.setIcon(Images.getResourceImage("edit.png", 32, 32));
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				TaskEditDialog.this.cancelled = true;
				TaskEditDialog.this.setTask(null);
				TaskEditDialog.this.setVisible(false);
			}
			
		});
		
		this.taskEditPanel = new TaskEditPanel();
		this.taskEditPanel.setBorder(BorderFactory.createEmptyBorder(
				5,
				10,
				0,
				10));
		
		this.add(header, BorderLayout.NORTH);
		this.add(this.taskEditPanel, BorderLayout.CENTER);
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					TaskEditDialog.this.cancelled = false;
					TaskEditDialog.this.setTask(null);
					TaskEditDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("CANCEL")) {
					TaskEditDialog.this.cancelled = true;
					TaskEditDialog.this.setTask(null);
					TaskEditDialog.this.setVisible(false);
				}
			}
			
		};
		
		JButton okButton = ComponentFactory.createButtonOk(listener);
		this.cancelButton = ComponentFactory.createButtonCancel(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				okButton,
				this.cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
