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
package com.leclercb.taskunifier.gui.components.welcome.panels;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class SettingsPanel extends CardPanel {
	
	private String title;
	private ConfigurationPanel panel;
	private CardInterface cardInterface;
	
	public SettingsPanel(String title, ConfigurationPanel panel) {
		this(title, panel, null);
	}
	
	public SettingsPanel(
			String title,
			ConfigurationPanel panel,
			CardInterface cardInterface) {
		this.reset(title, panel, cardInterface);
	}
	
	public ConfigurationPanel getConfigurationPanel() {
		return this.panel;
	}
	
	public void reset(
			String title,
			ConfigurationPanel panel,
			CardInterface cardInterface) {
		CheckUtils.isNotNull(title);
		CheckUtils.isNotNull(panel);
		
		this.title = title;
		this.panel = panel;
		this.cardInterface = cardInterface;
		
		this.removeAll();
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		panel.add(
				new JLabel(ImageUtils.getResourceImage("settings.png", 48, 48)),
				BorderLayout.WEST);
		panel.add(new JLabel(this.title));
		
		this.add(panel, BorderLayout.NORTH);
		
		this.add(this.panel, BorderLayout.CENTER);
	}
	
	@Override
	public boolean next() {
		if (this.cardInterface != null)
			return this.cardInterface.next();
		
		return true;
	}
	
	@Override
	public void display() {
		if (this.cardInterface != null)
			this.cardInterface.display();
	}
	
	@Override
	public void saveAndApplyConfig() {
		if (this.panel != null)
			this.panel.saveAndApplyConfig();
	}
	
	@Override
	public void cancelConfig() {
		if (this.panel != null)
			this.panel.cancelConfig();
	}
	
}
