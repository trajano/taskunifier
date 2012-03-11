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
package com.leclercb.taskunifier.gui.utils;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionManageTaskTemplates;
import com.leclercb.taskunifier.gui.commons.comparators.TaskTemplateComparator;

public final class TemplateUtils {
	
	private TemplateUtils() {
		
	}
	
	public static void updateTemplateList(ActionListener listener, JMenu menu) {
		updateTemplateList(listener, menu, null);
	}
	
	public static void updateTemplateList(
			ActionListener listener,
			JPopupMenu popupMenu) {
		updateTemplateList(listener, null, popupMenu);
	}
	
	private static void updateTemplateList(
			ActionListener listener,
			JMenu menu,
			JPopupMenu popupMenu) {
		if (menu != null)
			menu.removeAll();
		
		if (popupMenu != null)
			popupMenu.removeAll();
		
		List<TaskTemplate> templates = new ArrayList<TaskTemplate>(
				TaskTemplateFactory.getInstance().getList());
		Collections.sort(templates, TaskTemplateComparator.INSTANCE);
		
		for (TaskTemplate template : templates) {
			if (menu != null)
				menu.add(new ActionAddTemplateTask(16, 16, template, listener));
			
			if (popupMenu != null)
				popupMenu.add(new ActionAddTemplateTask(
						16,
						16,
						template,
						listener));
		}
		
		if (menu != null) {
			menu.addSeparator();
			menu.add(new ActionManageTaskTemplates(16, 16));
		}
		
		if (popupMenu != null) {
			popupMenu.addSeparator();
			popupMenu.add(new ActionManageTaskTemplates(16, 16));
		}
	}
	
}
