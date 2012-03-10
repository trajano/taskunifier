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
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.api.models.templates.Template;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionAddTemplateTask extends AbstractAction implements PropertyChangeListener {
	
	public static final String ACTION_ADD_TEMPLATE_TASK = "ACTION_ADD_TEMPLATE_TASK";
	
	public static final ActionListener ADD_TASK_LISTENER = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!(e.getSource() instanceof ActionAddTemplateTask))
				return;
			
			ActionAddTemplateTask action = (ActionAddTemplateTask) e.getSource();
			ActionAddTask.addTask(action.getTemplate(), null, true);
		}
	};
	
	private TaskTemplate template;
	private ActionListener listener;
	
	public ActionAddTemplateTask(TaskTemplate template, ActionListener listener) {
		this(template, listener, 32, 32);
	}
	
	public ActionAddTemplateTask(
			TaskTemplate template,
			ActionListener listener,
			int width,
			int height) {
		super(template.getTitle(), ImageUtils.getResourceImage(
				"template.png",
				width,
				height));
		
		CheckUtils.isNotNull(template);
		CheckUtils.isNotNull(listener);
		
		this.template = template;
		this.listener = listener;
		
		this.putValue(SHORT_DESCRIPTION, template.getTitle());
		
		Integer keyEvent = template.getProperties().getIntegerProperty(
				"shortcut");
		
		if (keyEvent != null) {
			this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					keyEvent,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
							+ InputEvent.SHIFT_MASK));
		}
		
		template.addPropertyChangeListener(
				Template.PROP_TITLE,
				new WeakPropertyChangeListener(template, this));
		
		template.getProperties().addPropertyChangeListener(
				"shortcut",
				new WeakPropertyChangeListener(template.getProperties(), this));
	}
	
	public TaskTemplate getTemplate() {
		return this.template;
	}
	
	public ActionListener getListener() {
		return this.listener;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.listener.actionPerformed(new ActionEvent(
				this,
				0,
				ACTION_ADD_TEMPLATE_TASK));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (EqualsUtils.equals(Template.PROP_TITLE, evt.getPropertyName())) {
			ActionAddTemplateTask.this.putValue(
					NAME,
					ActionAddTemplateTask.this.template.getTitle());
			
			ActionAddTemplateTask.this.putValue(
					SHORT_DESCRIPTION,
					ActionAddTemplateTask.this.template.getTitle());
			
			return;
		}
		
		if (EqualsUtils.equals("shortcut", evt.getPropertyName())) {
			Integer keyEvent = ActionAddTemplateTask.this.template.getProperties().getIntegerProperty(
					"shortcut");
			
			if (keyEvent != null) {
				ActionAddTemplateTask.this.putValue(
						ACCELERATOR_KEY,
						KeyStroke.getKeyStroke(
								keyEvent,
								Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
										+ InputEvent.SHIFT_MASK));
			}
			
			return;
		}
	}
	
}
