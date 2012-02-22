package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit.StyledTextAction;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WysiwygInsertHTMLLinkAction extends StyledTextAction {
	
	private String link;
	private String label;
	
	public WysiwygInsertHTMLLinkAction(
			String icon,
			String description) {
		super(description);
		
		this.setLink("");
		this.setLabel("");
		
		this.putValue(SMALL_ICON, ImageUtils.getResourceImage(icon, 16, 16));
		this.putValue(SHORT_DESCRIPTION, description);
	}
	
	public String getLink() {
		return this.link;
	}
	
	public void setLink(String link) {
		CheckUtils.isNotNull(link);
		this.link = link;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		CheckUtils.isNotNull(label);
		this.label = label;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		JEditorPane editor = getEditor(evt);
		
		if (editor == null)
			return;
		
		HTMLDocument document = (HTMLDocument) editor.getDocument();
		HTMLEditorKit ekit = (HTMLEditorKit) editor.getEditorKit();
		int offset = editor.getSelectionStart();
		
		try {
			ekit.insertHTML(document, offset, "<a href=\"" + link + "\">" + label + "</a>", 0, 0, HTML.Tag.A);
		} catch (BadLocationException ble) {
			throw new Error(ble);
		} catch (IOException ioe) {
			throw new Error(ioe);
		}
	}
	
}
