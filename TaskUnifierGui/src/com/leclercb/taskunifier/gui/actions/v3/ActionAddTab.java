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
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionAddTab extends AbstractAction {
	
	private ViewType type;
	
	public ActionAddTab() {
		this(null, 32, 32);
	}
	
	public ActionAddTab(ViewType type) {
		this(type, 32, 32);
	}
	
	public ActionAddTab(int width, int height) {
		this(null, width, height);
	}
	
	public ActionAddTab(ViewType type, int width, int height) {
		super(
				Translations.getString("action.add_tab"),
				ImageUtils.getResourceImage("tab_add.png", width, height));
		
		this.type = type;
		
		if (type != null) {
			switch (type) {
				case CALENDAR:
					this.putValue(
							NAME,
							Translations.getString("general.calendar"));
					break;
				case NOTES:
					this.putValue(NAME, Translations.getString("general.notes"));
					break;
				case TASKS:
					this.putValue(NAME, Translations.getString("general.tasks"));
					break;
			}
		}
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_tab"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ViewType type = this.type;
		
		if (type == null)
			type = ViewUtils.getCurrentViewType();
		
		ActionAddTab.addTab(type);
	}
	
	public static void addTab(ViewType type) {
		ViewItem viewItem = null;
		
		if (type == ViewType.CALENDAR) {
			viewItem = new ViewItem(
					ViewType.CALENDAR,
					Translations.getString("general.calendar"),
					ImageUtils.getResourceImage("calendar.png", 16, 16),
					ViewList.getInstance().getCurrentView().getFrameId(),
					true);
			viewItem.setView(new DefaultCalendarView(MainFrame.getInstance()));
		} else if (type == ViewType.NOTES) {
			viewItem = new ViewItem(
					ViewType.NOTES,
					Translations.getString("general.notes"),
					ImageUtils.getResourceImage("note.png", 16, 16),
					ViewList.getInstance().getCurrentView().getFrameId(),
					true);
			viewItem.setView(new DefaultNoteView(MainFrame.getInstance()));
		} else if (type == ViewType.TASKS) {
			viewItem = new ViewItem(
					ViewType.TASKS,
					Translations.getString("general.tasks"),
					ImageUtils.getResourceImage("task.png", 16, 16),
					ViewList.getInstance().getCurrentView().getFrameId(),
					true);
			viewItem.setView(new DefaultTaskView(MainFrame.getInstance()));
		}
		
		ViewList.getInstance().addView(viewItem);
		ViewList.getInstance().setCurrentView(viewItem);
	}
	
}
