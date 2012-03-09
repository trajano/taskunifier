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
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.commons.undoableedit.ModelDeleteUndoableEdit;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionDelete extends AbstractViewAction {
	
	public ActionDelete() {
		this(32, 32);
	}
	
	public ActionDelete(int width, int height) {
		super(
				Translations.getString("action.delete"),
				ImageUtils.getResourceImage("remove.png", width, height),
				ViewType.TASKS,
				ViewType.NOTES,
				ViewType.CALENDAR);
		
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
		ViewType viewType = ViewUtils.getCurrentViewType();
		
		if (viewType == ViewType.TASKS || viewType == ViewType.CALENDAR) {
			Task[] tasks = ViewUtils.getSelectedTasks();
			
			if (tasks.length > 1) {
				int deleteTasks = askDeleteTasks(tasks.length);
				
				if (deleteTasks == JOptionPane.NO_OPTION)
					return;
			}
			
			boolean hasSubTasks = false;
			
			for (Task task : tasks) {
				for (Task subtask : task.getAllChildren()) {
					if (subtask.getModelStatus().isEndUserStatus()) {
						hasSubTasks = true;
						break;
					}
				}
			}
			
			int deleteSubTasks = hasSubTasks ? askDeleteSubTasks() : JOptionPane.NO_OPTION;
			
			if (deleteSubTasks == JOptionPane.CANCEL_OPTION)
				return;
			
			Synchronizing.setSynchronizing(true);
			
			try {
				Constants.UNDO_SUPPORT.beginUpdate();
				
				for (Task task : tasks) {
					if (task.getModelStatus().isEndUserStatus()) {
						if (deleteSubTasks == JOptionPane.YES_OPTION) {
							List<Task> children = task.getAllChildren();
							for (Task child : children) {
								if (child.getModelStatus().isEndUserStatus()) {
									TaskFactory.getInstance().markToDelete(
											child);
									Constants.UNDO_SUPPORT.postEdit(new ModelDeleteUndoableEdit(
											child));
								}
							}
						}
						
						TaskFactory.getInstance().markToDelete(task);
						Constants.UNDO_SUPPORT.postEdit(new ModelDeleteUndoableEdit(
								task));
					}
				}
				
				Constants.UNDO_SUPPORT.endUpdate();
			} finally {
				Synchronizing.setSynchronizing(false);
			}
		} else if (viewType == ViewType.NOTES) {
			Note[] notes = ViewUtils.getSelectedNotes();
			
			if (notes.length > 1) {
				int deleteNotes = askDeleteNotes(notes.length);
				
				if (deleteNotes == JOptionPane.NO_OPTION)
					return;
			}
			
			Constants.UNDO_SUPPORT.beginUpdate();
			
			for (Note note : notes) {
				if (note.getModelStatus().isEndUserStatus()) {
					NoteFactory.getInstance().markToDelete(note);
					Constants.UNDO_SUPPORT.postEdit(new ModelDeleteUndoableEdit(
							note));
				}
			}
			
			Constants.UNDO_SUPPORT.endUpdate();
		}
	}
	
	private static int askDeleteNotes(int number) {
		String[] options = new String[] {
				Translations.getString("general.yes"),
				Translations.getString("general.no") };
		
		int result = JOptionPane.showOptionDialog(
				MainFrame.getInstance().getFrame(),
				Translations.getString("action.delete.delete_notes", number),
				Translations.getString("general.question"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		
		return result;
	}
	
	private static int askDeleteTasks(int number) {
		String[] options = new String[] {
				Translations.getString("general.yes"),
				Translations.getString("general.no") };
		
		int result = JOptionPane.showOptionDialog(
				MainFrame.getInstance().getFrame(),
				Translations.getString("action.delete.delete_tasks", number),
				Translations.getString("general.question"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		
		return result;
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
