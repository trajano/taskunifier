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
package com.leclercb.taskunifier.gui.components.models.lists;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.commons.models.ModelListModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public abstract class ModelList extends JPanel implements IModelList {
	
	private JTextField titleField;
	
	private JXSearchField searchField;
	
	private JXList modelList;
	private ModelRowFilter rowFilter;
	
	private JButton addButton;
	private JButton removeButton;
	
	public ModelList(ModelListModel model, JTextField titleField) {
		CheckUtils.isNotNull(titleField, "Title field cannot be null");
		this.titleField = titleField;
		
		this.initialize(model);
	}
	
	private void initialize(ModelListModel model) {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.rowFilter = new ModelRowFilter();
		
		this.modelList = new JXList();
		this.modelList.setModel(model);
		this.modelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.modelList.setCellRenderer(new DefaultListRenderer(
				new StringValueModel(),
				new IconValueModel()));
		
		this.modelList.setAutoCreateRowSorter(true);
		this.modelList.setComparator(new ModelComparator());
		this.modelList.setSortOrder(SortOrder.DESCENDING);
		this.modelList.setSortsOnUpdates(true);
		this.modelList.toggleSortOrder();
		
		this.modelList.setRowFilter(rowFilter);
		
		this.modelList.setHighlighters(new AlternateHighlighter());
		
		this.modelList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting())
					return;
				
				if (ModelList.this.modelList.getSelectedValue() == null) {
					ModelList.this.modelSelected(null);
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
		
		this.searchField = new JXSearchField(Translations.getString("general.search"));
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelList.this.rowFilter.setTitle(e.getActionCommand());
				ModelList.this.modelList.setRowFilter(rowFilter);
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
					ModelList.this.modelList.setRowFilter(rowFilter);
					Model model = ModelList.this.addModel();
					ModelList.this.modelList.setSelectedValue(model, true);
					ModelList.this.focusAndSelectTextInTextField();
				} else {
					ModelList.this.removeModel((Model) ModelList.this.modelList.getSelectedValue());
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
	
	@Override
	public void setSelectedModel(Model model) {
		this.modelList.setSelectedValue(model, true);
	}
	
	@Override
	public Model getSelectedModel() {
		return (Model) this.modelList.getSelectedValue();
	}
	
	public abstract Model addModel();
	
	public abstract void removeModel(Model model);
	
	public abstract void modelSelected(Model model);
	
	private void focusAndSelectTextInTextField() {
		int length = this.titleField.getText().length();
		
		this.titleField.setSelectionStart(0);
		this.titleField.setSelectionEnd(length);
		
		this.titleField.requestFocus();
	}
	
}
