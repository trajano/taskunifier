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
package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class WysiwygInsertHTMLTextAction extends AbstractAction {
	
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
		HTMLDocument document = (HTMLDocument) this.editor.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit) this.editor.getEditorKit();
		int offset = this.editor.getCaretPosition();
		
		try {
			document.insertString(offset, " ", null);
			editorKit.insertHTML(document, offset, this.html, 0, 0, this.tag);
		} catch (BadLocationException e) {
			GuiLogger.getLogger().log(Level.WARNING, "Wysiwyg action error", e);
		} catch (IOException e) {
			GuiLogger.getLogger().log(Level.WARNING, "Wysiwyg action error", e);
		}
		
		this.editor.requestFocus();
	}
	
}
