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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionPostponeTaskBeans extends AbstractAction {
	
	private ActionListener listener;
	
	private PostponeType type;
	private int field;
	private int amount;
	
	public ActionPostponeTaskBeans(
			ActionListener listener,
			String title,
			PostponeType type,
			int field,
			int amount) {
		this(listener, title, type, field, amount, 32, 32);
	}
	
	public ActionPostponeTaskBeans(
			ActionListener listener,
			String title,
			PostponeType type,
			int field,
			int amount,
			int width,
			int height) {
		super(title, ImageUtils.getResourceImage("calendar.png", width, height));
		
		CheckUtils.isNotNull(listener);
		CheckUtils.isNotNull(type);
		
		this.listener = listener;
		this.type = type;
		this.field = field;
		this.amount = amount;
		
		this.putValue(SHORT_DESCRIPTION, title);
	}
	
	public PostponeType getType() {
		return this.type;
	}
	
	public int getField() {
		return this.field;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public void postponeTaskBeans(TaskBean[] tasks) {
		postponeTaskBeans(tasks, this.type, this.field, this.amount);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		e.setSource(this);
		this.listener.actionPerformed(e);
	}
	
	public static void postponeTaskBeans(
			TaskBean[] tasks,
			PostponeType type,
			int field,
			int amount) {
		CheckUtils.isNotNull(tasks);
		CheckUtils.isNotNull(type);
		
		boolean fromCurrentDate = Main.getSettings().getBooleanProperty(
				"task.postpone_from_current_date");
		
		if (type == PostponeType.BOTH)
			fromCurrentDate = false;
		
		if (type == PostponeType.START_DATE || type == PostponeType.BOTH) {
			for (TaskBean task : tasks) {
				Calendar newStartDate = task.getStartDate();
				
				if (newStartDate == null)
					newStartDate = Calendar.getInstance();
				
				if (fromCurrentDate
						|| (field == Calendar.DAY_OF_MONTH && amount == 0)) {
					Calendar now = Calendar.getInstance();
					newStartDate.set(
							now.get(Calendar.YEAR),
							now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH));
				}
				
				newStartDate.add(field, amount);
				
				task.setStartDate(newStartDate);
			}
		}
		
		if (type == PostponeType.DUE_DATE || type == PostponeType.BOTH) {
			for (TaskBean task : tasks) {
				Calendar newDueDate = task.getDueDate();
				
				if (newDueDate == null)
					newDueDate = Calendar.getInstance();
				
				if (fromCurrentDate
						|| (field == Calendar.DAY_OF_MONTH && amount == 0)) {
					Calendar now = Calendar.getInstance();
					newDueDate.set(
							now.get(Calendar.YEAR),
							now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH));
				}
				
				newDueDate.add(field, amount);
				
				task.setDueDate(newDueDate);
			}
		}
		
		ViewUtils.refreshTasks();
	}
	
	public static ActionPostponeTaskBeans[] createDefaultActions(
			ActionListener listener,
			PostponeType type,
			int width,
			int height) {
		List<ActionPostponeTaskBeans> actions = new ArrayList<ActionPostponeTaskBeans>();
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.today"),
				type,
				Calendar.DAY_OF_MONTH,
				0,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.1_day"),
				type,
				Calendar.DAY_OF_MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.x_days", 2),
				type,
				Calendar.DAY_OF_MONTH,
				2,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.x_days", 3),
				type,
				Calendar.DAY_OF_MONTH,
				3,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.1_week"),
				type,
				Calendar.WEEK_OF_YEAR,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.x_weeks", 2),
				type,
				Calendar.WEEK_OF_YEAR,
				2,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.x_weeks", 3),
				type,
				Calendar.WEEK_OF_YEAR,
				3,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.1_month"),
				type,
				Calendar.MONTH,
				1,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.x_months", 2),
				type,
				Calendar.MONTH,
				2,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.x_months", 3),
				type,
				Calendar.MONTH,
				3,
				width,
				height));
		
		actions.add(new ActionPostponeTaskBeans(
				listener,
				Translations.getString("postpone.1_year"),
				type,
				Calendar.YEAR,
				1,
				width,
				height));
		
		return actions.toArray(new ActionPostponeTaskBeans[0]);
	}
	
}

