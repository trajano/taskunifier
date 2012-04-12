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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.JXHeader;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.lists.ITagList;
import com.leclercb.taskunifier.gui.components.models.panels.ContactConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.ContextConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.FolderConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.GoalConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.LocationConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.panels.TagConfigurationPanel;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ModelConfigurationDialog extends JDialog {
	
	private static ModelConfigurationDialog INSTANCE = null;
	
	public static ModelConfigurationDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ModelConfigurationDialog();
		
		return INSTANCE;
	}
	
	public static enum ModelConfigurationTab {
		
		CONTACTS,
		CONTEXTS,
		FOLDERS,
		GOALS,
		LOCATIONS,
		TAGS;
		
	}
	
	private JTabbedPane tabbedPane;
	
	private ModelConfigurationDialog() {
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.setLocationRelativeTo(FrameUtils.getCurrentFrame());
		}
		
		super.setVisible(visible);
	}
	
	public void setSelectedModelConfigurationTab(ModelConfigurationTab tab) {
		CheckUtils.isNotNull(tab);
		this.tabbedPane.setSelectedIndex(tab.ordinal());
	}
	
	public void setSelectedModel(ModelType type, Model model) {
		int index = -1;
		
		switch (type) {
			case CONTACT:
				index = 0;
				break;
			case CONTEXT:
				index = 1;
				break;
			case FOLDER:
				index = 2;
				break;
			case GOAL:
				index = 3;
				break;
			case LOCATION:
				index = 4;
				break;
		}
		
		if (index == -1)
			return;
		
		this.tabbedPane.setSelectedIndex(index);
		
		if (model != null) {
			IModelList list = (IModelList) this.tabbedPane.getSelectedComponent();
			list.setSelectedModel(model);
		}
	}
	
	public void setSelectedTag(Tag tag) {
		this.tabbedPane.setSelectedIndex(5);
		
		ITagList list = (ITagList) this.tabbedPane.getSelectedComponent();
		list.setSelectedTag(tag);
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.manage_models"));
		this.setSize(800, 500);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JXHeader header = new JXHeader();
		header.setTitle(Translations.getString("header.title.manage_models"));
		header.setDescription(Translations.getString("header.description.manage_models"));
		header.setIcon(ImageUtils.getResourceImage("folder.png", 32, 32));
		
		JPanel tabbedPanel = new JPanel(new BorderLayout());
		tabbedPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.tabbedPane = new JTabbedPane();
		
		this.tabbedPane.addTab(
				Translations.getString("general.contacts"),
				new ContactConfigurationPanel());
		
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
		
		this.tabbedPane.addTab(
				Translations.getString("general.task.tags"),
				new TagConfigurationPanel());
		
		tabbedPanel.add(this.tabbedPane);
		
		this.add(header, BorderLayout.NORTH);
		this.add(tabbedPanel, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ModelConfigurationDialog.this.setVisible(false);
			}
			
		};
		
		JButton okButton = new TUOkButton(listener);
		JPanel panel = new TUButtonsPanel(okButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
		
		this.getRootPane().registerKeyboardAction(
				listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
}
