package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum ViewType {
	
	NOTES(Translations.getString("general.notes")),
	TASKS(Translations.getString("general.tasks")),
	CALENDAR(Translations.getString("general.calendar")),
	STATISTICS(Translations.getString("general.statistics")), ;
	
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
		TASKS.setView(new DefaultTaskView(mainView));
		CALENDAR.setView(new DefaultCalendarView(mainView));
		STATISTICS.setView(new DefaultStatisticsView(mainView));
	}
	
}
