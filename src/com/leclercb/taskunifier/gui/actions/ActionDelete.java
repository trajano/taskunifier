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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.View;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionDelete extends AbstractAction {
	
	public ActionDelete() {
		this(32, 32);
	}
	
	public ActionDelete(int width, int height) {
		super(Translations.getString("action.delete"), Images.getResourceImage(
				"remove.png",
				width,
				height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.delete"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_D,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionDelete.delete();
	}
	
	public static void delete() {
		if (MainFrame.getInstance().getSelectedView() == View.TASKS) {
			Task[] tasks = MainFrame.getInstance().getTaskView().getSelectedTasks();
			
			boolean hasSubTasks = false;
			
			for (Task task : tasks) {
				if (task.getChildren().length != 0) {
					hasSubTasks = true;
					break;
				}
			}
			
			int deleteSubTasks = hasSubTasks ? askDeleteSubTasks() : JOptionPane.NO_OPTION;
			
			if (deleteSubTasks == JOptionPane.CANCEL_OPTION)
				return;
			
			Synchronizing.setSynchronizing(true);
			
			for (Task task : tasks) {
				if (task.getModelStatus().isEndUserStatus()) {
					if (deleteSubTasks == JOptionPane.YES_OPTION) {
						Task[] children = task.getChildren();
						for (Task child : children) {
							if (child.getModelStatus().isEndUserStatus()) {
								TaskFactory.getInstance().markToDelete(child);
							}
						}
					}
					
					TaskFactory.getInstance().markToDelete(task);
				}
			}
			
			Synchronizing.setSynchronizing(false);
		} else {
			Note[] notes = MainFrame.getInstance().getNoteView().getSelectedNotes();
			
			for (Note note : notes) {
				if (note.getModelStatus().isEndUserStatus()) {
					NoteFactory.getInstance().markToDelete(note);
				}
			}
		}
	}
	
	private static int askDeleteSubTasks() {
		String[] options = new String[] {
				Translations.getString("general.yes"),
				Translations.getString("general.no"),
				Translations.getString("general.cancel") };
		
		int result = JOptionPane.showOptionDialog(
				MainFrame.getInstance().getFrame(),
				Translations.getString("action.delete.delete_subtasks"),
				Translations.getString("general.question"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		
		return result;
	}
	
}
