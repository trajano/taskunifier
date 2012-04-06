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
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.comparators.BasicModelComparator;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.commons.models.ModelListModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ComponentUtils;

public abstract class ModelList extends JPanel implements IModelList {
	
	private JTextField titleField;
	
	private JXSearchField searchField;
	
	private JXList modelList;
	private ModelRowFilter rowFilter;
	
	private TUButtonsPanel buttonsPanel;
	
	private JButton addButton;
	private JButton removeButton;
	
	public ModelList(ModelListModel model, JTextField titleField) {
		CheckUtils.isNotNull(titleField);
		this.titleField = titleField;
		
		this.initialize(model);
	}
	
	private void initialize(ModelListModel model) {
		this.setLayout(new BorderLayout(0, 3));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.modelList = new JXList();
		this.modelList.setModel(model);
		this.modelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.modelList.setCellRenderer(new DefaultListRenderer(
				StringValueModel.INSTANCE_INDENTED,
				IconValueModel.INSTANCE));
		
		this.modelList.setAutoCreateRowSorter(true);
		this.modelList.setComparator(BasicModelComparator.INSTANCE);
		this.modelList.setSortOrder(SortOrder.ASCENDING);
		this.modelList.setSortsOnUpdates(true);
		
		this.rowFilter = new ModelRowFilter();
		this.modelList.setRowFilter(this.rowFilter);
		
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
					ModelList.this.modelSelected(ModelList.this.getSelectedModel());
					ModelList.this.removeButton.setEnabled(true);
				}
			}
			
		});
		
		this.add(
				ComponentFactory.createJScrollPane(this.modelList, true),
				BorderLayout.CENTER);
		
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ModelList.this.rowFilter.setTitle(e.getActionCommand());
			}
			
		});
		
		this.rowFilter.addPropertyChangeListener(
				ModelRowFilter.PROP_TITLE,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ModelList.this.searchField.setText((String) evt.getNewValue());
						ModelList.this.modelList.setRowFilter((ModelRowFilter) evt.getSource());
					}
					
				});
		
		this.add(this.searchField, BorderLayout.NORTH);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					ModelList.this.rowFilter.setTitle(null);
					Model model = ModelList.this.addModel();
					ModelList.this.setSelectedModel(model);
					ComponentUtils.focusAndSelectTextInTextField(ModelList.this.titleField);
				} else {
					Model model = ModelList.this.getSelectedModel();
					ModelList.this.removeModel(model);
				}
			}
			
		};
		
		this.addButton = new TUAddButton(listener);
		
		this.removeButton = new TURemoveButton(listener);
		this.removeButton.setEnabled(false);
		
		this.buttonsPanel = new TUButtonsPanel(
				true,
				this.addButton,
				this.removeButton);
		
		this.add(this.buttonsPanel, BorderLayout.SOUTH);
	}
	
	public JXList getModelList() {
		return this.modelList;
	}
	
	public void addButton(JButton button) {
		this.buttonsPanel.addButton(button);
	}
	
	@Override
	public Model getSelectedModel() {
		return (Model) this.modelList.getSelectedValue();
	}
	
	@Override
	public void setSelectedModel(Model model) {
		this.modelList.setSelectedValue(model, true);
	}
	
	public abstract Model addModel();
	
	public abstract void removeModel(Model model);
	
	public abstract void modelSelected(Model model);
	
}
