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
package com.leclercb.taskunifier.gui.components.models;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.panels.ContextConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.FolderConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.GoalConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.LocationConfigurationPanel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ModelConfigurationDialog extends JDialog {
	
	private JTabbedPane tabbedPane;
	
	public ModelConfigurationDialog(Frame frame, boolean modal) {
		super(frame, modal);
		this.initialize();
	}
	
	public void setSelectedModel(ModelType type, Model model) {
		int index = -1;
		
		switch (type) {
			case CONTEXT:
				index = 0;
				break;
			case FOLDER:
				index = 1;
				break;
			case GOAL:
				index = 2;
				break;
			case LOCATION:
				index = 3;
				break;
		}
		
		if (index == -1)
			return;
		
		this.tabbedPane.setSelectedIndex(index);
		
		if (model != null)
			((IModelList) this.tabbedPane.getSelectedComponent()).setSelectedModel(model);
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.manage_models"));
		this.setSize(600, 400);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.tabbedPane = new JTabbedPane();
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		this.add(this.tabbedPane, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
		
		this.tabbedPane.addTab(
				Translations.getString("general.contexts"),
				new ContextConfigurationPanel());
		
		this.tabbedPane.addTab(
				Translations.getString("general.folders"),
				new FolderConfigurationPanel());
		
		this.tabbedPane.addTab(
				Translations.getString("general.goals"),
				new GoalConfigurationPanel());
		
		this.tabbedPane.addTab(
				Translations.getString("general.locations"),
				new LocationConfigurationPanel());
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					ModelConfigurationDialog.this.dispose();
				}
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
