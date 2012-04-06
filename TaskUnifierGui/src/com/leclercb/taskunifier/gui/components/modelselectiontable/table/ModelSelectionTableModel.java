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
package com.leclercb.taskunifier.gui.components.modelselectiontable.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelFactory;
import com.leclercb.taskunifier.api.models.ModelParent;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.gui.components.modelselectiontable.ModelSelectionColumn;

public class ModelSelectionTableModel extends AbstractTableModel implements ListChangeListener, PropertyChangeListener {
	
	private ModelFactory<Model, ModelBean, Model, ModelBean> modelFactory;
	private List<Model> selectedModels;
	
	@SuppressWarnings("unchecked")
	public ModelSelectionTableModel(ModelType modelType) {
		CheckUtils.isNotNull(modelType);
		this.modelFactory = (ModelFactory<Model, ModelBean, Model, ModelBean>) ModelFactoryUtils.getFactory(modelType);
		
		this.selectedModels = new ArrayList<Model>();
		
		this.modelFactory.addListChangeListener(this);
		this.modelFactory.addPropertyChangeListener(this);
	}
	
	public Model[] getSelectedModels() {
		return this.selectedModels.toArray(new Model[0]);
	}
	
	public void setSelectedModels(Model[] models) {
		this.selectedModels.clear();
		
		if (models != null)
			this.selectedModels.addAll(Arrays.asList(models));
		
		this.fireTableDataChanged();
	}
	
	public Model getModel(int row) {
		return this.modelFactory.get(row);
	}
	
	public ModelSelectionColumn getModelSelectionColumn(int col) {
		return ModelSelectionColumn.values()[col];
	}
	
	@Override
	public int getColumnCount() {
		return ModelSelectionColumn.values().length;
	}
	
	@Override
	public int getRowCount() {
		return this.modelFactory.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return ModelSelectionColumn.values()[col].getLabel();
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return ModelSelectionColumn.values()[col].getType();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Model model = this.modelFactory.get(row);
		
		switch (col) {
			case 0:
				return this.selectedModels.contains(model);
		}
		
		return ModelSelectionColumn.values()[col].getProperty(model);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return ModelSelectionColumn.values()[col].isEditable();
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		Model model = this.getModel(row);
		
		switch (col) {
			case 0:
				if ((Boolean) value) {
					if (!this.selectedModels.contains(model))
						this.selectedModels.add(model);
				} else {
					this.selectedModels.remove(model);
				}
				
				break;
		}
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.fireTableRowsInserted(event.getIndex(), event.getIndex());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.fireTableRowsDeleted(event.getIndex(), event.getIndex());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(BasicModel.PROP_MODEL_STATUS)) {
			ModelStatus oldStatus = (ModelStatus) event.getOldValue();
			ModelStatus newStatus = (ModelStatus) event.getNewValue();
			
			if (oldStatus.isEndUserStatus() != newStatus.isEndUserStatus())
				this.fireTableDataChanged();
		} else if (event.getPropertyName().equals(ModelParent.PROP_PARENT)
				|| event.getPropertyName().equals(Model.PROP_ORDER)) {
			this.fireTableDataChanged();
		} else {
			int index = this.modelFactory.getIndexOf((Model) event.getSource());
			this.fireTableRowsUpdated(index, index);
		}
	}
	
}
