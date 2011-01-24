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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.template.TemplateFactory;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionAddTemplateTask extends AbstractAction {
	
	public ActionAddTemplateTask() {
		this(32, 32);
	}
	
	public ActionAddTemplateTask(int width, int height) {
		super(
				Translations.getString("action.name.add_template_task"),
				Images.getResourceImage("duplicate.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.add_template_task"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
		
		Task task = TaskFactory.getInstance().create("");
		
		// TODO action.choose_template
		//general.template
		Template template = TemplateFactory.getInstance().getDefaultTemplate();
		
		if (template != null)
			template.applyToTask(task);
		
		MainFrame.getInstance().getTaskView().setSelectedTasks(
				new Task[] { task });
	}
	
}
