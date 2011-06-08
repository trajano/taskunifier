package com.leclercb.taskunifier.gui.components.traypopup;

import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

import com.leclercb.taskunifier.gui.actions.ActionCreateNoteFromClipboard;
import com.leclercb.taskunifier.gui.actions.ActionCreateTaskFromClipboard;
import com.leclercb.taskunifier.gui.components.notes.NoteView;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TrayPopup extends PopupMenu {
	
	public TrayPopup(Frame frame, TaskView taskView, NoteView noteView) {
		this.initialize(frame, taskView, noteView);
	}
	
	private void initialize(
			final Frame frame,
			final TaskView taskView,
			final NoteView noteView) {
		Action action = null;
		MenuItem item = null;
		
		item = new MenuItem(Translations.getString("general.open"));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				frame.setVisible(true);
				frame.setState(Frame.NORMAL);
			}
			
		});
		this.add(item);
		
		this.addSeparator();
		
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
