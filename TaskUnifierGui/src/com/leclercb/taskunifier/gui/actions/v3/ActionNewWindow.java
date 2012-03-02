package com.leclercb.taskunifier.gui.actions.v3;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.components.views.DefaultCalendarView;
import com.leclercb.taskunifier.gui.components.views.DefaultNoteView;
import com.leclercb.taskunifier.gui.components.views.DefaultTaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
import com.leclercb.taskunifier.gui.main.frame.SubFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionNewWindow extends AbstractAction {
	
	public ActionNewWindow() {
		this(32, 32);
	}
	
	public ActionNewWindow(int width, int height) {
		super(
				Translations.getString("action.new_window"),
				ImageUtils.getResourceImage("window_add.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.new_window"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionNewWindow.newWindow();
	}
	
	public static void newWindow() {
		ViewType type = ViewUtils.getCurrentViewType();
		
		SubFrame subFrame = SubFrame.createSubFrame();
		subFrame.setVisible(true);
		subFrame.requestFocus();
		
		ViewItem viewItem = null;
		
		if (type == ViewType.CALENDAR) {
			viewItem = new ViewItem(
					ViewType.CALENDAR,
					Translations.getString("general.calendar"),
					ImageUtils.getResourceImage("calendar.png", 16, 16),
					subFrame.getFrameId(),
					true);
			viewItem.setView(new DefaultCalendarView(MainFrame.getInstance()));
		} else if (type == ViewType.NOTES) {
			viewItem = new ViewItem(
					ViewType.NOTES,
					Translations.getString("general.notes"),
					ImageUtils.getResourceImage("note.png", 16, 16),
					subFrame.getFrameId(),
					true);
			viewItem.setView(new DefaultNoteView(MainFrame.getInstance()));
		} else if (type == ViewType.TASKS) {
			viewItem = new ViewItem(
					ViewType.TASKS,
					Translations.getString("general.tasks"),
					ImageUtils.getResourceImage("task.png", 16, 16),
					subFrame.getFrameId(),
					true);
			viewItem.setView(new DefaultTaskView(MainFrame.getInstance()));
		}
		
		ViewList.getInstance().addView(viewItem);
		ViewList.getInstance().setCurrentView(viewItem);
	}
	
}
