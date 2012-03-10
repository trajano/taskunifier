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
package com.leclercb.taskunifier.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.interfaces.TaskSelectionView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionPostponeTasksMenu extends AbstractViewAction {
	
	private JPopupMenu popupMenu;
	
	public ActionPostponeTasksMenu(TaskSelectionView view) {
		this(32, 32, view);
	}
	
	public ActionPostponeTasksMenu(int width, int height, TaskSelectionView view) {
		super(
				Translations.getString("action.postpone_tasks"),
				ImageUtils.getResourceImage("calendar.png", width, height),
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
		postponeStartDateMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeDueDateMenu.setToolTipText(Translations.getString("general.task.due_date"));
		postponeDueDateMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeBothMenu.setToolTipText(Translations.getString("action.postpone_tasks.both"));
		postponeBothMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		ActionPostponeTasks[] actions = null;
		
		actions = ActionPostponeTasks.createDefaultActions(
				16,
				16,
				view,
				PostponeType.START_DATE);
		for (ActionPostponeTasks action : actions) {
			postponeStartDateMenu.add(action);
		}
		
		actions = ActionPostponeTasks.createDefaultActions(
				16,
				16,
				view,
				PostponeType.DUE_DATE);
		for (ActionPostponeTasks action : actions) {
			postponeDueDateMenu.add(action);
		}
		
		actions = ActionPostponeTasks.createDefaultActions(
				16,
				16,
				view,
				PostponeType.BOTH);
		for (ActionPostponeTasks action : actions) {
			postponeBothMenu.add(action);
		}
		
		this.popupMenu.add(postponeStartDateMenu);
		this.popupMenu.add(postponeDueDateMenu);
		this.popupMenu.add(postponeBothMenu);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component)
			this.popupMenu.show((Component) e.getSource(), 0, 0);
	}
	
}
