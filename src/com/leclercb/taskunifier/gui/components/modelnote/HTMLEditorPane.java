package com.leclercb.taskunifier.gui.components.modelnote;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public abstract class HTMLEditorPane extends JPanel {
	
	private JXEditorPane htmlNote;
	private JTextArea textNote;
	private Action editAction;
	
	public HTMLEditorPane(String text, boolean canEdit) {
		this.initialize(text, canEdit);
	}
	
	public abstract void textChanged(String text);
	
	public String getText() {
		return this.textNote.getText();
	}
	
	public void setText(String text, boolean canEdit) {
		this.htmlNote.setText(this.convertTextNoteToHtml(text));
		this.htmlNote.setEnabled(canEdit);
		this.editAction.setEnabled(canEdit);
		
		this.textNote.setText(text);
		this.textNote.setCaretPosition(this.textNote.getText().length());
		
		((CardLayout) this.getLayout()).first(this);
	}
	
	public boolean edit() {
		if (!this.htmlNote.isEnabled())
			return false;
		
		((CardLayout) this.getLayout()).last(this);
		this.textNote.requestFocus();
		return true;
	}
	
	public void view() {
		((CardLayout) this.getLayout()).first(this);
		this.htmlNote.setText(this.convertTextNoteToHtml(this.getText()));
	}
	
	private void initialize(String text, boolean canEdit) {
		this.setLayout(new CardLayout());
		
		JToolBar toolBar = null;
		
		this.htmlNote = new JXEditorPane();
		this.htmlNote.setEditable(false);
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setFont(UIManager.getFont("Label.font"));
		
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
				HTMLEditorPane.this.edit();
			}
			
		};
		
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
		
		this.textNote.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				HTMLEditorPane.this.textChanged(HTMLEditorPane.this.getText());
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
				HTMLEditorPane.this.view();
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
		
		this.setText(text, canEdit);
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
	
}
