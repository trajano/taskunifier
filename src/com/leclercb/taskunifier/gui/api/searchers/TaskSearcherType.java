package com.leclercb.taskunifier.gui.api.searchers;

import com.leclercb.taskunifier.gui.translations.Translations;

public enum TaskSearcherType {
	
	GENERAL(true, true, Translations.getString("searcherlist.general")),
	MODEL(false, false, ""),
	PERSONAL(true, true, Translations.getString("searcherlist.personal"));
	
	private boolean editable;
	private boolean deletable;
	private String label;
	
	private TaskSearcherType(boolean editable, boolean deletable, String label) {
		this.setEditable(editable);
		this.setDeletable(deletable);
		this.setLabel(label);
	}

	public boolean isEditable() {
		return editable;
	}

	private void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isDeletable() {
		return deletable;
	}

	private void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public String getLabel() {
		return label;
	}

	private void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return this.label;
	}
	
}
