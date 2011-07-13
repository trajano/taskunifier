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
package com.leclercb.taskunifier.gui.commons.undoableedit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class ModelDeleteUndoableEdit extends AbstractUndoableEdit {
	
	private ModelType type;
	private ModelId id;
	
	public ModelDeleteUndoableEdit(ModelType type, ModelId id) {
		CheckUtils.isNotNull(type, "Model type cannot be null");
		CheckUtils.isNotNull(id, "Model ID cannot be null");
		
		this.type = type;
		this.id = id;
	}
	
	@Override
	public String getPresentationName() {
		return Translations.getString("general.delete")
				+ ": "
				+ TranslationsUtils.translateModelType(this.type, true);
	}
	
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		
		Model model = ModelFactoryUtils.getModel(this.type, this.id);
		
		if (model == null)
			return;
		
		if (model.getModelStatus() != ModelStatus.TO_DELETE)
			return;
		
		model.setModelStatus(ModelStatus.TO_UPDATE);
	}
	
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		
		Model model = ModelFactoryUtils.getModel(this.type, this.id);
		
		if (model == null)
			return;
		
		if (model.getModelStatus() != ModelStatus.LOADED
				&& model.getModelStatus() != ModelStatus.TO_UPDATE)
			return;
		
		ModelFactoryUtils.getFactory(this.type).markToDelete(this.id);
	}
	
}
