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
package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionImportComFile;
import com.leclercb.taskunifier.gui.api.models.beans.ComBean;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;
import com.leclercb.taskunifier.gui.threads.communicator.progress.CommunicatorDefaultProgressMessage;
import com.leclercb.taskunifier.gui.translations.Translations;

public class CommunicatorClient extends Thread {
	
	private Socket socket;
	private BufferedReader reader;
	
	public CommunicatorClient(ThreadGroup group, Socket socket) {
		super(group, "CommunicatorClient");
		
		try {
			this.socket = socket;
			this.reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(),
					"UTF-8"));
		} catch (Exception e) {
			this.socket = null;
			this.reader = null;
		}
	}
	
	@Override
	public void run() {
		if (this.socket == null)
			return;
		
		try {
			String data = null;
			StringBuffer buffer = new StringBuffer();
			while ((data = this.reader.readLine()) != null) {
				buffer.append(data.trim() + "\n");
				
				if (data.trim().endsWith("</com>")) {
					this.handleMessage(buffer.toString());
					buffer = new StringBuffer();
				}
			}
		} catch (IOException e) {
			
		} finally {
			this.socket = null;
			this.reader = null;
		}
	}
	
	private void handleMessage(final String message) {
		TUSwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					ComBean bean = ComBean.decodeFromXML(IOUtils.toInputStream(
							message,
							"UTF-8"));
					
					ActionImportComFile.importComBean(bean);
					
					return;
				} catch (Exception e) {
					
				}
				
				Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(
						Translations.getString("error.unknown_message_format")));
				
				GuiLogger.getLogger().warning("Unknown message format");
			}
			
		});
	}
	
}
