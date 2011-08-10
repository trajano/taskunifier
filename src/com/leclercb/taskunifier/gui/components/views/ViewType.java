package com.leclercb.taskunifier.gui.components.views;

import java.awt.event.ActionListener;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum ViewType implements ActionSupported {
	
	NOTES(Translations.getString("general.notes"), true),
	TASKS(Translations.getString("general.tasks"), true),
	CALENDAR(Translations.getString("general.calendar"), true),
	STATISTICS(Translations.getString("general.statistics"), false), ;
	
	private static final String ACTION_VIEW_LOADED = "loaded";
	
	public static void initialize(MainView mainView) {
		NOTES.setView(new DefaultNoteView(mainView));
		TASKS.setView(new DefaultTaskView(mainView));
		CALENDAR.setView(new DefaultCalendarView(mainView));
		STATISTICS.setView(new DefaultStatisticsView(mainView));
	}
	
	public static NoteView getNoteView() {
		return (NoteView) NOTES.getView();
	}
	
	public static TaskView getTaskView() {
		return (TaskView) TASKS.getView();
	}
	
	public static CalendarView getCalendarView() {
		return (CalendarView) CALENDAR.getView();
	}
	
	public static StatisticsView getStatisticsView() {
		return (StatisticsView) STATISTICS.getView();
	}
	
	private ActionSupport actionSupport;
	
	private View view;
	private String label;
	private boolean quickChangeView;
	
	private ViewType(String label, boolean quickChangeView) {
		this.actionSupport = new ActionSupport(this);
		
		this.label = label;
		this.quickChangeView = quickChangeView;
	}
	
	public boolean isLoaded() {
		return this.view != null;
	}
	
	public View getView() {
		return this.view;
	}
	
	private void setView(View view) {
		if (this.view != null)
			throw new RuntimeException("View is already loaded");
		
		this.view = view;
		this.actionSupport.fireActionPerformed(0, ACTION_VIEW_LOADED);
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
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}
