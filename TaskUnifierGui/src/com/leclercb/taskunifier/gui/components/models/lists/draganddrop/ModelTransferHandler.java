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
package com.leclercb.taskunifier.gui.components.models.lists.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.jdesktop.swingx.JXList;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelParent;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.ModelTransferable;

public class ModelTransferHandler<M extends ModelParent<M>> extends TransferHandler {
	
	private ModelType type;
	
	public ModelTransferHandler(ModelType type) {
		CheckUtils.isNotNull(type);
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canImport(TransferSupport support) {
		Transferable t = support.getTransferable();
		
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			// Get Drag Model
			List<M> dragModels = new ArrayList<M>();
			
			try {
				ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (!data.getType().equals(this.type))
					return false;
				
				for (ModelId id : data.getIds()) {
					Model model = ModelFactoryUtils.getModel(this.type, id);
					if (model instanceof ModelParent)
						dragModels.add((M) model);
				}
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						"Transfer data error",
						e);
				
				return false;
			}
			
			if (support.isDrop()) {
				// Get Objects
				JXList list = (JXList) support.getComponent();
				JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
				
				// True : If insert row
				if (((JList.DropLocation) support.getDropLocation()).isInsert())
					return true;
				
				// Get Drop Model
				int index = list.convertIndexToModel(dl.getIndex());
				M dropModel = (M) list.getModel().getElementAt(index);
				
				if (dropModel == null)
					return false;
				
				// False if drag model equals to drop model
				for (M dragModel : dragModels)
					if (dragModel.equals(dropModel))
						return false;
				
				return true;
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		JXList list = (JXList) c;
		Model model = (Model) list.getSelectedValue();
		
		return new ModelTransferable(new ModelTransferData(
				this.type,
				model.getModelId()));
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		
		Transferable t = support.getTransferable();
		JXList list = (JXList) support.getComponent();
		
		if (support.isDataFlavorSupported(ModelTransferable.MODEL_FLAVOR)) {
			// Get Drag Model
			List<M> dragModels = new ArrayList<M>();
			
			try {
				ModelTransferData data = (ModelTransferData) t.getTransferData(ModelTransferable.MODEL_FLAVOR);
				
				if (!data.getType().equals(this.type))
					return false;
				
				for (ModelId id : data.getIds()) {
					Model model = ModelFactoryUtils.getModel(this.type, id);
					if (model instanceof ModelParent)
						dragModels.add((M) model);
				}
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.WARNING,
						"Transfer data error",
						e);
				
				return false;
			}
			
			if (support.isDrop()) {
				// Get Objects
				JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
				
				// Import : If insert row
				if (dl.isInsert()) {
					for (M dragModel : dragModels) {
						dragModel.setParent(null);
					}
					
					return true;
				}
				
				// Get Drop Task
				int index = list.convertIndexToModel(dl.getIndex());
				M dropModel = (M) list.getModel().getElementAt(index);
				
				if (dropModel == null)
					return false;
				
				// Import
				for (M dragModel : dragModels) {
					dragModel.setParent(dropModel);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		
	}
	
}
