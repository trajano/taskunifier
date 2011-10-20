package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leclercb.taskunifier.gui.translations.Translations;

public class TUOkButton extends JButton {
	
	public TUOkButton() {
		this(null);
	}
	
	public TUOkButton(ActionListener listener) {
		super(Translations.getString("general.ok"));
		
		this.setActionCommand("OK");
		
		if (listener != null)
			this.addActionListener(listener);
	}
	
}
