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
package com.leclercb.taskunifier.gui.components.modelselectiontable;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.components.modelselectiontable.table.ModelSelectionTable;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;

public class ModelSelectionPanel extends JPanel implements ModelSelectionView {
	
	private ModelSelectionTable table;
	
	public ModelSelectionPanel(
			TUTableProperties<ModelSelectionColumn> tableProperties,
			ModelType modelType) {
		this.initialize(tableProperties, modelType);
	}
	
	@Override
	public Model[] getSelectedModels() {
		return this.table.getSelectedModels();
	}
	
	@Override
	public void setSelectedModels(Model[] models) {
		this.table.setSelectedModels(models);
	}
	
	private void initialize(
			TUTableProperties<ModelSelectionColumn> tableProperties,
			ModelType modelType) {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		
		this.table = new ModelSelectionTable(tableProperties, modelType);
		
		this.add(this.table, BorderLayout.CENTER);
	}
	
}
