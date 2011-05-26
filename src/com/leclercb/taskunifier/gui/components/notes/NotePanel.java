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
package com.leclercb.taskunifier.gui.components.notes;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JTable.PrintMode;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.notes.table.NoteTable;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class NotePanel extends JPanel implements NoteView {
	
	private ModelSelectionChangeSupport noteSelectionChangeSupport;
	
	private NoteTable noteTable;
	
	public NotePanel() {
		this.noteSelectionChangeSupport = new ModelSelectionChangeSupport(this);
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.noteTable = new NoteTable();
		this.noteTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						NotePanel.this.noteSelectionChangeSupport.fireModelSelectionChange(NotePanel.this.getSelectedNotes());
					}
					
				});
		
		this.add(
				ComponentFactory.createJScrollPane(this.noteTable, false),
				BorderLayout.CENTER);
	}
	
	@Override
	public Note[] getSelectedNotes() {
		return this.noteTable.getSelectedNotes();
	}
	
	@Override
	public void setSelectedNoteAndStartEdit(Note note) {
		this.noteTable.setSelectedNoteAndStartEdit(note);
	}
	
	@Override
	public void setSelectedNotes(Note[] notes) {
		this.noteTable.setSelectedNotes(notes);
	}
	
	@Override
	public void refreshNotes() {
		this.noteTable.refreshNotes();
	}
	
	@Override
	public String getTitleFilter() {
		return this.noteTable.getTitleFilter();
	}
	
	@Override
	public void setTitleFilter(String titleFilter) {
		this.noteTable.setTitleFilter(titleFilter);
	}
	
	@Override
	public void printNotes() throws HeadlessException, PrinterException {
		this.noteTable.print(
				PrintMode.FIT_WIDTH,
				new MessageFormat(Constants.TITLE + " - Notes"),
				new MessageFormat(this.noteTable.getRowCount()
						+ " notes | Page - {0}"),
				true,
				null,
				true);
	}
	
	@Override
	public void addModelSelectionChangeListener(ModelSelectionListener listener) {
		this.noteSelectionChangeSupport.addModelSelectionChangeListener(listener);
	}
	
	@Override
	public void removeModelSelectionChangeListener(
			ModelSelectionListener listener) {
		this.noteSelectionChangeSupport.removeModelSelectionChangeListener(listener);
	}
	
}
