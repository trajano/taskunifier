package com.leclercb.taskunifier.gui.components.traypopup;

import java.awt.MenuItem;
import java.awt.PopupMenu;

import javax.swing.Action;

import com.leclercb.taskunifier.gui.actions.ActionCreateNoteFromClipboard;
import com.leclercb.taskunifier.gui.actions.ActionCreateTaskFromClipboard;
import com.leclercb.taskunifier.gui.components.notes.NoteView;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;

public class TrayPopup extends PopupMenu {
	
	public TrayPopup(TaskView taskView, NoteView noteView) {
		this.initialize(taskView, noteView);
	}
	
	private void initialize(TaskView taskView, NoteView noteView) {
		Action action = null;
		MenuItem item = null;
		
		action = new ActionCreateTaskFromClipboard(taskView);
		item = new MenuItem((String) action.getValue(Action.NAME));
		item.addActionListener(action);
		this.add(item);
		
		action = new ActionCreateNoteFromClipboard(noteView);
		item = new MenuItem((String) action.getValue(Action.NAME));
		item.addActionListener(action);
		this.add(item);
	}
	
}
