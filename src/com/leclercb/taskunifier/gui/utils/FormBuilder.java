package com.leclercb.taskunifier.gui.utils;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class FormBuilder {
	
	private DefaultFormBuilder builder;
	
	public FormBuilder(String layout) {
		this.builder = new DefaultFormBuilder(new FormLayout(layout, ""));
	}
	
	public JLabel append(String textWithMnemonic) {
		return this.builder.append(textWithMnemonic);
	}
	
	public void append(Component component) {
		this.builder.append(component);
	}
	
	public JLabel append(String textWithMnemonic, Component component) {
		return this.builder.append(textWithMnemonic, component);
	}
	
	public JLabel appendI15d(String resourceKey, boolean colon) {
		String s = (colon ? ":" : "");
		return this.builder.append(Translations.getString(resourceKey) + s);
	}
	
	public JLabel appendI15d(
			String resourceKey,
			boolean colon,
			Component component) {
		String s = (colon ? ":" : "");
		return this.builder.append(
				Translations.getString(resourceKey) + s,
				component);
	}
	
	public JComponent appendSeparator(String text) {
		return this.builder.appendSeparator(text);
	}
	
	public JComponent appendI15dSeparator(String resourceKey) {
		return this.builder.appendSeparator(Translations.getString(resourceKey));
	}
	
	public JComponent appendSeparator() {
		return this.builder.appendSeparator();
	}
	
	public DefaultFormBuilder getBuilder() {
		return this.builder;
	}
	
	public JPanel getPanel() {
		return this.builder.getPanel();
	}
	
}
