package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leclercb.taskunifier.gui.translations.Translations;

public class TUCloseButton extends JButton {
	
	public TUCloseButton() {
		this(null);
	}
	
	public TUCloseButton(ActionListener listener) {
		super(Translations.getString("general.close"));
		
		this.setActionCommand("CLOSE");
		
		if (listener != null)
			this.addActionListener(listener);
	}
	
}
