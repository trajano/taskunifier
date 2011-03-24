/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.tasknote;

import java.awt.CardLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.StyledEditorKit;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TaskNotePanel extends JPanel implements TaskSelectionListener {
	
	private JEditorPane htmlNote;
	private JTextArea textNote;
	
	private Task previousSelectedTask;
	
	public TaskNotePanel() {
		this.previousSelectedTask = null;
		this.initialize();
	}
	
	public String getTaskNote() {
		return this.textNote.getText();
	}
	
	private void initialize() {
		this.setLayout(new CardLayout());
		
		this.htmlNote = new JEditorPane();
		
		this.htmlNote.setEnabled(false);
		this.htmlNote.setEditable(false);
		this.htmlNote.setEditorKit(new StyledEditorKit());
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setText(Translations.getString("error.select_one_task"));
		this.htmlNote.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (TaskNotePanel.this.htmlNote.isEnabled()) {
					((CardLayout) TaskNotePanel.this.getLayout()).last(TaskNotePanel.this);
					TaskNotePanel.this.textNote.setCaretPosition(0);
				}
			}
			
		});
		
		this.htmlNote.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						BrowserUtils.openDefaultBrowser(evt.getURL().toString());
					} catch (Exception exc) {}
				}
			}
			
		});
		
		this.textNote = new JTextArea();
		
		this.textNote.setLineWrap(true);
		this.textNote.setWrapStyleWord(true);
		this.textNote.setBorder(BorderFactory.createEmptyBorder());
		this.textNote.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (TaskNotePanel.this.previousSelectedTask != null) {
					if (!EqualsUtils.equals(
							TaskNotePanel.this.previousSelectedTask.getNote(),
							TaskNotePanel.this.getTaskNote())) {
						TaskNotePanel.this.previousSelectedTask.setNote(TaskNotePanel.this.getTaskNote());
					}
				}
			}
			
		});
		
		this.add(
				ComponentFactory.createJScrollPane(this.htmlNote, false),
				"" + 0);
		this.add(
				ComponentFactory.createJScrollPane(this.textNote, false),
				"" + 1);
		
		((CardLayout) this.getLayout()).first(TaskNotePanel.this);
	}
	
	@Override
	public void taskSelectionChange(TaskSelectionChangeEvent event) {
		if (this.previousSelectedTask != null) {
			if (!EqualsUtils.equals(
					this.previousSelectedTask.getNote(),
					this.getTaskNote()))
				this.previousSelectedTask.setNote(this.getTaskNote());
		}
		
		Task[] tasks = event.getSelectedTasks();
		
		if (tasks.length != 1) {
			this.previousSelectedTask = null;
			
			this.htmlNote.setText(Translations.getString("error.select_one_task"));
			this.textNote.setText(null);
			
			this.htmlNote.setCaretPosition(0);
			this.textNote.setCaretPosition(0);
			
			this.htmlNote.setEnabled(false);
		} else {
			this.previousSelectedTask = tasks[0];
			
			this.htmlNote.setText((tasks[0].getNote() == null ? "" : tasks[0].getNote()));
			this.textNote.setText((tasks[0].getNote() == null ? "" : tasks[0].getNote()));
			
			this.htmlNote.setCaretPosition(0);
			this.textNote.setCaretPosition(0);
			
			this.htmlNote.setEnabled(true);
		}
		
		((CardLayout) this.getLayout()).first(TaskNotePanel.this);
	}
	
}
