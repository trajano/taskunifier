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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.models.beans.ComBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComNoteBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComQuickTaskBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComTaskBean;
import com.leclercb.taskunifier.gui.components.import_data.ImportComFileDialog;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.threads.communicator.progress.CommunicatorDefaultProgressMessage;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionImportComFile extends AbstractAction {
	
	public ActionImportComFile() {
		this(32, 32);
	}
	
	public ActionImportComFile(int width, int height) {
		super(
				Translations.getString("action.import_com_file"),
				ImageUtils.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.import_com_file"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionImportComFile.importComFile();
	}
	
	public static void importComFile() {
		ImportComFileDialog.getInstance().setVisible(true);
	}
	
	public static void importComBean(ComBean bean) {
		try {
			if (bean.getArguments() != null) {
				for (String argument : bean.getArguments()) {
					if (argument == null)
						continue;
					
					File file = new File(argument);
					
					try {
						if (!file.exists() || !file.isFile())
							continue;
						
						String ext = FileUtils.getExtention(argument);
						
						if ("tue".equals(ext)) {
							FileInputStream input = new FileInputStream(file);
							ComBean b = ComBean.decodeFromXML(input);
							input.close();
							
							importComBean(b);
						}
					} catch (Throwable t) {
						ErrorInfo info = new ErrorInfo(
								Translations.getString("general.error"),
								Translations.getString(
										"error.cannot_open_file",
										file.getAbsolutePath()),
								null,
								null,
								t,
								null,
								null);
						
						JXErrorPane.showDialog(
								MainFrame.getInstance().getFrame(),
								info);
					}
				}
			}
			
			if (bean.getNotes() != null) {
				List<Note> notes = new ArrayList<Note>();
				for (ComNoteBean note : bean.getNotes()) {
					note.loadModels(false);
					notes.add(ActionAddNote.addNote(note, false));
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
					task.loadModels(false);
					tasks.add(ActionAddTask.addTask(task, false));
				}
				
				Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(
						Translations.getString(
								"communicator.message.add_task",
								tasks.size(),
								bean.getApplicationName())));
				
				ViewType.getTaskView().getTaskTableView().setSelectedTasks(
						tasks.toArray(new Task[0]));
			}
			
			if (bean.getQuickTasks() != null) {
				List<Task> tasks = new ArrayList<Task>();
				for (ComQuickTaskBean quickTask : bean.getQuickTasks()) {
					if (quickTask.getTitle() == null)
						continue;
					
					tasks.add(ActionAddQuickTask.addQuickTask(
							quickTask.getTitle(),
							false));
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
			e.printStackTrace();
		}
		
		Constants.PROGRESS_MONITOR.addMessage(new CommunicatorDefaultProgressMessage(
				Translations.getString("error.unknown_message_format")));
		
		GuiLogger.getLogger().warning("Unknown message format");
	}
	
}
