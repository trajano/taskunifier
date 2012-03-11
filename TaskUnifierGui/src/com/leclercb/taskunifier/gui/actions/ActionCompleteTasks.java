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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionCompleteTasks extends AbstractViewTaskSelectionAction {
	
	public ActionCompleteTasks(int width, int height) {
		super(
				Translations.getString("action.complete_tasks"),
				ImageUtils.getResourceImage("check.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.complete_tasks"));
		
		this.putValue(
				ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
	}
	
	@Override
	public boolean shouldBeEnabled() {
		if (!super.shouldBeEnabled())
			return false;
		
		return ViewUtils.getSelectedTasks().length != 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionCompleteTasks.completeTasks(ViewUtils.getSelectedTasks());
	}
	
	public static void completeTasks(Task[] tasks) {
		CheckUtils.isNotNull(tasks);
		
		boolean completed = true;
		boolean allTrueOrFalse = true;
		
		for (int i = 0; i < tasks.length; i++) {
			if (i == 0) {
				completed = tasks[i].isCompleted();
				continue;
			}
			
			if (tasks[i - 1].isCompleted() != tasks[i].isCompleted()) {
				allTrueOrFalse = false;
				break;
			}
		}
		
		if (allTrueOrFalse)
			completed = !completed;
		else
			completed = true;
		
		for (Task task : tasks) {
			task.setCompleted(completed);
		}
		
		ViewUtils.refreshTasks();
	}
	
}
