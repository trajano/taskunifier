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
package com.leclercb.taskunifier.gui.components.models.lists;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.models.ModelListModel;
import com.leclercb.taskunifier.gui.commons.renderers.ModelListCellRenderer;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public abstract class ModelList extends JPanel {
	
	private JList modelList;
	private JButton addButton;
	private JButton removeButton;
	
	public ModelList(ModelListModel model) {
		this.initialize(model);
	}
	
	private void initialize(ModelListModel model) {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.modelList = new JList();
		this.modelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.modelList.setCellRenderer(new ModelListCellRenderer());
		this.modelList.setModel(model);
		this.modelList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting())
					return;
				
				if (ModelList.this.modelList.getSelectedValue() == null) {
					ModelList.this.removeButton.setEnabled(false);
				} else {
					ModelList.this.modelSelected((Model) ModelList.this.modelList.getSelectedValue());
					ModelList.this.removeButton.setEnabled(true);
				}
			}
			
		});
		
		this.add(
				ComponentFactory.createJScrollPane(this.modelList, true),
				BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonsPanel);
	}
	
	private void initializeButtons(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					ModelList.this.addModel();
				} else {
					ModelList.this.removeModel((Model) ModelList.this.modelList.getSelectedValue());
					ModelList.this.modelList.setSelectedIndex(0);
				}
			}
			
		};
		
		this.addButton = new JButton(Images.getResourceImage("add.png", 16, 16));
		this.addButton.setActionCommand("ADD");
		this.addButton.addActionListener(listener);
		buttonsPanel.add(this.addButton);
		
		this.removeButton = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		buttonsPanel.add(this.removeButton);
	}
	
	public void setSelectedModel(Model model) {
		this.modelList.setSelectedValue(model, true);
	}
	
	public Model getSelectedModel() {
		return (Model) this.modelList.getSelectedValue();
	}
	
	public abstract void addModel();
	
	public abstract void removeModel(Model model);
	
	public abstract void modelSelected(Model model);
	
}
