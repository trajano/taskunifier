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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.WeakTaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.components.views.CalendarView;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewTaskSearcherSelectionAction extends AbstractViewAction implements TaskSearcherSelectionListener, PropertyChangeListener {
	
	public AbstractViewTaskSearcherSelectionAction() {
		this(null, null);
	}
	
	public AbstractViewTaskSearcherSelectionAction(String title) {
		this(title, null);
	}
	
	public AbstractViewTaskSearcherSelectionAction(String title, Icon icon) {
		super(title, icon, ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	private void initialize() {
		ViewList.getInstance().addPropertyChangeListener(
				ViewList.PROP_CURRENT_VIEW,
				new WeakPropertyChangeListener(ViewList.getInstance(), this));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event != null && event.getOldValue() != null) {
			ViewItem oldView = (ViewItem) event.getOldValue();
			
			TaskSearcherView view = null;
			
			if (oldView.getViewType() == ViewType.CALENDAR)
				view = ((CalendarView) oldView.getView()).getTaskSearcherView();
			
			if (oldView.getViewType() == ViewType.TASKS)
				view = ((TaskView) oldView.getView()).getTaskSearcherView();
			
			if (view != null)
				view.removeTaskSearcherSelectionChangeListener(this);
		}
		
		if (ViewList.getInstance().getCurrentView() != null
				&& ViewList.getInstance().getCurrentView().isLoaded()) {
			TaskSearcherView view = null;
			
			if (ViewUtils.getCurrentViewType() == ViewType.CALENDAR)
				view = ViewUtils.getCurrentCalendarView().getTaskSearcherView();
			
			if (ViewUtils.getCurrentViewType() == ViewType.TASKS)
				view = ViewUtils.getCurrentTaskView().getTaskSearcherView();
			
			if (view != null)
				view.addTaskSearcherSelectionChangeListener(new WeakTaskSearcherSelectionListener(
						view,
						this));
		}
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled());
	}
	
}
