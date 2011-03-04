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
package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.GuiFolder;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.models.FolderModel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class FolderConfigurationPanel extends JSplitPane {
	
	public FolderConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField folderTitle = new JTextField(30);
		final JLabel folderColor = new JLabel();
		final JColorChooser folderColorChooser = new JColorChooser();
		
		// Initialize Model List
		final ModelList modelList = new ModelList(new FolderModel(false)) {
			
			private BeanAdapter<Folder> adapter;
			
			{
				this.adapter = new BeanAdapter<Folder>((Folder) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Model.PROP_TITLE);
				Bindings.bind(folderTitle, titleModel);
			}
			
			@Override
			public void addModel() {
				Model model = FolderFactory.getInstance().create(
						Translations.getString("folder.default.title"));
				this.setSelectedModel(model);
				FolderConfigurationPanel.this.focusAndSelectTextInTextField(folderTitle);
			}
			
			@Override
			public void removeModel(Model model) {
				this.modelSelected(null);
				FolderFactory.getInstance().markToDelete(model);
			}
			
			@Override
			public void modelSelected(Model model) {
				this.adapter.setBean(model != null ? (Folder) model : null);
				folderTitle.setEnabled(model != null);
				folderColor.setEnabled(model != null);
				
				if (model == null) {
					folderColor.setBackground(Color.WHITE);
					folderColorChooser.setColor(Color.WHITE);
				} else {
					folderColor.setBackground(((GuiFolder) model).getColor());
					folderColorChooser.setColor(((GuiFolder) model).getColor());
				}
			}
			
		};
		
		this.setLeftComponent(modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(rightPanel);
		
		JPanel info = new JPanel();
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Folder Title
		label = new JLabel(
				Translations.getString("general.folder.title") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		folderTitle.setEnabled(false);
		info.add(folderTitle);
		
		// Folder Color
		label = new JLabel(
				Translations.getString("general.folder.color") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		folderColor.setOpaque(true);
		folderColor.setBackground(Color.WHITE);
		folderColor.setBorder(new LineBorder(Color.BLACK));
		
		folderColorChooser.setColor(Color.WHITE);
		
		final JDialog colorDialog = JColorChooser.createDialog(
				this,
				"Color",
				true,
				folderColorChooser,
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						folderColor.setBackground(folderColorChooser.getColor());
						((GuiFolder) modelList.getSelectedModel()).setColor(folderColorChooser.getColor());
					}
					
				},
				null);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				colorDialog.setVisible(true);
			}
			
		});
		
		info.add(folderColor);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 2, 2, // rows, cols
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
	
}
