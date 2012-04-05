/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.views;

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionAddTab;
import com.leclercb.taskunifier.gui.actions.ActionNewWindow;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;

public final class ViewUtils {
	
	private ViewUtils() {
		
	}
	
	public static ViewType getCurrentViewType() {
		if (ViewList.getInstance().getCurrentView() == null)
			return null;
		
		return ViewList.getInstance().getCurrentView().getViewType();
	}
	
	public static void setView(ViewType viewType, boolean create) {
		if (FrameUtils.getCurrentFrameView() == null) {
			ActionNewWindow.newWindow(false);
		}
		
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getFrameId() == FrameUtils.getCurrentFrameView().getFrameId()) {
				if (view.getViewType() == viewType) {
					ViewList.getInstance().setCurrentView(view);
				}
			}
		}
		
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (view.getViewType() == viewType) {
				ViewList.getInstance().setCurrentView(view);
			}
		}
		
		ActionAddTab.addTab(viewType);
	}
	
	public static void setCalendarView(boolean create) {
		setView(ViewType.CALENDAR, create);
	}
	
	public static void setNoteView(boolean create) {
		setView(ViewType.NOTES, create);
	}
	
	public static void setTaskView(boolean create) {
		setView(ViewType.TASKS, create);
	}
	
	public static CalendarView getCurrentCalendarView() {
		if (getCurrentViewType() != ViewType.CALENDAR)
			return null;
		
		return (CalendarView) ViewList.getInstance().getCurrentView().getView();
	}
	
	public static NoteView getCurrentNoteView() {
		if (getCurrentViewType() != ViewType.NOTES)
			return null;
		
		return (NoteView) ViewList.getInstance().getCurrentView().getView();
	}
	
	public static TaskView getCurrentTaskView() {
		if (getCurrentViewType() != ViewType.TASKS)
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
