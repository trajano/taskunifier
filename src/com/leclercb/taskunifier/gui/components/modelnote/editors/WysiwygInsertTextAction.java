package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.text.StyledEditorKit.StyledTextAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.modelnote.converters.HTML2Text;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WysiwygInsertTextAction extends StyledTextAction {
	
	private JEditorPane editorPane;
	private String text;
	
	public WysiwygInsertTextAction(
			JEditorPane editorPane,
			String icon,
			String description,
			String text) {
		super(description);
		
		CheckUtils.isNotNull(editorPane);
		CheckUtils.isNotNull(text);
		
		this.editorPane = editorPane;
		this.text = text;
		
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
	public void actionPerformed(ActionEvent evt) {
		String text = HTML2Text.convert(this.editorPane.getText());
		text += this.text;
		
		this.editorPane.setText(Text2HTML.convert(text));
	}
	
}
