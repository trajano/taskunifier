package com.leclercb.taskunifier.gui.api.models;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.models.beans.GuiNoteBean;

public class GuiNote extends Note {
	
	public GuiNote(GuiNoteBean bean) {
		super(bean);
	}
	
	public GuiNote(String title) {
		super(title);
	}
	
	public GuiNote(ModelId modelId, String title) {
		super(modelId, title);
	}
	
}
