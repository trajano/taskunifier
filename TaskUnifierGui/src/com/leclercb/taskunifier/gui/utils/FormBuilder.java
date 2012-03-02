/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.utils;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.leclercb.taskunifier.gui.translations.Translations;

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
