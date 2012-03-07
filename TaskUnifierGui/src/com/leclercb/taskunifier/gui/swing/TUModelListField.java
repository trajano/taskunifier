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
package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.components.modelselectiontable.ModelSelectionColumn;
import com.leclercb.taskunifier.gui.components.modelselectiontable.ModelSelectionPanel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TUModelListField<M extends Model> extends JPanel {
	
	public static final String PROP_MODELLIST = "modelList";
	
	private ModelType modelType;
	
	private TUModelListLabel modelListLabel;
	private JButton button;
	private JPopupMenu popup;
	private ModelSelectionPanel modelSelectionPanel;
	
	public TUModelListField(ModelType modelType) {
		CheckUtils.isNotNull(modelType);
		this.modelType = modelType;
		
		this.initialize();
	}
	
	@SuppressWarnings("unchecked")
	public ModelList<M> getModelList() {
		Model[] models = this.modelSelectionPanel.getSelectedModels();
		ModelList<M> modelList = new ModelList<M>();
		
		for (Model model : models) {
			modelList.add((M) model);
		}
		
		return modelList;
	}
	
	public void setModelList(ModelList<M> modelList) {
		Model[] models = new Model[0];
		
		if (modelList != null)
			models = modelList.getList().toArray(new Model[0]);
		
		this.modelSelectionPanel.setSelectedModels(models);
		this.modelListLabel.setModelList(modelList);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.modelListLabel.setEnabled(enabled);
		this.button.setEnabled(enabled);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.popup = new JPopupMenu();
		
		this.modelListLabel = new TUModelListLabel();
		
		this.button = new JButton(ImageUtils.getResourceImage(
				"edit.png",
				16,
				16));
		this.button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				TUModelListField.this.popup.show(
						TUModelListField.this.button,
						e.getX(),
						e.getY());
			}
			
		});
		
		JPanel popupPanel = new JPanel(new BorderLayout());
		popupPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.modelSelectionPanel = new ModelSelectionPanel(
				new TUTableProperties<ModelSelectionColumn>(
						ModelSelectionColumn.class,
						"modelselectioncolumn",
						false), this.modelType);
		
		popupPanel.add(this.modelSelectionPanel);
		
		this.popup.add(popupPanel);
		
		this.popup.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
				
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
				ModelList<M> modelList = TUModelListField.this.getModelList();
				
				TUModelListField.this.modelListLabel.setModelList(modelList);
				
				TUModelListField.this.firePropertyChange(
						PROP_MODELLIST,
						null,
						modelList);
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent event) {
				ModelList<M> modelList = TUModelListField.this.getModelList();
				
				TUModelListField.this.modelListLabel.setModelList(modelList);
				
				TUModelListField.this.firePropertyChange(
						PROP_MODELLIST,
						null,
						modelList);
			}
			
		});
		
		this.add(this.modelListLabel, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
	}
	
}
