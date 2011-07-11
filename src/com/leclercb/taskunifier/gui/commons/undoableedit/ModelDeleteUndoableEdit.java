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
