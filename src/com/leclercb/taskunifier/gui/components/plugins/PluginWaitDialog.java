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
package com.leclercb.taskunifier.gui.components.plugins;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.WaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class PluginWaitDialog<ResultType> extends WaitDialog {
	
	private ResultType result;
	private ProgressMonitor monitor;
	
	public PluginWaitDialog(Frame frame, String title) {
		super(frame, title);
		this.setRunnable(new PluginRunnable());
		
		this.result = null;
		
		this.monitor = new ProgressMonitor();
		this.monitor.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
					ProgressMessage message = (ProgressMessage) event.getValue();
					PluginWaitDialog.this.appendToProgressStatus(message.toString()
							+ "\n");
				}
			}
		});
	}
	
	public ResultType getResult() {
		return this.result;
	}
	
	public class PluginRunnable implements Runnable {
		
		@Override
		public void run() {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				
				@Override
				protected Void doInBackground() throws Exception {
					try {
						PluginWaitDialog.this.result = PluginWaitDialog.this.doActions(PluginWaitDialog.this.monitor);
					} catch (final PluginException e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								ErrorInfo info = new ErrorInfo(
										Translations.getString("general.error"),
										e.getMessage(),
										null,
										null,
										e,
										null,
										null);
								
								JXErrorPane.showDialog(
										MainFrame.getInstance().getFrame(),
										info);
							}
							
						});
						
						return null;
					} catch (final Throwable e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								ErrorInfo info = new ErrorInfo(
										Translations.getString("general.error"),
										e.getMessage(),
										null,
										null,
										e,
										null,
										null);
								
								JXErrorPane.showDialog(
										MainFrame.getInstance().getFrame(),
										info);
							}
							
						});
						
						return null;
					}
					
					Thread.sleep(1000);
					
					return null;
				}
				
				@Override
				protected void done() {
					PluginWaitDialog.this.setCursor(null);
					PluginWaitDialog.this.dispose();
				}
				
			};
			
			PluginWaitDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			worker.execute();
		}
		
	}
	
	public abstract ResultType doActions(ProgressMonitor monitor)
			throws Throwable;
	
}
