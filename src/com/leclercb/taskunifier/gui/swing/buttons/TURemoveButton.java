package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.leclercb.taskunifier.gui.utils.Images;

public class TURemoveButton extends JButton {
	
	public TURemoveButton() {
		this(null);
	}
	
	public TURemoveButton(ActionListener listener) {
		super(Images.getResourceImage("remove.png", 16, 16));
		
		this.setActionCommand("REMOVE");
		
		if (listener != null)
			this.addActionListener(listener);
	}
	
}
