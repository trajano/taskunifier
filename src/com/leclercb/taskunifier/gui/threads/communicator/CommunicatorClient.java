package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.api.models.beans.ComBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComNoteBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComTaskBean;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.constants.Constants;
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
	
	private void handleMessage(String message) {
		try {
			ComBean bean = ComBean.decodeFromXML(IOUtils.toInputStream(
					message,
					"UTF-8"));
			
			if (bean.getNotes() != null) {
				List<Note> notes = new ArrayList<Note>();
				for (ComNoteBean note : bean.getNotes()) {
					note.setModels();
					notes.add(ActionAddNote.addNote(note, true, false));
				}
				
				Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(
						Translations.getString(
								"communicator.message.add_note",
								notes.size(),
								bean.getApplicationName())));
				
				ViewType.getNoteView().getNoteTableView().setSelectedNotes(
						notes.toArray(new Note[0]));
			}
			
			if (bean.getTasks() != null) {
				List<Task> tasks = new ArrayList<Task>();
				for (ComTaskBean task : bean.getTasks()) {
					task.setModels();
					tasks.add(ActionAddTask.addTask(task, true, false));
				}
				
				Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(
						Translations.getString(
								"communicator.message.add_task",
								tasks.size(),
								bean.getApplicationName())));
				
				ViewType.getTaskView().getTaskTableView().setSelectedTasks(
						tasks.toArray(new Task[0]));
			}
			
			return;
		} catch (Exception e) {
			
		}
		
		Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(
				Translations.getString("error.unknown_message_format")));
		
		GuiLogger.getLogger().warning("Unknown message format");
	}
	
}
