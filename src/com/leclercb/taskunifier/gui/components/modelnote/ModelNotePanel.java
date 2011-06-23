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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ModelNotePanel extends JPanel implements ModelSelectionListener, PropertyChangeListener {
	
	private JXEditorPane htmlNote;
	private JTextArea textNote;
	private Action editAction;
	
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
		
		JToolBar toolBar = null;
		
		this.htmlNote = new JXEditorPane();
		
		this.htmlNote.setEnabled(false);
		this.htmlNote.setEditable(false);
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setFont(UIManager.getFont("Label.font"));
		this.htmlNote.setText(Translations.getString("error.select_one_row"));
		
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
		
		toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		this.editAction = new AbstractAction("", Images.getResourceImage(
				"edit.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (ModelNotePanel.this.htmlNote.isEnabled()) {
					((CardLayout) ModelNotePanel.this.getLayout()).last(ModelNotePanel.this);
				}
			}
			
		};
		
		this.editAction.setEnabled(false);
		
		toolBar.add(this.editAction);
		
		JPanel htmlPanel = new JPanel(new BorderLayout());
		htmlPanel.add(toolBar, BorderLayout.NORTH);
		htmlPanel.add(
				ComponentFactory.createJScrollPane(this.htmlNote, false),
				BorderLayout.CENTER);
		
		this.textNote = new JTextArea();
		
		this.textNote.setLineWrap(true);
		this.textNote.setWrapStyleWord(true);
		this.textNote.setBorder(BorderFactory.createEmptyBorder());
		this.textNote.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				this.updateNote();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				this.updateNote();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				this.updateNote();
			}
			
			private void updateNote() {
				if (ModelNotePanel.this.previousSelectedModel != null) {
					ModelNotePanel.this.previousSelectedModel.setNote(ModelNotePanel.this.getModelNote());
				}
			}
			
		});
		
		toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		toolBar.add(new AbstractAction("", Images.getResourceImage(
				"previous.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				((CardLayout) ModelNotePanel.this.getLayout()).first(ModelNotePanel.this);
				ModelNotePanel.this.htmlNote.setText(ModelNotePanel.this.convertTextNoteToHtml(ModelNotePanel.this.getModelNote()));
			}
			
		});
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_b.png",
				"<b>|</b>"));
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_i.png",
				"<i>|</i>"));
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_ul.png",
				"\n<ul>\n<li>|</li>\n</ul>"));
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_ol.png",
				"\n<ol>\n<li>|</li>\n</ol>"));
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_li.png",
				"\n<li>|</li>"));
		
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(toolBar, BorderLayout.NORTH);
		textPanel.add(
				ComponentFactory.createJScrollPane(this.textNote, false),
				BorderLayout.CENTER);
		
		this.add(htmlPanel, "" + 0);
		this.add(textPanel, "" + 1);
		
		((CardLayout) this.getLayout()).first(ModelNotePanel.this);
	}
	
	@Override
	public synchronized void modelSelectionChange(
			ModelSelectionChangeEvent event) {
		Model[] models = event.getSelectedModels();
		
		if (models.length == 1 && models[0] instanceof ModelNote) {
			if (EqualsUtils.equals(models[0], this.previousSelectedModel))
				return;
		}
		
		if (this.previousSelectedModel != null) {
			this.previousSelectedModel.removePropertyChangeListener(this);
		}
		
		if (models.length != 1 || !(models[0] instanceof ModelNote)) {
			this.previousSelectedModel = null;
			
			this.htmlNote.setText(Translations.getString("error.select_one_row"));
			this.textNote.setText(null);
			this.textNote.setCaretPosition(0);
			
			this.htmlNote.setEnabled(false);
			this.editAction.setEnabled(false);
		} else {
			this.previousSelectedModel = (ModelNote) models[0];
			this.previousSelectedModel.addPropertyChangeListener(
					ModelNote.PROP_NOTE,
					this);
			
			String note = this.previousSelectedModel.getNote();
			
			this.htmlNote.setText(this.convertTextNoteToHtml(note));
			this.textNote.setText(note);
			this.textNote.setCaretPosition(0);
			
			this.htmlNote.setEnabled(true);
			this.editAction.setEnabled(true);
		}
		
		((CardLayout) this.getLayout()).first(ModelNotePanel.this);
	}
	
	private String convertTextNoteToHtml(String note) {
		if (note == null || note.length() == 0)
			return " ";
		
		note = this.convertNlToBr(note);
		note = this.convertToHtmlUrl(note);
		
		return note;
	}
	
	private String convertToHtmlUrl(String note) {
		StringBuffer buffer = new StringBuffer(note);
		
		Pattern p = Pattern.compile("(href=['\"]{1})?((https?|ftp|file):((//)|(\\\\))+[\\w\\d:#@%/;$~_?\\+\\-=\\\\.&]*)");
		Matcher m = null;
		int position = 0;
		
		while (true) {
			m = p.matcher(buffer.toString());
			
			if (!m.find(position))
				break;
			
			position = m.end();
			String firstGroup = m.group(1);
			
			if (firstGroup == null)
				firstGroup = "";
			
			if (firstGroup.contains("href"))
				continue;
			
			String url = firstGroup
					+ "<a href=\""
					+ m.group(2)
					+ "\">"
					+ m.group(2)
					+ "</a>";
			
			buffer.replace(m.start(), m.end(), url);
			
			position = m.start() + url.length() - 1;
		}
		
		return buffer.toString();
	}
	
	private String convertNlToBr(String note) {
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
		String note = (String) evt.getNewValue();
		
		if (EqualsUtils.equals(this.textNote.getText(), note))
			return;
		
		this.htmlNote.setText(this.convertTextNoteToHtml(note));
		
		this.textNote.setText(note);
		this.textNote.setCaretPosition(0);
	}
	
}
