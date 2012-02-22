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

import javax.swing.AbstractAction;
import javax.swing.JTextArea;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ModeInsertHTMLTextAction extends AbstractAction {
	
	private JTextArea textArea;
	private String text;
	
	public ModeInsertHTMLTextAction(
			JTextArea textArea,
			String icon,
			String description,
			String text) {
		CheckUtils.isNotNull(textArea);
		CheckUtils.isNotNull(icon);
		CheckUtils.isNotNull(text);
		
		this.textArea = textArea;
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
	public void actionPerformed(ActionEvent event) {
		int caret = this.textArea.getCaretPosition();
		
		if (this.text.contains("|")) {
			int index = this.text.indexOf('|');
			String selectedText = this.textArea.getSelectedText();
			
			if (selectedText == null)
				selectedText = "";
			
			this.textArea.replaceSelection(this.text.replace(
					"|",
					selectedText));
			this.textArea.setCaretPosition(caret + index);
		} else {
			this.textArea.insert(this.text, caret);
		}
		
		this.textArea.requestFocus();
	}
	
}
