package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;

public class WysiwygHTMLEditorKit extends HTMLEditorKit {
	
	private MyLinkController handler = new MyLinkController();
	
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
	
	public class MyLinkController extends LinkController {
		
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
