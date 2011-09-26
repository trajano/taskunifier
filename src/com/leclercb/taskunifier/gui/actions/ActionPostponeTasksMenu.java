package com.leclercb.taskunifier.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionPostponeTasksMenu extends AbstractViewAction {
	
	private JPopupMenu popupMenu;
	
	public ActionPostponeTasksMenu() {
		this(32, 32);
	}
	
	public ActionPostponeTasksMenu(int width, int height) {
		super(
				Translations.getString("action.postpone_tasks"),
				Images.getResourceImage("calendar.png", width, height),
				ViewType.TASKS,
				ViewType.CALENDAR);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.postpone_tasks"));
		
		this.popupMenu = new JPopupMenu();
		
		final JMenu postponeStartDateMenu = new JMenu(
				Translations.getString("general.task.start_date"));
		final JMenu postponeDueDateMenu = new JMenu(
				Translations.getString("general.task.due_date"));
		final JMenu postponeBothMenu = new JMenu(
				Translations.getString("action.postpone_tasks.both"));
		
		postponeStartDateMenu.setToolTipText(Translations.getString("general.task.start_date"));
		postponeStartDateMenu.setIcon(Images.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeDueDateMenu.setToolTipText(Translations.getString("general.task.due_date"));
		postponeDueDateMenu.setIcon(Images.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeBothMenu.setToolTipText(Translations.getString("action.postpone_tasks.both"));
		postponeBothMenu.setIcon(Images.getResourceImage("calendar.png", 16, 16));
		
		ActionPostponeTasks[] actions = null;
		
		actions = ActionPostponeTasks.createDefaultActions(
				PostponeType.START_DATE,
				16,
				16);
		for (ActionPostponeTasks action : actions) {
			postponeStartDateMenu.add(action);
		}
		
		actions = ActionPostponeTasks.createDefaultActions(
				PostponeType.DUE_DATE,
				16,
				16);
		for (ActionPostponeTasks action : actions) {
			postponeDueDateMenu.add(action);
		}
		
		actions = ActionPostponeTasks.createDefaultActions(
				PostponeType.BOTH,
				16,
				16);
		for (ActionPostponeTasks action : actions) {
			postponeBothMenu.add(action);
		}
		
		this.popupMenu.add(postponeStartDateMenu);
		this.popupMenu.add(postponeDueDateMenu);
		this.popupMenu.add(postponeBothMenu);
		
		this.setEnabled(this.shouldBeEnabled());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component)
			this.popupMenu.show((Component) e.getSource(), 0, 0);
	}
	
}
