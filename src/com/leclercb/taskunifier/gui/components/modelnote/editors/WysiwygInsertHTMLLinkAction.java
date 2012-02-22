package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.text.StyledEditorKit.StyledTextAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.modelnote.converters.HTML2Text;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WysiwygInsertHTMLLinkAction extends StyledTextAction {
	
	private String link;
	private String label;
	
	public WysiwygInsertHTMLLinkAction(String icon, String description) {
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
		return this.label;
	}
	
	public void setLabel(String label) {
		CheckUtils.isNotNull(label);
		this.label = label;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		JEditorPane editor = this.getEditor(evt);
		
		String text = HTML2Text.convert(editor.getText());
		text += "<a href=\"" + this.link + "\">" + this.label + "</a>";
		editor.setText(Text2HTML.convert(text));
	}
	
}
