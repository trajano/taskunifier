package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum ViewType {
	
	NOTES(Translations.getString("general.notes"), true),
	TASKS(Translations.getString("general.tasks"), true),
	CALENDAR(Translations.getString("general.calendar"), true),
	STATISTICS(Translations.getString("general.statistics"), false), ;
	
	private View view;
	private String label;
	private boolean quickChangeView;
	
	private ViewType(String label, boolean quickChangeView) {
		this.label = label;
		this.quickChangeView = quickChangeView;
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
	
	public boolean isQuickChangeView() {
		return this.quickChangeView;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	public static void initialize(MainView mainView) {
		NOTES.setView(new DefaultNoteView(mainView));
		TASKS.setView(new DefaultTaskView(mainView));
		CALENDAR.setView(new DefaultCalendarView(mainView));
		STATISTICS.setView(new DefaultStatisticsView(mainView));
	}
	
}
