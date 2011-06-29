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
package com.leclercb.taskunifier.gui.components.welcome;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.components.configuration.GeneralConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.PluginConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.ProxyConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.SynchronizationConfigurationPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.CardPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.SettingsPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.WelcomePanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class WelcomeDialog extends JDialog {
	
	private CardPanel[] panels = new CardPanel[] {
			new WelcomePanel(),
			new SettingsPanel(
					Translations.getString("configuration.tab.general"),
					new GeneralConfigurationPanel(false, true)),
			new SettingsPanel(
					Translations.getString("configuration.tab.proxy"),
					new ProxyConfigurationPanel()),
			new SettingsPanel(
					Translations.getString("configuration.tab.synchronization"),
					new SynchronizationConfigurationPanel(true)),
			new SettingsPanel(
					SynchronizerUtils.getPlugin().getName(),
					new PluginConfigurationPanel(
							false,
							SynchronizerUtils.getPlugin())) };
	
	private JPanel cardPanel;
	private int currentPanel;
	
	public WelcomeDialog(Frame frame) {
		super(frame);
		
		// For API Configuration Panel
		Main.SETTINGS.addPropertyChangeListener(
				"api.id",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						SettingsPanel servicePanel = (SettingsPanel) WelcomeDialog.this.panels[WelcomeDialog.this.panels.length - 1];
						
						servicePanel.reset(
								SynchronizerUtils.getPlugin().getName(),
								new PluginConfigurationPanel(
										false,
										SynchronizerUtils.getPlugin()));
						
						((CardLayout) WelcomeDialog.this.cardPanel.getLayout()).previous(WelcomeDialog.this.cardPanel);
						((CardLayout) WelcomeDialog.this.cardPanel.getLayout()).next(WelcomeDialog.this.cardPanel);
					}
					
				});
		
		this.initialize();
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.welcome"));
		this.setSize(700, 600);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.currentPanel = 0;
		
		this.cardPanel = new JPanel();
		this.cardPanel.setLayout(new CardLayout());
		this.cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		this.add(this.cardPanel, BorderLayout.CENTER);
		
		int i = 0;
		for (CardPanel cp : this.panels)
			this.cardPanel.add(cp, "" + i++);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		final JButton previousButton = new JButton(
				Translations.getString("general.previous"));
		
		final JButton nextButton = new JButton(
				Translations.getString("general.next"));
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				WelcomeDialog.this.panels[WelcomeDialog.this.currentPanel].applyChanges();
				
				if (event.getActionCommand().equals("PREVIOUS")) {
					if (WelcomeDialog.this.currentPanel != 0) {
						WelcomeDialog.this.currentPanel--;
						((CardLayout) WelcomeDialog.this.cardPanel.getLayout()).previous(WelcomeDialog.this.cardPanel);
					}
				}
				
				if (event.getActionCommand().equals("NEXT")) {
					if (WelcomeDialog.this.currentPanel < WelcomeDialog.this.panels.length - 1) {
						WelcomeDialog.this.currentPanel++;
						((CardLayout) WelcomeDialog.this.cardPanel.getLayout()).next(WelcomeDialog.this.cardPanel);
						
						if (WelcomeDialog.this.currentPanel == 3)
							ActionManagePlugins.managePlugins();
					} else {
						WelcomeDialog.this.dispose();
					}
				}
				
				if (WelcomeDialog.this.currentPanel == WelcomeDialog.this.panels.length - 1)
					nextButton.setText(Translations.getString("general.finish"));
				else
					nextButton.setText(Translations.getString("general.next"));
				
				if (WelcomeDialog.this.currentPanel == 0)
					previousButton.setEnabled(false);
				else
					previousButton.setEnabled(true);
			}
			
		};
		
		previousButton.setActionCommand("PREVIOUS");
		previousButton.addActionListener(listener);
		previousButton.setEnabled(false);
		
		nextButton.setActionCommand("NEXT");
		nextButton.addActionListener(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				previousButton,
				nextButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
}
