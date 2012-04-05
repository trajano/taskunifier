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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.calendar.TaskCalendarView;
import com.leclercb.taskunifier.gui.components.calendar.TasksCalendarPanel;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.main.frames.FrameView;

public class DefaultCalendarView extends JPanel implements CalendarView {
	
	private FrameView frameView;
	
	private TasksCalendarPanel calendarPanel;
	
	public DefaultCalendarView(FrameView frameView) {
		CheckUtils.isNotNull(frameView);
		this.frameView = frameView;
		
		this.initialize();
	}
	
	@Override
	public JPanel getViewContent() {
		return this;
	}
	
	@Override
	public TaskSearcherView getTaskSearcherView() {
		return this.calendarPanel.getTaskSearcherView();
	}
	
	@Override
	public TaskCalendarView getTaskCalendarView() {
		return this.calendarPanel;
	}
	
	private void initialize() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		
		this.calendarPanel = new TasksCalendarPanel(this.frameView);
		
		this.add(this.calendarPanel, BorderLayout.CENTER);
		
		ViewList.getInstance().addPropertyChangeListener(
				ViewList.PROP_CURRENT_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (ViewUtils.getCurrentViewType() == ViewType.CALENDAR)
							DefaultCalendarView.this.calendarPanel.refreshTasks();
					}
					
				});
		
		Synchronizing.addPropertyChangeListener(
				Synchronizing.PROP_SYNCHRONIZING,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (!(Boolean) evt.getNewValue())
							DefaultCalendarView.this.calendarPanel.refreshTasks();
					}
					
				});
	}
	
}
