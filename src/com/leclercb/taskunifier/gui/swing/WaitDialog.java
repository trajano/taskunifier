/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
		super(frame, true);
		this.initialize(title);
	}
	
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
	
	public void appendToProgressStatus(String text) {
		this.progressStatus.append(text);
	}
	
	private void initialize(String title) {
		this.setTitle(title);
		this.setSize(400, 180);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
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
		CheckUtils.isNotNull(this.runnable, "Runnable cannot be null");
		
		Thread synchronizeThread = new Thread(this.runnable);
		synchronizeThread.start();
		super.setVisible(visible);
	}
	
}
