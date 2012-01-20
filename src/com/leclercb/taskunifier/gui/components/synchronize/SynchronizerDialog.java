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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.taskunifier.gui.actions.ActionGetSerial;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.components.synchronize.progress.SynchronizerProgressMessageListener;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUWaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizerDialog extends TUWaitDialog {
	
	public SynchronizerDialog() {
		super(
				MainFrame.getInstance().getFrame(),
				Translations.getString("general.synchronization"));
		final SynchronizerDialogWorker worker = new SynchronizerDialogWorker();
		this.setWorker(worker);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				worker.stop();
			}
			
		});
		
		try {
			SynchronizerGuiPlugin plugin = SynchronizerUtils.getSynchronizerPlugin();
			if (plugin.needsLicense()
					&& plugin.getLicenseUrl() != null
					&& !plugin.checkLicense())
				this.setSouthComponent(new JButton(
						new ActionGetSerial(
								SynchronizerUtils.getSynchronizerPlugin().getLicenseUrl(),
								22,
								22)));
		} catch (SynchronizerLicenseException exc) {
			
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible && Synchronizing.isSynchronizing())
			return;
		
		super.setVisible(visible);
	}
	
	public class SynchronizerDialogWorker extends SynchronizerWorker {
		
		public SynchronizerDialogWorker() {
			super(false, new SynchronizerProgressMessageListener() {
				
				@Override
				public void showMessage(ProgressMessage message, String content) {
					SynchronizerDialog.this.appendToProgressStatus(content
							+ "\n");
				}
				
			});
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			SynchronizerDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			return super.doInBackground();
		}
		
		@Override
		protected void done() {
			super.done();
			SynchronizerDialog.this.setCursor(null);
			SynchronizerDialog.this.dispose();
		}
		
	}
	
}
