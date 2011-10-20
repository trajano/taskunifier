package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leclercb.taskunifier.gui.utils.Images;

public class TUAddButton extends JButton {
	
	public TUAddButton() {
		this(null);
	}
	
	public TUAddButton(ActionListener listener) {
		super(Images.getResourceImage("add.png", 16, 16));
		
		this.setActionCommand("ADD");
		
		if (listener != null)
			this.addActionListener(listener);
	}
	
}
