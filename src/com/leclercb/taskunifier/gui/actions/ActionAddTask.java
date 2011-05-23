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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.View;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ActionAddTask extends AbstractAction {
	
	public ActionAddTask() {
		this(32, 32);
	}
	
	public ActionAddTask(int width, int height) {
		super(
				Translations.getString("action.name.add_task"),
				Images.getResourceImage("document.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.add_task"));
		
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_T,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionAddTask.addTask(TemplateFactory.getInstance().getDefaultTemplate());
	}
	
	public static Task addTask(Template template) {
		MainFrame.getInstance().setSelectedView(View.TASKS);
		
		Template searcherTemplate = MainFrame.getInstance().getSearcherView().getSelectedTaskSearcher().getTemplate();
		
		if (searcherTemplate == null)
			MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
		
		Task task = TaskFactory.getInstance().create("");
		
		if (template != null)
			template.applyToTask(task);
		
		if (searcherTemplate != null)
			searcherTemplate.applyToTask(task);
		
		MainFrame.getInstance().getTaskView().refreshTasks();
		
		if (Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add") != null
				&& Main.SETTINGS.getBooleanProperty("task.show_edit_window_on_add")) {
			TaskEditDialog dialog = new TaskEditDialog(
					task,
					MainFrame.getInstance().getFrame(),
					true);
			dialog.setVisible(true);
			
			if (dialog.isCancelled())
				TaskFactory.getInstance().markDeleted(task);
		} else {
			MainFrame.getInstance().getTaskView().setSelectedTaskAndStartEdit(
					task);
		}
		
		return task;
	}
	
}
