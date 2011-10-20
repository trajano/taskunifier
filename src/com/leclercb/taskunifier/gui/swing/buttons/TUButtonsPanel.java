package com.leclercb.taskunifier.gui.swing.buttons;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TUButtonsPanel extends JPanel {
	
	public TUButtonsPanel(JButton... buttons) {
		this(false, buttons);
	}
	
	public TUButtonsPanel(boolean removeText, JButton... buttons) {
		this.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		if (buttons == null)
			return;
		
		for (JButton button : buttons) {
			if (button != null) {
				if (removeText)
					button.setText("");
				
				this.add(button);
			}
		}
	}
	
}
