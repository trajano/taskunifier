package com.leclercb.taskunifier.gui.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.main.frames.ShortcutKey;

public class TUShortcutField extends JTextField {
	
	public static final String PROP_SHORTCUT_KEY = "shortcutKey";
	
	private ShortcutKey shortcutKey;
	
	public TUShortcutField() {
		this.shortcutKey = null;
		
		this.initialize();
	}
	
	private void initialize() {
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent event) {}
			
			@Override
			public void keyReleased(KeyEvent event) {
				if (TUShortcutField.this.shortcutKey == null)
					TUShortcutField.this.setShortcutKey(null);
			}
			
			@Override
			public void keyPressed(KeyEvent event) {
				ShortcutKey shortcutKey = null;
				
				if (event.getModifiers() != 0
						&& event.getKeyCode() != KeyEvent.VK_ALT
						&& event.getKeyCode() != KeyEvent.VK_ALT_GRAPH
						&& event.getKeyCode() != KeyEvent.VK_CONTROL
						&& event.getKeyCode() != KeyEvent.VK_SHIFT)
					shortcutKey = new ShortcutKey(
							event.getKeyCode(),
							event.getModifiers());
				
				TUShortcutField.this.setShortcutKey(shortcutKey);
			}
			
		});
	}
	
	public ShortcutKey getShortcutKey() {
		return this.shortcutKey;
	}
	
	public void setShortcutKey(ShortcutKey shortcutKey) {
		ShortcutKey oldShortcutKey = this.shortcutKey;
		this.shortcutKey = shortcutKey;
		
		if (shortcutKey == null)
			this.setText(null);
		else
			this.setText(KeyEvent.getKeyModifiersText(shortcutKey.getModifiers())
					+ " + "
					+ KeyEvent.getKeyText(shortcutKey.getKeyCode()));
		
		this.firePropertyChange(PROP_SHORTCUT_KEY, oldShortcutKey, shortcutKey);
	}
	
}
