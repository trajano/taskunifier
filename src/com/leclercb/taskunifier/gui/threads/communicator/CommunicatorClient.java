package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.api.models.beans.ComNoteBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComTaskBean;

public class CommunicatorClient extends Thread {
	
	private Socket socket;
	private BufferedReader reader;
	
	public CommunicatorClient(Socket socket) {
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
				
				if (data.trim().endsWith("</task>")) {
					this.handleTaskMessage(buffer.toString());
					buffer = new StringBuffer();
				}
				
				if (data.trim().endsWith("</note>")) {
					this.handleNoteMessage(buffer.toString());
					buffer = new StringBuffer();
				}
			}
		} catch (IOException e) {
			
		} finally {
			this.socket = null;
			this.reader = null;
		}
	}
	
	private void handleTaskMessage(String message) {
		try {
			ComTaskBean bean = ComTaskBean.decodeFromXML(IOUtils.toInputStream(
					message,
					"UTF-8"));
			ActionAddTask.addTask(bean, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleNoteMessage(String message) {
		try {
			ComNoteBean bean = ComNoteBean.decodeFromXML(IOUtils.toInputStream(
					message,
					"UTF-8"));
			ActionAddNote.addNote(bean, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
