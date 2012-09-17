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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionManageSynchronizerPlugins;
import com.leclercb.taskunifier.gui.components.configuration.DateConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.GeneralConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.ProxyConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.SynchronizationConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.welcome.panels.CardInterface;
import com.leclercb.taskunifier.gui.components.welcome.panels.CardPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.SettingsPanel;
import com.leclercb.taskunifier.gui.components.welcome.panels.WelcomePanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class WelcomeDialog extends JDialog implements ConfigurationGroup {
	
	private List<CardPanel> panels;
	
	private JPanel cardPanel;
	private int currentPanel;
	
	private JButton previousButton;
	private JButton nextButton;
	
	public WelcomeDialog() {
		this(null, null);
	}
	
	public WelcomeDialog(String[] messages, TUButtonsPanel messageButtons) {
		this.panels = new ArrayList<CardPanel>();
		
		if (!Main.isFirstExecution()) {
			this.panels.add(new WelcomePanel(messages, messageButtons));
		} else {
			this.panels.add(new WelcomePanel(messages, messageButtons));
			
			this.panels.add(new SettingsPanel(
					Translations.getString("configuration.tab.general"),
					new GeneralConfigurationPanel(this, false, true)));
			
			this.panels.add(new SettingsPanel(
					Translations.getString("configuration.tab.date"),
					new DateConfigurationPanel(this, false)));
			
			this.panels.add(new SettingsPanel(
					Translations.getString("configuration.tab.proxy"),
					new ProxyConfigurationPanel(this, false)));
			
			this.panels.add(new SettingsPanel(
					Translations.getString("configuration.tab.synchronization"),
					new SynchronizationConfigurationPanel(this, true),
					new CardInterface() {
						
						@Override
						public boolean next() {
							return true;
						}
						
						@Override
						public void display() {
							ActionManageSynchronizerPlugins.manageSynchronizerPlugins();
						}
						
					}));
		}
		
		this.initialize();
	}
	
	public WelcomeDialog(List<CardPanel> panels) {
		CheckUtils.isNotNull(panels);
		
		if (panels.size() == 0)
			throw new IllegalArgumentException();
		
		this.panels = new ArrayList<CardPanel>(panels);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.welcome"));
		this.setSize(700, 600);
		this.setResizable(true);
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
		for (CardPanel cp : this.panels) {
			if (cp.displayInScrollPane())
				this.cardPanel.add(
						ComponentFactory.createJScrollPane(cp, false),
						"" + i++);
			else
				this.cardPanel.add(cp, "" + i++);
		}
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		this.previousButton = new JButton(
				Translations.getString("general.previous"));
		
		this.nextButton = new JButton(Translations.getString("general.next"));
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("PREVIOUS")) {
					WelcomeDialog.this.previous();
				}
				
				if (event.getActionCommand().equals("NEXT")) {
					WelcomeDialog.this.next();
				}
			}
			
		};
		
		this.previousButton.setActionCommand("PREVIOUS");
		this.previousButton.addActionListener(listener);
		
		this.nextButton.setActionCommand("NEXT");
		this.nextButton.addActionListener(listener);
		
		this.checkButtonsState();
		
		JPanel panel = new TUButtonsPanel(this.previousButton, this.nextButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
	public void previous() {
		this.panels.get(this.currentPanel).saveAndApplyConfig();
		
		if (this.currentPanel != 0) {
			this.currentPanel--;
			((CardLayout) this.cardPanel.getLayout()).previous(this.cardPanel);
		}
		
		this.checkButtonsState();
	}
	
	public void next() {
		this.panels.get(this.currentPanel).saveAndApplyConfig();
		
		if (this.panels.get(this.currentPanel).next()) {
			if (this.currentPanel < this.panels.size() - 1) {
				this.currentPanel++;
				((CardLayout) this.cardPanel.getLayout()).next(this.cardPanel);
				this.panels.get(this.currentPanel).display();
				
				this.checkButtonsState();
			} else {
				this.setVisible(false);
				this.dispose();
			}
		}
	}
	
	private void checkButtonsState() {
		if (this.currentPanel == 0)
			this.previousButton.setEnabled(false);
		else
			this.previousButton.setEnabled(true);
		
		if (this.currentPanel == this.panels.size() - 1)
			this.nextButton.setText(Translations.getString("general.finish"));
		else
			this.nextButton.setText(Translations.getString("general.next"));
	}
	
	@Override
	public void saveAndApplyConfig() {
		for (CardPanel panel : this.panels)
			panel.saveAndApplyConfig();
	}
	
	@Override
	public void cancelConfig() {
		for (CardPanel panel : this.panels)
			panel.cancelConfig();
	}
	
}
