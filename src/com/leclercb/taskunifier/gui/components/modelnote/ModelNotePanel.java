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
package com.leclercb.taskunifier.gui.components.modelnote;

import java.awt.CardLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelNote;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class ModelNotePanel extends JPanel implements ModelSelectionListener, PropertyChangeListener {
	
	private JXEditorPane htmlNote;
	private JTextArea textNote;
	
	private ModelNote previousSelectedModel;
	
	public ModelNotePanel() {
		this.previousSelectedModel = null;
		this.initialize();
	}
	
	public String getModelNote() {
		return this.textNote.getText();
	}
	
	private void initialize() {
		this.setLayout(new CardLayout());
		
		this.htmlNote = new JXEditorPane();
		
		this.htmlNote.setEnabled(false);
		this.htmlNote.setEditable(false);
		// this.htmlNote.setEditorKit(new StyledEditorKit());
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setText(Translations.getString("error.select_one_row"));
		this.htmlNote.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ModelNotePanel.this.htmlNote.isEnabled()) {
					((CardLayout) ModelNotePanel.this.getLayout()).last(ModelNotePanel.this);
					ModelNotePanel.this.textNote.setCaretPosition(0);
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
				if (ModelNotePanel.this.previousSelectedModel != null) {
					if (!EqualsUtils.equals(
							ModelNotePanel.this.previousSelectedModel.getNote(),
							ModelNotePanel.this.getModelNote())) {
						ModelNotePanel.this.previousSelectedModel.setNote(ModelNotePanel.this.getModelNote());
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
		
		((CardLayout) this.getLayout()).first(ModelNotePanel.this);
	}
	
	@Override
	public synchronized void modelSelectionChange(
			ModelSelectionChangeEvent event) {
		if (this.previousSelectedModel != null) {
			if (!EqualsUtils.equals(
					this.previousSelectedModel.getNote(),
					this.getModelNote()))
				this.previousSelectedModel.setNote(this.getModelNote());
			
			this.previousSelectedModel.removePropertyChangeListener(this);
		}
		
		Model[] models = event.getSelectedModels();
		
		if (models.length != 1 || !(models[0] instanceof ModelNote)) {
			this.previousSelectedModel = null;
			
			this.htmlNote.setText(Translations.getString("error.select_one_row"));
			this.textNote.setText(null);
			
			this.htmlNote.setCaretPosition(0);
			this.textNote.setCaretPosition(0);
			
			this.htmlNote.setEnabled(false);
		} else {
			this.previousSelectedModel = (ModelNote) models[0];
			this.previousSelectedModel.addPropertyChangeListener(this);
			
			String note = (this.previousSelectedModel.getNote() == null ? "" : this.previousSelectedModel.getNote());
			
			this.htmlNote.setText(this.convertTextNoteToHtml(note));
			this.textNote.setText(note);
			
			this.htmlNote.setCaretPosition(0);
			this.textNote.setCaretPosition(0);
			
			this.htmlNote.setEnabled(true);
		}
		
		((CardLayout) this.getLayout()).first(ModelNotePanel.this);
	}
	
	private String convertTextNoteToHtml(String note) {
		if (note.length() == 0)
			return " ";
		
		StringBuffer buffer = new StringBuffer();
		note = note.replace("\n", "\n ");
		String[] lines = note.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			line = line.trim();
			buffer.append(line);
			if (line.startsWith("<"))
				if (i + 1 < lines.length && lines[i + 1].trim().startsWith("<"))
					continue;
			
			buffer.append("<br />");
		}
		
		return buffer.toString();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (EqualsUtils.equals(evt.getPropertyName(), ModelNote.PROP_NOTE)) {
			String note = (this.previousSelectedModel.getNote() == null ? "" : this.previousSelectedModel.getNote());
			
			this.htmlNote.setText(this.convertTextNoteToHtml(note));
			this.textNote.setText(note);
			
			this.htmlNote.setCaretPosition(0);
			this.textNote.setCaretPosition(0);
		}
	}
	
}
