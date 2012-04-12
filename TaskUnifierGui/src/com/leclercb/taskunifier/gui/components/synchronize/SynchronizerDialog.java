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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.taskunifier.gui.actions.ActionGetSerial;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.components.synchronize.progress.SynchronizerProgressMessageListener;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.swing.TUWorkerDialog;
import com.leclercb.taskunifier.gui.translations.Translations;

public class SynchronizerDialog extends TUWorkerDialog<Void> {
	
	private boolean serialNeeded;
	
	public SynchronizerDialog() {
		super(
				FrameUtils.getCurrentFrame(),
				Translations.getString("general.synchronization"));
		
		this.serialNeeded = false;
		
		final SynchronizerDialogWorker worker = new SynchronizerDialogWorker();
		this.setWorker(worker);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				worker.stop();
			}
			
		});
	}
	
	public void add(SynchronizerGuiPlugin plugin, SynchronizerWorker.Type type) {
		SynchronizerDialogWorker worker = (SynchronizerDialogWorker) this.getWorker();
		worker.add(plugin, type);
		
		try {
			if (!this.serialNeeded
					&& plugin.needsLicense()
					&& plugin.getLicenseUrl() != null
					&& !plugin.checkLicense()) {
				this.serialNeeded = true;
				this.setSouthComponent(new JButton(new ActionGetSerial(
						22,
						22,
						plugin.getLicenseUrl())));
			}
		} catch (SynchronizerLicenseException exc) {
			
		}
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
		
	}
	
}
