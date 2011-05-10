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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.models.ModelListModel;
import com.leclercb.taskunifier.gui.commons.renderers.ModelListCellRenderer;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public abstract class ModelList extends JPanel implements IModelList {
	
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
		
		model.addListDataListener(new ListDataListener() {
			
			@Override
			public void intervalRemoved(ListDataEvent e) {
				ModelList.this.modelList.setSelectedIndex(-1);
			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
				ModelList.this.modelList.setSelectedIndex(e.getIndex0());
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {

			}
			
		});
		
		this.modelList.setModel(model);
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
	
	public abstract void addModel();
	
	public abstract void removeModel(Model model);
	
	public abstract void modelSelected(Model model);
	
}
