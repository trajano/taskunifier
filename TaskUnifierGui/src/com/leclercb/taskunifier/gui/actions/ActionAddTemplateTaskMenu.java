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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.TemplateUtils;

public class ActionAddTemplateTaskMenu extends AbstractAction implements ListChangeListener, PropertyChangeListener {
	
	private ActionListener listener;
	private JPopupMenu popupMenu;
	
	public ActionAddTemplateTaskMenu(
			int width,
			int height,
			ActionListener listener) {
		super(
				Translations.getString("action.add_template_task"),
				ImageUtils.getResourceImage("template.png", width, height));
		
		CheckUtils.isNotNull(listener);
		this.listener = listener;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_template_task"));
		
		this.popupMenu = new JPopupMenu(
				Translations.getString("action.add_template_task"));
		
		TemplateUtils.updateTemplateList(this.listener, this.popupMenu);
		
		TaskTemplateFactory.getInstance().addListChangeListener(
				new WeakListChangeListener(
						TaskTemplateFactory.getInstance(),
						this));
		
		TaskTemplateFactory.getInstance().addPropertyChangeListener(
				BasicModel.PROP_MODEL_STATUS,
				new WeakPropertyChangeListener(
						TaskTemplateFactory.getInstance(),
						this));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component)
			this.popupMenu.show((Component) e.getSource(), 0, 0);
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		TemplateUtils.updateTemplateList(
				this.listener,
				ActionAddTemplateTaskMenu.this.popupMenu);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (((ModelStatus) evt.getOldValue()).isEndUserStatus() != ((ModelStatus) evt.getNewValue()).isEndUserStatus()) {
			TemplateUtils.updateTemplateList(
					this.listener,
					ActionAddTemplateTaskMenu.this.popupMenu);
		}
	}
	
}
