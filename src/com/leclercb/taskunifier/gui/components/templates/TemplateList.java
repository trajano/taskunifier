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
package com.leclercb.taskunifier.gui.components.templates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.commons.comparators.TemplateComparator;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.commons.models.TemplateModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTemplateTitle;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

abstract class TemplateList extends JPanel {
	
	private JTextField titleField;
	
	private JXSearchField searchField;
	
	private JXList templateList;
	private TemplateRowFilter rowFilter;
	
	private JButton addButton;
	private JButton removeButton;
	private JButton defaultButton;
	
	public TemplateList(JTextField titleField) {
		CheckUtils.isNotNull(titleField, "Title field cannot be null");
		this.titleField = titleField;
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		TemplateModel model = new TemplateModel(false);
		
		this.templateList = new JXList();
		this.templateList.setModel(model);
		this.templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.templateList.setCellRenderer(new DefaultListRenderer(
				new StringValueTemplateTitle()));
		
		this.templateList.setAutoCreateRowSorter(true);
		this.templateList.setComparator(new TemplateComparator());
		this.templateList.setSortOrder(SortOrder.ASCENDING);
		this.templateList.setSortsOnUpdates(true);
		
		this.rowFilter = new TemplateRowFilter();
		this.templateList.setRowFilter(this.rowFilter);
		
		this.templateList.setHighlighters(new AlternateHighlighter());
		
		this.templateList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting())
					return;
				
				if (TemplateList.this.templateList.getSelectedValue() == null) {
					TemplateList.this.templateSelected(null);
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
		
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TemplateList.this.rowFilter.setTitle(e.getActionCommand());
			}
			
		});
		
		this.rowFilter.addPropertyChangeListener(
				TemplateRowFilter.PROP_TITLE,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TemplateList.this.searchField.setText((String) evt.getNewValue());
						TemplateList.this.templateList.setRowFilter((TemplateRowFilter) evt.getSource());
					}
					
				});
		
		this.add(this.searchField, BorderLayout.NORTH);
		
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
					TemplateList.this.rowFilter.setTitle(null);
					Template template = TemplateList.this.addTemplate();
					TemplateList.this.templateList.setSelectedValue(
							template,
							true);
					TemplateList.this.focusAndSelectTextInTextField();
				} else if (event.getActionCommand().equals("REMOVE")) {
					TemplateList.this.removeTemplate((Template) TemplateList.this.templateList.getSelectedValue());
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
	
	public Template addTemplate() {
		return TemplateFactory.getInstance().create(
				Translations.getString("general.template"));
	}
	
	public void removeTemplate(Template template) {
		TemplateFactory.getInstance().unregister(template);
	}
	
	public void setDefaultTemplate(Template template) {
		TemplateFactory.getInstance().setDefaultTemplate(template);
	}
	
	public abstract void templateSelected(Template template);
	
	private void focusAndSelectTextInTextField() {
		int length = this.titleField.getText().length();
		
		this.titleField.setSelectionStart(0);
		this.titleField.setSelectionEnd(length);
		
		this.titleField.requestFocus();
	}
	
}
