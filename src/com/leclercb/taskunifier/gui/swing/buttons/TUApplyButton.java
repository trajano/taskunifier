package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leclercb.taskunifier.gui.translations.Translations;

public class TUApplyButton extends JButton {
	
	public TUApplyButton() {
		this(null);
	}
	
	public TUApplyButton(ActionListener listener) {
		super(Translations.getString("general.apply"));
		
		this.setActionCommand("APPLY");
		
		if (listener != null)
			this.addActionListener(listener);
	}
	
}
