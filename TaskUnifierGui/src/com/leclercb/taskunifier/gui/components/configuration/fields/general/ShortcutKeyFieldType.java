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
package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.ShortcutKey;
import com.leclercb.taskunifier.gui.swing.TUShortcutField;

public class ShortcutKeyFieldType extends ConfigurationFieldType.Panel implements PropertyChangeListener {
	
	private boolean first;
	private PropertyMap settings;
	private String propertyName;
	
	private TUShortcutField shortcutField;
	
	public ShortcutKeyFieldType(PropertyMap settings, String propertyName) {
		CheckUtils.isNotNull(settings);
		CheckUtils.isNotNull(propertyName);
		
		this.first = true;
		this.settings = settings;
		this.propertyName = propertyName;
		
		this.initialize();
	}
	
	private void initialize() {
		this.shortcutField = new TUShortcutField();
		
		JPanel panel = new JPanel();
		this.setPanel(panel);
		
		panel.setLayout(new BorderLayout());
		panel.add(this.shortcutField, BorderLayout.CENTER);
	}
	
	@Override
	public void initializeFieldComponent() {
		this.shortcutField.setShortcutKey(Main.getSettings().getObjectProperty(
				this.propertyName,
				ShortcutKey.class));
		
		if (this.first) {
			this.first = false;
			
			this.settings.addPropertyChangeListener(
					this.propertyName,
					new WeakPropertyChangeListener(this.settings, this));
		}
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.getSettings().setObjectProperty(
				this.propertyName,
				ShortcutKey.class,
				this.shortcutField.getShortcutKey());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.shortcutField.setShortcutKey(Main.getSettings().getObjectProperty(
				ShortcutKeyFieldType.this.propertyName,
				ShortcutKey.class));
	}
	
}
