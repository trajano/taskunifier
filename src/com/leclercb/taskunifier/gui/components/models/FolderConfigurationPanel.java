/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.models.FolderListModel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SpringUtils;

public class FolderConfigurationPanel extends JSplitPane implements PropertyChangeListener {
	
	private Folder selectedFolder;
	
	private JTextField folderTitle;
	
	public FolderConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		// Initialize Fields
		this.folderTitle = new JTextField(30);
		
		// Initialize Model List
		final ModelList modelList = new ModelList(new FolderListModel()) {
			
			@Override
			public void addModel() {
				Model model = FolderFactory.getInstance().create(
						Translations.getString("folder.default.title"));
				this.setSelectedModel(model);
				FolderConfigurationPanel.this.focusAndSelectTextInTextField(FolderConfigurationPanel.this.folderTitle);
			}
			
			@Override
			public void removeModel(Model model) {
				this.modelSelected(null);
				FolderFactory.getInstance().markToDelete(
						this.getSelectedModel());
			}
			
			@Override
			public void modelSelected(Model model) {
				if (FolderConfigurationPanel.this.selectedFolder != null)
					FolderConfigurationPanel.this.selectedFolder.removePropertyChangeListener(FolderConfigurationPanel.this);
				
				FolderConfigurationPanel.this.selectedFolder = (Folder) model;
				
				if (FolderConfigurationPanel.this.selectedFolder != null)
					FolderConfigurationPanel.this.selectedFolder.addPropertyChangeListener(FolderConfigurationPanel.this);
				
				if (model == null) {
					FolderConfigurationPanel.this.folderTitle.setEnabled(false);
					FolderConfigurationPanel.this.folderTitle.setText("");
					
					return;
				}
				
				Folder folder = (Folder) model;
				
				FolderConfigurationPanel.this.folderTitle.setEnabled(true);
				FolderConfigurationPanel.this.folderTitle.setText(folder.getTitle());
			}
			
		};
		
		this.setLeftComponent(modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(rightPanel);
		
		JPanel info = new JPanel();
		info.setBorder(new LineBorder(Color.BLACK));
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Folder Title
		label = new JLabel(
				Translations.getString("general.folder.title") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		this.folderTitle.setEnabled(false);
		this.folderTitle.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				Folder folder = (Folder) modelList.getSelectedModel();
				folder.setTitle(FolderConfigurationPanel.this.folderTitle.getText());
			}
			
		});
		info.add(this.folderTitle);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 1, 2, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		this.setDividerLocation(200);
	}
	
	private void focusAndSelectTextInTextField(JTextField field) {
		int length = field.getText().length();
		
		field.setSelectionStart(0);
		field.setSelectionEnd(length);
		
		field.requestFocus();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Folder.PROP_MODEL_TITLE)) {
			if (!EqualsUtils.equals(
					this.folderTitle.getText(),
					evt.getNewValue()))
				this.folderTitle.setText((String) evt.getNewValue());
		}
	}
	
}
