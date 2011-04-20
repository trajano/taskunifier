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
package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
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
import com.leclercb.taskunifier.gui.api.models.GuiFolder;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class FolderConfigurationPanel extends JSplitPane implements IModelList {
	
	private ModelList modelList;
	
	public FolderConfigurationPanel() {
		this.initialize();
	}
	
	@Override
	public Model getSelectedModel() {
		return this.modelList.getSelectedModel();
	}
	
	@Override
	public void setSelectedModel(Model model) {
		this.modelList.setSelectedModel(model);
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField folderTitle = new JTextField(30);
		final JLabel folderColor = new JLabel();
		final JColorChooser folderColorChooser = new JColorChooser();
		final JButton removeColor = new JButton();
		
		// Initialize Model List
		this.modelList = new ModelList(new FolderModel(false)) {
			
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
				removeColor.setEnabled(model != null);
				
				if (model == null) {
					folderColor.setBackground(Color.GRAY);
					folderColorChooser.setColor(Color.GRAY);
				} else {
					folderColor.setBackground(((GuiFolder) model).getColor());
					folderColorChooser.setColor(((GuiFolder) model).getColor());
				}
			}
			
		};
		
		this.setLeftComponent(this.modelList);
		
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
				Translations.getString("general.color") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		folderColor.setEnabled(false);
		folderColor.setOpaque(true);
		folderColor.setBackground(Color.GRAY);
		folderColor.setBorder(new LineBorder(Color.BLACK));
		
		folderColorChooser.setColor(Color.GRAY);
		
		final JDialog colorDialog = JColorChooser.createDialog(
				this,
				"Color",
				true,
				folderColorChooser,
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						folderColor.setBackground(folderColorChooser.getColor());
						((GuiFolder) FolderConfigurationPanel.this.modelList.getSelectedModel()).setColor(folderColorChooser.getColor());
					}
					
				},
				null);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (folderColor.isEnabled())
					colorDialog.setVisible(true);
			}
			
		});
		
		removeColor.setEnabled(false);
		removeColor.setIcon(Images.getResourceImage("remove.png", 16, 16));
		removeColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				folderColor.setBackground(Color.GRAY);
				folderColorChooser.setColor(Color.GRAY);
				((GuiFolder) FolderConfigurationPanel.this.modelList.getSelectedModel()).setColor(null);
			}
			
		});
		
		JPanel p = new JPanel(new BorderLayout(5, 0));
		p.add(folderColor, BorderLayout.CENTER);
		p.add(removeColor, BorderLayout.EAST);
		
		info.add(p);
		
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
