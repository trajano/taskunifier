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
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.JXHeader;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class BatchTaskEditDialog extends JDialog {
	
	private static BatchTaskEditDialog INSTANCE;
	
	public static BatchTaskEditDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BatchTaskEditDialog();
		
		return INSTANCE;
	}
	
	private JXHeader header;
	private BatchTaskEditPanel batchTaskEditPanel;
	private boolean cancelled;
	
	private BatchTaskEditDialog() {
		super(MainFrame.getInstance().getFrame());
		this.initialize();
	}
	
	public Task[] getTasks() {
		return this.batchTaskEditPanel.getTasks();
	}
	
	public void setTasks(Task[] tasks) {
		if (tasks != null && tasks.length == 1) {
			this.header.setTitle(Translations.getString("header.title.edit_task"));
			this.header.setDescription(Translations.getString("header.description.edit_task"));
		} else {
			this.header.setTitle(Translations.getString("header.title.batch_edit_task"));
			this.header.setDescription(Translations.getString("header.description.batch_edit_task"));
		}
		
		this.batchTaskEditPanel.setTasks(tasks);
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
		this.setTitle(Translations.getString("batch_task_edit"));
		this.setSize(750, 600);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.header = new JXHeader();
		this.header.setTitle(Translations.getString("header.title.batch_edit_task"));
		this.header.setDescription(Translations.getString("header.description.batch_edit_task"));
		this.header.setIcon(Images.getResourceImage("edit.png", 32, 32));
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				BatchTaskEditDialog.this.cancelled = true;
				BatchTaskEditDialog.this.setTasks(null);
				BatchTaskEditDialog.this.setVisible(false);
			}
			
		});
		
		this.batchTaskEditPanel = new BatchTaskEditPanel();
		this.batchTaskEditPanel.setBorder(BorderFactory.createEmptyBorder(
				5,
				10,
				0,
				10));
		
		this.add(this.header, BorderLayout.NORTH);
		this.add(this.batchTaskEditPanel, BorderLayout.CENTER);
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (EqualsUtils.equals(event.getActionCommand(), "OK")) {
					BatchTaskEditDialog.this.cancelled = false;
					BatchTaskEditDialog.this.batchTaskEditPanel.editTasks();
					BatchTaskEditDialog.this.setTasks(null);
					BatchTaskEditDialog.this.setVisible(false);
					return;
				}
				
				BatchTaskEditDialog.this.cancelled = true;
				BatchTaskEditDialog.this.setTasks(null);
				BatchTaskEditDialog.this.setVisible(false);
			}
			
		};
		
		JButton okButton = ComponentFactory.createButtonOk(listener);
		JButton cancelButton = ComponentFactory.createButtonCancel(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				okButton,
				cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
		
		this.getRootPane().registerKeyboardAction(
				listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
}
