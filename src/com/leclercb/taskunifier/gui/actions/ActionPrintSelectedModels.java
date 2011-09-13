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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;

import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.NoteUtils;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ActionPrintSelectedModels extends AbstractAction {
	
	public ActionPrintSelectedModels() {
		this(32, 32);
	}
	
	public ActionPrintSelectedModels(int width, int height) {
		super(
				Translations.getString("action.print_selection"),
				Images.getResourceImage("print.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.print_selection"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionPrintSelectedModels.print();
	}
	
	public static void print() {
		try {
			String text = null;
			ViewType viewType = MainFrame.getInstance().getSelectedViewType();
			
			if (viewType == ViewType.NOTES) {
				List<NoteColumn> columns = new ArrayList<NoteColumn>(
						Arrays.asList(NoteColumn.getVisibleNoteColumns()));
				
				if (!columns.contains(NoteColumn.NOTE)) {
					columns.add(NoteColumn.NOTE);
				}
				
				text = NoteUtils.toText(
						ViewType.getSelectedNotes(),
						columns.toArray(new NoteColumn[0]),
						true);
			} else if (viewType == ViewType.TASKS) {
				List<TaskColumn> columns = new ArrayList<TaskColumn>(
						Arrays.asList(TaskColumn.getVisibleTaskColumns()));
				columns.remove(TaskColumn.MODEL_EDIT);
				columns.remove(TaskColumn.SHOW_CHILDREN);
				
				if (!columns.contains(TaskColumn.NOTE)) {
					columns.add(TaskColumn.NOTE);
				}
				
				text = TaskUtils.toText(
						ViewType.getSelectedTasks(),
						columns.toArray(new TaskColumn[0]),
						true);
			}
			
			JXEditorPane editor = new JXEditorPane();
			editor.setEditable(false);
			editor.setContentType("text/html");
			editor.setText(text);
			
			editor.print();
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.print"),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
		}
	}
	
}
