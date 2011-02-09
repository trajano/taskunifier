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
package com.leclercb.taskunifier.gui.components.templates;

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

import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.models.TemplateModel;
import com.leclercb.taskunifier.gui.renderers.TemplateListCellRenderer;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.template.TemplateFactory;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

abstract class TemplateList extends JPanel {
	
	private JList templateList;
	private JButton addButton;
	private JButton removeButton;
	private JButton defaultButton;
	
	public TemplateList() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.templateList = new JList();
		this.templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.templateList.setCellRenderer(new TemplateListCellRenderer());
		this.templateList.setModel(new TemplateModel());
		this.templateList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting())
					return;
				
				if (TemplateList.this.templateList.getSelectedValue() == null) {
					TemplateList.this.removeButton.setEnabled(false);
					TemplateList.this.defaultButton.setEnabled(false);
				} else {
					TemplateList.this.templateSelected((Template) TemplateList.this.templateList.getSelectedValue());
					TemplateList.this.removeButton.setEnabled(true);
					TemplateList.this.defaultButton.setEnabled(true);
				}
			}
			
		});
		
		this.add(
				ComponentFactory.createJScrollPane(this.templateList, true),
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
					TemplateList.this.addTemplate();
				} else if (event.getActionCommand().equals("REMOVE")) {
					TemplateList.this.removeTemplate((Template) TemplateList.this.templateList.getSelectedValue());
					TemplateList.this.templateList.setSelectedIndex(0);
				} else {
					TemplateList.this.setDefaultTemplate((Template) TemplateList.this.templateList.getSelectedValue());
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
		
		this.defaultButton = new JButton(Images.getResourceImage(
				"properties.png",
				16,
				16));
		this.defaultButton.setActionCommand("DEFAULT");
		this.defaultButton.setToolTipText(Translations.getString("general.set_default"));
		this.defaultButton.addActionListener(listener);
		this.defaultButton.setEnabled(false);
		buttonsPanel.add(this.defaultButton);
	}
	
	public void setSelectedTemplate(Template template) {
		this.templateList.setSelectedValue(template, true);
	}
	
	public Template getSelectedTemplate() {
		return (Template) this.templateList.getSelectedValue();
	}
	
	public void addTemplate() {
		Template template = TemplateFactory.getInstance().create(
				Translations.getString("general.template"));
		this.setSelectedTemplate(template);
	}
	
	public void removeTemplate(Template template) {
		this.templateSelected(null);
		TemplateFactory.getInstance().unregister(template);
	}
	
	public void setDefaultTemplate(Template template) {
		TemplateFactory.getInstance().setDefaultTemplate(template);
	}
	
	public abstract void templateSelected(Template template);
	
}
