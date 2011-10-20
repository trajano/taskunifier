package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leclercb.taskunifier.gui.translations.Translations;

public class TUCancelButton extends JButton {
	
	public TUCancelButton() {
		this(null);
	}
	
	public TUCancelButton(ActionListener listener) {
		super(Translations.getString("general.cancel"));
		
		this.setActionCommand("CANCEL");
		
		if (listener != null)
			this.addActionListener(listener);
	}
	
}
