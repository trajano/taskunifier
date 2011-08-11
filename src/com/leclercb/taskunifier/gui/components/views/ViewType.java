package com.leclercb.taskunifier.gui.components.views;

import java.awt.event.ActionListener;
import java.util.logging.Level;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum ViewType implements ActionSupported {
	
	NOTES(Translations.getString("general.notes")),
	TASKS(Translations.getString("general.tasks")),
	CALENDAR(Translations.getString("general.calendar")),
	STATISTICS(Translations.getString("general.statistics")), ;
	
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
	
	private ViewType(String label) {
		this.actionSupport = new ActionSupport(this);
		
		this.label = label;
	}
	
	public boolean isLoaded() {
		return this.view != null;
	}
	
	public View getView() {
		if (!this.isLoaded())
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"View \"" + this.name() + "\" is not loaded yet",
					new Throwable().getStackTrace());
		
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
