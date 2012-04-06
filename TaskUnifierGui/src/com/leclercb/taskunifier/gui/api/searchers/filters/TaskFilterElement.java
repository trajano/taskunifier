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
package com.leclercb.taskunifier.gui.api.searchers.filters;

import java.util.Calendar;

import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.Condition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.DaysCondition;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class TaskFilterElement extends FilterElement<Task, TaskColumn, TaskFilter> implements Cloneable {
	
	public TaskFilterElement(
			TaskColumn property,
			Condition<?, ?> condition,
			Object value) {
		super(property, condition, value);
	}
	
	@Override
	public TaskFilterElement clone() {
		return new TaskFilterElement(
				this.getProperty(),
				this.getCondition(),
				this.getValue());
	}
	
	@Override
	public String toString() {
		String str = this.getProperty()
				+ " "
				+ TranslationsUtils.translateFilterCondition(this.getCondition())
				+ " \"";
		
		switch (this.getProperty()) {
			case SHOW_CHILDREN:
			case COMPLETED:
			case STAR:
				str += TranslationsUtils.translateBoolean(Boolean.parseBoolean(this.getValue().toString()));
				break;
			case PRIORITY:
				str += TranslationsUtils.translateTaskPriority((TaskPriority) this.getValue());
				break;
			case REPEAT_FROM:
				str += TranslationsUtils.translateTaskRepeatFrom((TaskRepeatFrom) this.getValue());
				break;
			default:
				str += (this.getValue() == null ? "" : this.getValue());
				break;
		}
		
		if (this.getValue() != null
				&& this.getCondition() instanceof DaysCondition) {
			try {
				if (this.getCondition() == DaysCondition.WEEK_EQUALS
						|| this.getCondition() == DaysCondition.WEEK_NOT_EQUALS) {
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					
					c1.add(Calendar.WEEK_OF_YEAR, (Integer) this.getValue());
					c2.add(Calendar.WEEK_OF_YEAR, (Integer) this.getValue());
					
					DateUtils.goToFirstDayOfWeek(c1);
					DateUtils.goToLastDayOfWeek(c2);
					
					str += String.format(
							" (%1s - %2s)",
							StringValueCalendar.INSTANCE_DATE.getString(c1),
							StringValueCalendar.INSTANCE_DATE.getString(c2));
				} else if (this.getCondition() == DaysCondition.MONTH_EQUALS
						|| this.getCondition() == DaysCondition.MONTH_NOT_EQUALS) {
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					
					c1.add(Calendar.MONTH, (Integer) this.getValue());
					c2.add(Calendar.MONTH, (Integer) this.getValue());
					
					DateUtils.goToFirstDayOfMonth(c1);
					DateUtils.goToLastDayOfMonth(c2);
					
					str += String.format(
							" (%1s - %2s)",
							StringValueCalendar.INSTANCE_DATE.getString(c1),
							StringValueCalendar.INSTANCE_DATE.getString(c2));
				} else {
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_MONTH, (Integer) this.getValue());
					
					if (this.getCondition() == DaysCondition.LESS_THAN_USING_TIME
							|| this.getCondition() == DaysCondition.GREATER_THAN_USING_TIME)
						str += " ("
								+ StringValueCalendar.INSTANCE_DATE_TIME.getString(c)
								+ ")";
					else
						str += " ("
								+ StringValueCalendar.INSTANCE_DATE.getString(c)
								+ ")";
				}
			} catch (Throwable t) {
				
			}
		}
		
		return str + "\"";
	}
	
}
