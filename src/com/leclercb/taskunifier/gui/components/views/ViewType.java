package com.leclercb.taskunifier.gui.components.views;

import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.Icon;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public enum ViewType implements ActionSupported {
	
	TASKS(Translations.getString("general.tasks"), ImageUtils.getResourceImage(
			"task.png",
			16,
			16)),
	NOTES(Translations.getString("general.notes"), ImageUtils.getResourceImage(
			"note.png",
			16,
			16)),
	CALENDAR(Translations.getString("general.calendar"), ImageUtils.getResourceImage(
			"calendar.png",
			16,
			16));
	
	private static final String ACTION_VIEW_LOADED = "loaded";
	
	public static void initialize(MainView mainView) {
		NOTES.setView(new DefaultNoteView(mainView));
		TASKS.setView(new DefaultTaskView(mainView));
		CALENDAR.setView(new DefaultCalendarView(mainView));
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
	
	public static void commitAll() {
		ViewType.getNoteView().getNoteTableView().commitChanges();
		ViewType.getNoteView().getModelNoteView().view();
		
		ViewType.getTaskView().getTaskTableView().commitChanges();
		ViewType.getTaskView().getModelNoteView().view();
	}
	
	public static Note[] getSelectedNotes() {
		return ViewType.getNoteView().getNoteTableView().getSelectedNotes();
	}
	
	public static Task[] getSelectedTasks() {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			return ViewType.getTaskView().getTaskTableView().getSelectedTasks();
		else if (viewType == ViewType.CALENDAR)
			return ViewType.getCalendarView().getTaskCalendarView().getSelectedTasks();
		
		return null;
	}
	
	public static TaskSearcher getSelectedTaskSearcher() {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			return ViewType.getTaskView().getTaskSearcherView().getSelectedTaskSearcher();
		else if (viewType == ViewType.CALENDAR)
			return ViewType.getCalendarView().getTaskSearcherView().getSelectedTaskSearcher();
		
		return null;
	}
	
	public static TaskSearcher getSelectedOriginalTaskSearcher() {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			return ViewType.getTaskView().getTaskSearcherView().getSelectedOriginalTaskSearcher();
		else if (viewType == ViewType.CALENDAR)
			return ViewType.getCalendarView().getTaskSearcherView().getSelectedOriginalTaskSearcher();
		
		return null;
	}
	
	public static void setSelectedTasks(Task[] tasks) {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskTableView().setSelectedTasks(tasks);
	}
	
	public static void selectDefaultTaskSearcher() {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskSearcherView().selectDefaultTaskSearcher();
		else if (viewType == ViewType.CALENDAR)
			ViewType.getCalendarView().getTaskSearcherView().selectDefaultTaskSearcher();
	}
	
	public static void addExtraTasks(Task[] tasks) {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskSearcherView().addExtraTasks(tasks);
		else if (viewType == ViewType.CALENDAR)
			ViewType.getCalendarView().getTaskSearcherView().addExtraTasks(
					tasks);
	}
	
	public static void setExtraTasks(Task[] tasks) {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskSearcherView().setExtraTasks(tasks);
		else if (viewType == ViewType.CALENDAR)
			ViewType.getCalendarView().getTaskSearcherView().setExtraTasks(
					tasks);
	}
	
	public static void setSearchFilter(String filter) {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskSearcherView().setSearchFilter(filter);
		else if (viewType == ViewType.CALENDAR)
			ViewType.getCalendarView().getTaskSearcherView().setSearchFilter(
					filter);
		else if (viewType == ViewType.NOTES)
			ViewType.getNoteView().getNoteSearcherView().setSearchFilter(filter);
	}
	
	public static void refreshTasks() {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskTableView().refreshTasks();
		else if (viewType == ViewType.CALENDAR)
			ViewType.getCalendarView().getTaskCalendarView().refreshTasks();
	}
	
	public static void refreshTaskSearcher() {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			ViewType.getTaskView().getTaskSearcherView().refreshTaskSearcher();
		else if (viewType == ViewType.CALENDAR)
			ViewType.getCalendarView().getTaskSearcherView().refreshTaskSearcher();
	}
	
	public static boolean shouldBeDisplayed(Task task) {
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		
		if (viewType == ViewType.TASKS)
			return ViewType.getTaskView().getTaskTableView().shouldBeDisplayed(
					task);
		else if (viewType == ViewType.CALENDAR)
			return ViewType.getCalendarView().getTaskCalendarView().shouldBeDisplayed(
					task);
		
		return false;
	}
	
	private ActionSupport actionSupport;
	
	private View view;
	private String label;
	private Icon icon;
	
	private ViewType(String label, Icon icon) {
		this.actionSupport = new ActionSupport(this);
		
		this.view = null;
		this.label = label;
		this.icon = icon;
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
	
	public Icon getIcon() {
		return this.icon;
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
