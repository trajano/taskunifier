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
package com.leclercb.taskunifier.gui.components.plugins;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.WaitDialog;

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
								ErrorDialog dialog = new ErrorDialog(
										MainFrame.getInstance().getFrame(),
										e.getMessage(),
										e,
										false);
								dialog.setVisible(true);
							}
							
						});
						
						return null;
					} catch (final Throwable e) {
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								ErrorDialog dialog = new ErrorDialog(
										MainFrame.getInstance().getFrame(),
										null,
										e,
										true);
								dialog.setVisible(true);
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
