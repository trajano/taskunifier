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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;

public class WysiwygHTMLEditorKit extends HTMLEditorKit {
	
	private MyLinkController handler;
	
	public WysiwygHTMLEditorKit() {
		this.handler = new MyLinkController();
	}
	
	@Override
	public void install(JEditorPane c) {
		MouseListener[] oldMouseListeners = c.getMouseListeners();
		MouseMotionListener[] oldMouseMotionListeners = c.getMouseMotionListeners();
		
		super.install(c);
		
		for (MouseListener l : c.getMouseListeners()) {
			c.removeMouseListener(l);
		}
		
		for (MouseListener l : oldMouseListeners) {
			c.addMouseListener(l);
		}
		
		for (MouseMotionListener l : c.getMouseMotionListeners()) {
			c.removeMouseMotionListener(l);
		}
		
		for (MouseMotionListener l : oldMouseMotionListeners) {
			c.addMouseMotionListener(l);
		}
		
		c.addMouseListener(this.handler);
		c.addMouseMotionListener(this.handler);
	}
	
	private class MyLinkController extends LinkController {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			JEditorPane editor = (JEditorPane) e.getSource();
			
			if (editor.isEditable() && SwingUtilities.isLeftMouseButton(e)) {
				if (e.getClickCount() == 2) {
					editor.setEditable(false);
					super.mouseClicked(e);
					editor.setEditable(true);
					
					editor.setSelectionStart(0);
					editor.setSelectionEnd(0);
					editor.requestFocus();
				}
			}
			
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			JEditorPane editor = (JEditorPane) e.getSource();
			
			if (editor.isEditable()) {
				editor.setEditable(false);
				super.mouseMoved(e);
				editor.setEditable(true);
			}
		}
		
	}
	
}
