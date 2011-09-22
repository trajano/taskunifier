package com.leclercb.taskunifier.gui.components.modelnote;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.components.modelnote.converters.HTML2Text;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.swing.JFileDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public abstract class RealHTMLEditorPane extends JPanel {
	
	private UndoSupport undoSupport;
	
	private JXEditorPane htmlNote;
	
	public RealHTMLEditorPane(String text, boolean canEdit) {
		this.initialize(text, canEdit);
	}
	
	public abstract void textChanged(String text);
	
	public String getText() {
		return HTML2Text.convert(this.htmlNote.getText());
	}
	
	public void setText(String text, boolean canEdit, boolean discardAllEdits) {
		this.htmlNote.setText(Text2HTML.convert(text));
		this.htmlNote.setEnabled(canEdit);
		
		if (discardAllEdits) {
			this.htmlNote.setCaretPosition(this.htmlNote.getText().length());
			this.undoSupport.discardAllEdits();
			this.htmlNote.requestFocus();
		}
	}
	
	private void initialize(String text, boolean canEdit) {
		this.setLayout(new BorderLayout());
		
		this.undoSupport = new UndoSupport();
		
		JToolBar toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		toolBar.addSeparator();
		
		toolBar.add(this.undoSupport.getUndoAction());
		toolBar.add(this.undoSupport.getRedoAction());
		
		toolBar.addSeparator();
		
		toolBar.add(new RealHTMLInsertContentAction(
				this.htmlNote,
				"html_b.png",
				"<b>|</b>"));
		
		toolBar.add(new RealHTMLInsertContentAction(
				this.htmlNote,
				"html_i.png",
				"<i>|</i>"));
		
		toolBar.add(new RealHTMLInsertContentAction(
				this.htmlNote,
				"html_ul.png",
				"\n<ul>\n<li>|</li>\n</ul>"));
		
		toolBar.add(new RealHTMLInsertContentAction(
				this.htmlNote,
				"html_ol.png",
				"\n<ol>\n<li>|</li>\n</ol>"));
		
		toolBar.add(new RealHTMLInsertContentAction(
				this.htmlNote,
				"html_li.png",
				"\n<li>|</li>"));
		
		toolBar.add(new RealHTMLInsertContentAction(
				this.htmlNote,
				"html_a.png",
				"<a href=\"\">|</a>") {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileDialog dialog = new JFileDialog(
						true,
						Translations.getString("general.link"));
				dialog.setFile("http://");
				dialog.setVisible(true);
				
				if (dialog.isCancelled()) {
					RealHTMLEditorPane.this.htmlNote.requestFocus();
					return;
				}
				
				String url = dialog.getFile();
				
				try {
					File file = new File(url);
					if (file.exists())
						url = file.toURI().toURL().toExternalForm();
				} catch (Throwable t) {
					
				}
				
				this.setContent("<a href=\"" + url + "\">|</a>");
				super.actionPerformed(event);
			}
			
		});
		
		this.htmlNote = new JXEditorPane();
		this.htmlNote.setEditable(true);
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setFont(UIManager.getFont("Label.font"));
		this.htmlNote.getDocument().addUndoableEditListener(this.undoSupport);
		this.undoSupport.initializeMaps(this.htmlNote);
		
		this.htmlNote.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				RealHTMLEditorPane.this.textChanged(RealHTMLEditorPane.this.getText());
			}
			
		});
		
		this.htmlNote.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						BrowserUtils.openDefaultBrowser(evt.getURL().toExternalForm());
					} catch (Exception exc) {
						
					}
				}
			}
			
		});
		
		this.add(toolBar, BorderLayout.NORTH);
		this.add(
				ComponentFactory.createJScrollPane(this.htmlNote, false),
				BorderLayout.CENTER);
		
		this.setText(text, canEdit, true);
	}
	
}