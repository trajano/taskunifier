package com.leclercb.taskunifier.gui.api.models;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.beans.NoteBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiNoteBean;

public class GuiNote extends Note {
	
	public GuiNote(NoteBean bean, boolean loadReferenceIds) {
		super(bean, loadReferenceIds);
	}
	
	public GuiNote(String title) {
		super(title);
	}
	
	public GuiNote(ModelId modelId, String title) {
		super(modelId, title);
	}
	
	@Override
	public GuiNoteBean toBean() {
		GuiNoteBean bean = (GuiNoteBean) super.toBean();
		
		return bean;
	}
	
}
