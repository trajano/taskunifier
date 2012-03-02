package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.HTMLTextAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WysiwygInsertHTMLTextAction extends HTMLTextAction {
	
	private JEditorPane editor;
	private String html;
	private HTML.Tag tag;
	
	public WysiwygInsertHTMLTextAction(
			JEditorPane editor,
			String icon,
			String description,
			String html,
			HTML.Tag tag) {
		super(description);
		
		CheckUtils.isNotNull(editor);
		CheckUtils.isNotNull(icon);
		
		this.editor = editor;
		
		this.setHtml(html);
		this.setTag(tag);
		
		this.putValue(SMALL_ICON, ImageUtils.getResourceImage(icon, 16, 16));
		this.putValue(SHORT_DESCRIPTION, description);
	}
	
	public String getHtml() {
		return this.html;
	}
	
	public void setHtml(String html) {
		CheckUtils.isNotNull(html);
		this.html = html;
	}
	
	public HTML.Tag getTag() {
		return this.tag;
	}
	
	public void setTag(HTML.Tag tag) {
		CheckUtils.isNotNull(tag);
		this.tag = tag;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		HTMLDocument document = this.getHTMLDocument(this.editor);
		HTMLEditorKit editorKit = this.getHTMLEditorKit(this.editor);
		int offset = this.editor.getCaretPosition();
		
		try {
			document.insertString(offset, " ", null);
			editorKit.insertHTML(document, offset, this.html, 0, 0, this.tag);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.editor.requestFocus();
	}
	
}
