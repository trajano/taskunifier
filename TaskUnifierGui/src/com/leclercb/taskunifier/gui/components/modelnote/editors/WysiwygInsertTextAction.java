package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit.HTMLTextAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WysiwygInsertTextAction extends HTMLTextAction {
	
	private JEditorPane editor;
	private String text;
	
	public WysiwygInsertTextAction(
			JEditorPane editor,
			String icon,
			String description,
			String text) {
		super(description);
		
		CheckUtils.isNotNull(editor);
		CheckUtils.isNotNull(icon);
		
		this.editor = editor;
		
		this.setText(text);
		
		this.putValue(SMALL_ICON, ImageUtils.getResourceImage(icon, 16, 16));
		this.putValue(SHORT_DESCRIPTION, description);
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		CheckUtils.isNotNull(text);
		this.text = text;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		HTMLDocument document = this.getHTMLDocument(this.editor);
		int offset = this.editor.getCaretPosition();
		
		try {
			document.insertString(offset, this.text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		this.editor.requestFocus();
	}
	
}
