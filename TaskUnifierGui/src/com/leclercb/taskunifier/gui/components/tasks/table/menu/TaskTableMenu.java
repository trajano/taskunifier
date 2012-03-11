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
package com.leclercb.taskunifier.gui.components.tasks.table.menu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTaskAtSameLevel;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionCollapseAll;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionDuplicateTasks;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.actions.ActionExpandAll;
import com.leclercb.taskunifier.gui.actions.ActionMailTo;
import com.leclercb.taskunifier.gui.actions.ActionPrintSelectedModels;
import com.leclercb.taskunifier.gui.actions.ActionRefresh;
import com.leclercb.taskunifier.gui.actions.ActionSelectParentTasks;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TaskTableMenu extends JPopupMenu {
	
	public TaskTableMenu() {
		super(Translations.getString("general.task"));
		
		this.initialize();
	}
	
	private void initialize() {
		this.add(new ActionEditTasks(16, 16));
		this.add(ComponentFactory.createPostponeMenu());
		this.addSeparator();
		this.add(new ActionAddTask(16, 16));
		this.add(new ActionAddSubTask(16, 16));
		this.add(new ActionAddSubTaskAtSameLevel(16, 16));
		this.add(new ActionDuplicateTasks(16, 16));
		this.addSeparator();
		this.add(new ActionRefresh(16, 16));
		this.addSeparator();
		this.add(new JMenuItem(new ActionCollapseAll(16, 16)));
		this.add(new JMenuItem(new ActionExpandAll(16, 16)));
		this.add(new JMenuItem(new ActionSelectParentTasks(16, 16)));
		this.addSeparator();
		this.add(new ActionMailTo(16, 16));
		this.add(new ActionPrintSelectedModels(16, 16));
		this.addSeparator();
		this.add(new ActionDelete(16, 16));
	}
	
}
