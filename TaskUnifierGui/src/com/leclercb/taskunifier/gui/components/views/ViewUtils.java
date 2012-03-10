package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;

public final class ViewUtils {
	
	private ViewUtils() {
		
	}
	
	public static ViewType getCurrentViewType() {
		return ViewList.getInstance().getCurrentView().getViewType();
	}
	
	public static CalendarView getMainCalendarView() {
		return (CalendarView) ViewList.getInstance().getMainCalendarView().getView();
	}
	
	public static NoteView getMainNoteView() {
		return (NoteView) ViewList.getInstance().getMainNoteView().getView();
	}
	
	public static TaskView getMainTaskView() {
		return (TaskView) ViewList.getInstance().getMainTaskView().getView();
	}
	
	public static void setMainCalendarView() {
		ViewList.getInstance().setCurrentView(
				ViewList.getInstance().getMainCalendarView());
	}
	
	public static void setMainNoteView() {
		ViewList.getInstance().setCurrentView(
				ViewList.getInstance().getMainNoteView());
	}
	
	public static void setMainTaskView() {
		ViewList.getInstance().setCurrentView(
				ViewList.getInstance().getMainTaskView());
	}
	
	public static CalendarView getCurrentCalendarView() {
		if (ViewList.getInstance().getCurrentView().getViewType() != ViewType.CALENDAR)
			return null;
		
		return (CalendarView) ViewList.getInstance().getCurrentView().getView();
	}
	
	public static NoteView getCurrentNoteView() {
		if (ViewList.getInstance().getCurrentView().getViewType() != ViewType.NOTES)
			return null;
		
		return (NoteView) ViewList.getInstance().getCurrentView().getView();
	}
	
	public static TaskView getCurrentTaskView() {
		if (ViewList.getInstance().getCurrentView().getViewType() != ViewType.TASKS)
			return null;
		
		return (TaskView) ViewList.getInstance().getCurrentView().getView();
	}
	
	public static void commitAll() {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getViewType() == ViewType.NOTES) {
				((NoteView) view.getView()).getNoteTableView().commitChanges();
			}
			
			if (view.getViewType() == ViewType.TASKS) {
				((TaskView) view.getView()).getTaskTableView().commitChanges();
			}
		}
	}
	
	public static Note[] getSelectedNotes() {
		if (getCurrentNoteView() != null)
			return getCurrentNoteView().getNoteTableView().getSelectedNotes();
		
		return new Note[0];
	}
	
	public static Task[] getSelectedTasks() {
		if (getCurrentCalendarView() != null)
			return getCurrentCalendarView().getTaskCalendarView().getSelectedTasks();
		
		if (getCurrentTaskView() != null)
			return getCurrentTaskView().getTaskTableView().getSelectedTasks();
		
		return new Task[0];
	}
	
	public static NoteSearcher getSelectedNoteSearcher() {
		if (getCurrentNoteView() != null)
			return getCurrentNoteView().getNoteSearcherView().getSelectedNoteSearcher();
		
		return null;
	}
	
	public static NoteSearcher getSelectedOriginalNoteSearcher() {
		if (getCurrentNoteView() != null)
			return getCurrentNoteView().getNoteSearcherView().getSelectedOriginalNoteSearcher();
		
		return null;
	}
	
	public static TaskSearcher getSelectedTaskSearcher() {
		if (getCurrentCalendarView() != null)
			return getCurrentCalendarView().getTaskSearcherView().getSelectedTaskSearcher();
		
		if (getCurrentTaskView() != null)
			return getCurrentTaskView().getTaskSearcherView().getSelectedTaskSearcher();
		
		return null;
	}
	
	public static TaskSearcher getSelectedOriginalTaskSearcher() {
		if (getCurrentCalendarView() != null)
			return getCurrentCalendarView().getTaskSearcherView().getSelectedOriginalTaskSearcher();
		
		if (getCurrentTaskView() != null)
			return getCurrentTaskView().getTaskSearcherView().getSelectedOriginalTaskSearcher();
		
		return null;
	}
	
	public static void setSelectedNotes(Note[] notes) {
		if (getCurrentNoteView() != null)
			getCurrentNoteView().getNoteTableView().setSelectedNotes(notes);
	}
	
	public static void setSelectedTasks(Task[] tasks) {
		if (getCurrentTaskView() != null)
			getCurrentTaskView().getTaskTableView().setSelectedTasks(tasks);
	}
	
	public static void selectDefaultNoteSearcher() {
		if (getCurrentNoteView() != null)
			getCurrentNoteView().getNoteSearcherView().selectDefaultNoteSearcher();
	}
	
	public static void selectDefaultTaskSearcher() {
		if (getCurrentCalendarView() != null)
			getCurrentCalendarView().getTaskSearcherView().selectDefaultTaskSearcher();
		
		if (getCurrentTaskView() != null)
			getCurrentTaskView().getTaskSearcherView().selectDefaultTaskSearcher();
	}
	
	public static void addExtraNotes(Note... notes) {
		if (getCurrentNoteView() != null)
			getCurrentNoteView().getNoteSearcherView().addExtraNotes(notes);
	}
	
	public static void setExtraNotes(Note... notes) {
		if (getCurrentNoteView() != null)
			getCurrentNoteView().getNoteSearcherView().setExtraNotes(notes);
	}
	
	public static void addExtraTasks(Task... tasks) {
		if (getCurrentCalendarView() != null)
			getCurrentCalendarView().getTaskSearcherView().addExtraTasks(tasks);
		
		if (getCurrentTaskView() != null)
			getCurrentTaskView().getTaskSearcherView().addExtraTasks(tasks);
	}
	
	public static void setExtraTasks(Task... tasks) {
		if (getCurrentCalendarView() != null)
			getCurrentCalendarView().getTaskSearcherView().setExtraTasks(tasks);
		
		if (getCurrentTaskView() != null)
			getCurrentTaskView().getTaskSearcherView().setExtraTasks(tasks);
	}
	
	public static void setSearchFilter(String filter) {
		if (getCurrentNoteView() != null)
			getCurrentNoteView().getNoteSearcherView().setSearchFilter(filter);
		
		if (getCurrentCalendarView() != null)
			getCurrentCalendarView().getTaskSearcherView().setSearchFilter(
					filter);
		
		if (getCurrentTaskView() != null)
			getCurrentTaskView().getTaskSearcherView().setSearchFilter(filter);
	}
	
	public static void refreshNotes() {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getViewType() == ViewType.NOTES) {
				((NoteView) view.getView()).getNoteTableView().refreshNotes();
			}
		}
	}
	
	public static void refreshTasks() {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getViewType() == ViewType.CALENDAR) {
				((CalendarView) view.getView()).getTaskCalendarView().refreshTasks();
			}
			
			if (view.getViewType() == ViewType.TASKS) {
				((TaskView) view.getView()).getTaskTableView().refreshTasks();
			}
		}
	}
	
	public static void refreshNoteSearcher() {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getViewType() == ViewType.NOTES) {
				((NoteView) view.getView()).getNoteSearcherView().refreshNoteSearcher();
			}
		}
	}
	
	public static void refreshTaskSearcher() {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getViewType() == ViewType.CALENDAR) {
				((CalendarView) view.getView()).getTaskSearcherView().refreshTaskSearcher();
			}
			
			if (view.getViewType() == ViewType.TASKS) {
				((TaskView) view.getView()).getTaskSearcherView().refreshTaskSearcher();
			}
		}
	}
	
}
