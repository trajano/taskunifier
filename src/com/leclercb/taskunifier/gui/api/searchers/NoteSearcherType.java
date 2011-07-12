package com.leclercb.taskunifier.gui.api.searchers;

import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum NoteSearcherType {
	
	DEFAULT(false, false, ""),
	FOLDER(false, false, Translations.getString("general.folders"));
	
	private boolean editable;
	private boolean deletable;
	private String label;
	
	private NoteSearcherType(boolean editable, boolean deletable, String label) {
		this.setEditable(editable);
		this.setDeletable(deletable);
		this.setLabel(label);
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isDeletable() {
		return this.deletable;
	}
	
	private void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
}