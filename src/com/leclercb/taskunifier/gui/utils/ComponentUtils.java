package com.leclercb.taskunifier.gui.utils;

import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class ComponentUtils {
	
	private ComponentUtils() {

	}
	
	public static void focusAndSelectTextInTextField(JTextField textField) {
		int length = textField.getText().length();
		
		textField.setSelectionStart(0);
		textField.setSelectionEnd(length);
		
		textField.requestFocus();
	}
	
}
