package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.components.views.statistics.DefaultNoteView;
import com.leclercb.taskunifier.gui.components.views.statistics.DefaultStatisticsView;
import com.leclercb.taskunifier.gui.components.views.statistics.DefaultTaskView;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum ViewType {
	
	NOTES(Translations.getString("general.notes")),
	STATISTICS(Translations.getString("general.statistics")),
	TASKS(Translations.getString("general.tasks"));
	
	private View view;
	private String label;
	
	private ViewType(String label) {
		this.label = label;
	}
	
	public View getView() {
		return this.view;
	}
	
	private void setView(View view) {
		this.view = view;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	public static void initialize(MainView mainView) {
		NOTES.setView(new DefaultNoteView(mainView));
		STATISTICS.setView(new DefaultStatisticsView(mainView));
		TASKS.setView(new DefaultTaskView(mainView));
	}
	
}
