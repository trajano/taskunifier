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
import java.awt.Frame;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class WaitDialog extends JDialog {
	
	private Runnable runnable;
	private JProgressBar progressBar;
	private JTextArea progressStatus;
	
	public WaitDialog(Frame frame, String title) {
		super(frame);
		this.initialize(title);
	}
	
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
	
	public void appendToProgressStatus(String text) {
		this.progressStatus.append(text);
	}
	
	private void initialize(String title) {
		this.setModal(true);
		this.setTitle(title);
		this.setSize(400, 180);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		this.progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
		this.progressBar.setIndeterminate(true);
		this.progressBar.setString("");
		
		this.progressStatus = new JTextArea();
		this.progressStatus.setEditable(false);
		
		JScrollPane scrollStatus = ComponentFactory.createJScrollPane(
				this.progressStatus,
				true);
		scrollStatus.setAutoscrolls(true);
		scrollStatus.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					
					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						e.getAdjustable().setValue(
								e.getAdjustable().getMaximum());
					}
					
				});
		
		panel.add(this.progressBar, BorderLayout.NORTH);
		panel.add(scrollStatus, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			CheckUtils.isNotNull(this.runnable, "Runnable cannot be null");
			Thread synchronizeThread = new Thread(this.runnable);
			synchronizeThread.start();
		}
		
		super.setVisible(visible);
	}
	
}
