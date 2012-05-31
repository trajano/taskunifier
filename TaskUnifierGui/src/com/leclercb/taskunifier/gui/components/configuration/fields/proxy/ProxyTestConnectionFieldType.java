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
package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;
import com.leclercb.taskunifier.gui.swing.TUWorker;
import com.leclercb.taskunifier.gui.swing.TUWorkerDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.HttpUtils;

public class ProxyTestConnectionFieldType extends ConfigurationFieldType.Button {
	
	public ProxyTestConnectionFieldType(ConfigurationPanel panel) {
		super(
				Translations.getString("configuration.proxy.test_connection"),
				new TestConnection(panel));
	}
	
	private static class TestConnection implements ActionListener {
		
		private ConfigurationPanel panel;
		
		public TestConnection(ConfigurationPanel panel) {
			CheckUtils.isNotNull(panel);
			this.panel = panel;
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
			this.panel.saveAndApplyConfig();
			
			TUWorkerDialog<Void> dialog = new TUWorkerDialog<Void>(
					FrameUtils.getCurrentFrame(),
					Translations.getString("configuration.proxy.test_connection"));
			
			ProgressMonitor monitor = new ProgressMonitor();
			monitor.addListChangeListener(dialog);
			
			dialog.setWorker(new TUWorker<Void>(monitor) {
				
				@Override
				protected Void longTask() throws Exception {
					this.publish(new DefaultProgressMessage(
							Translations.getString("configuration.proxy.test_connection")));
					
					try {
						HttpUtils.getHttpGetResponse(new URI(
								Constants.TEST_CONNECTION));
						TestConnection.this.showResult(true);
					} catch (Throwable t) {
						TestConnection.this.showResult(false);
					}
					
					return null;
				}
				
			});
			
			dialog.setVisible(true);
		}
		
		private void showResult(final boolean result) {
			TUSwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					if (result) {
						JOptionPane.showMessageDialog(
								FrameUtils.getCurrentFrame(),
								Translations.getString("configuration.proxy.test_connection.success"),
								Translations.getString("general.information"),
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						ErrorInfo info = new ErrorInfo(
								Translations.getString("general.error"),
								Translations.getString("configuration.proxy.test_connection.failed"),
								null,
								"GUI",
								null,
								Level.INFO,
								null);
						
						JXErrorPane.showDialog(
								FrameUtils.getCurrentFrame(),
								info);
					}
				}
				
			});
		}
		
	}
	
}
