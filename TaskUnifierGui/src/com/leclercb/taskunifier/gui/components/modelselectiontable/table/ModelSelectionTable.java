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

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXTable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.components.modelselectiontable.ModelSelectionColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;

public class ModelSelectionTable extends JXTable {
	
	private TUTableProperties<ModelSelectionColumn> tableProperties;
	
	public ModelSelectionTable(
			TUTableProperties<ModelSelectionColumn> tableProperties,
			ModelType modelType) {
		CheckUtils.isNotNull(tableProperties);
		this.tableProperties = tableProperties;
		
		this.initialize(modelType);
	}
	
	public Model getModel(int row) {
		try {
			int index = this.getRowSorter().convertRowIndexToModel(row);
			return ((ModelSelectionTableModel) this.getModel()).getModel(index);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}
	
	public Model[] getSelectedModels() {
		return ((ModelSelectionTableModel) this.getModel()).getSelectedModels();
	}
	
	public void setSelectedModels(Model[] models) {
		((ModelSelectionTableModel) this.getModel()).setSelectedModels(models);
	}
	
	private void initialize(ModelType modelType) {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		ModelSelectionTableColumnModel columnModel = new ModelSelectionTableColumnModel(
				this.tableProperties);
		ModelSelectionTableModel tableModel = new ModelSelectionTableModel(
				modelType);
		
		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setShowGrid(true, false);
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		
		this.setSortable(false);
		this.setSortsOnUpdates(false);
		this.setSortOrderCycle(SortOrder.ASCENDING, SortOrder.DESCENDING);
		this.setSortOrder(1, SortOrder.ASCENDING);
		
		this.getTableHeader().setReorderingAllowed(false);
		
		this.initializeHighlighters();
	}
	
	private void initializeHighlighters() {
		this.setHighlighters(new AlternateHighlighter());
	}
	
}
