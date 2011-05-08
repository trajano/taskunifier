package com.leclercb.taskunifier.gui.main;

import com.leclercb.taskunifier.gui.translations.Translations;

public enum View {
	
	NOTES(Translations.getString("general.notes")),
	TASKS(Translations.getString("general.tasks"));
	
	private String label;
	
	private View(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
}
