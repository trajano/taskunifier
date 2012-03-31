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
package com.leclercb.taskunifier.gui.components.batchaddtask;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;

import org.jdesktop.swingx.JXHeader;

import com.leclercb.taskunifier.gui.main.frame.FrameUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class BatchAddTaskDialog extends JDialog {
	
	private static BatchAddTaskDialog INSTANCE;
	
	public static BatchAddTaskDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BatchAddTaskDialog();
		
		return INSTANCE;
	}
	
	private BatchAddTaskDialog() {
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.setLocationRelativeTo(FrameUtils.getCurrentFrameView().getFrame());
		}
		
		super.setVisible(visible);
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.batch_add_tasks"));
		this.setSize(600, 350);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		JXHeader header = new JXHeader();
		header.setTitle(Translations.getString("general.batch_add_tasks"));
		header.setDescription(Translations.getString("batch_add_tasks.insert_task_titles"));
		header.setIcon(ImageUtils.getResourceImage("batch.png", 32, 32));
		
		final BatchAddTaskPanel batchPanel = new BatchAddTaskPanel();
		batchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		
		this.add(header, BorderLayout.NORTH);
		this.add(batchPanel, BorderLayout.CENTER);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				batchPanel.actionCancel();
			}
			
		});
		
		batchPanel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BatchAddTaskDialog.this.setVisible(false);
			}
			
		});
		
		this.getRootPane().setDefaultButton(batchPanel.getOkButton());
	}
	
}
