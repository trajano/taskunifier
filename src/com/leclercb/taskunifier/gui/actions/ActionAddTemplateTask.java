/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.template.Template;

public class ActionAddTemplateTask extends AbstractAction {
	
	private Template template;
	
	public ActionAddTemplateTask(Template template) {
		this(template, 32, 32);
	}
	
	public ActionAddTemplateTask(Template template, int width, int height) {
		super(template.getTitle(), Images.getResourceImage(
				"duplicate.png",
				width,
				height));
		
		CheckUtils.isNotNull(template, "Template cannot be null");
		
		this.template = template;
		
		template.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(Template.PROP_TITLE)) {
					ActionAddTemplateTask.this.putValue(
							NAME,
							ActionAddTemplateTask.this.template.getTitle());
				}
			}
			
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
		
		Task task = TaskFactory.getInstance().create("");
		
		this.template.applyToTask(task);
		
		MainFrame.getInstance().getTaskView().setSelectedTasks(
				new Task[] { task });
	}
	
}
