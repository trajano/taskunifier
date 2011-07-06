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

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.View;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ActionAddNote extends AbstractAction {
	
	public ActionAddNote() {
		this(32, 32);
	}
	
	public ActionAddNote(int width, int height) {
		super(
				Translations.getString("action.add_note"),
				Images.getResourceImage("note.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_note"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionAddNote.addNote(null);
	}
	
	public static Note addNote(String title) {
		MainFrame.getInstance().setSelectedView(View.NOTES);
		
		Folder folder = null;
		TaskSearcher searcher = MainFrame.getInstance().getSearcherView().getSelectedTaskSearcher();
		if (searcher != null && searcher.getType() == TaskSearcherType.FOLDER) {
			List<TaskFilterElement> elements = searcher.getFilter().getElements();
			for (TaskFilterElement e : elements) {
				if (e.getColumn() == TaskColumn.FOLDER) {
					folder = (Folder) e.getValue();
					break;
				}
			}
		}
		
		Note note = NoteFactory.getInstance().create("");
		
		if (title != null)
			note.setTitle(title);
		
		if (folder != null)
			note.setFolder(folder);
		
		MainFrame.getInstance().getNoteView().refreshNotes();
		MainFrame.getInstance().getNoteView().setSelectedNoteAndStartEdit(note);
		
		return note;
	}
	
}
