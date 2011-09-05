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
package com.leclercb.taskunifier.gui.components.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionChangeView;
import com.leclercb.taskunifier.gui.actions.ActionChangeViewCalendar;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionScheduledSync;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.TemplateUtils;

public class DefaultToolBar extends JToolBar {
	
	public DefaultToolBar() {
		this.initialize();
	}
	
	private void initialize() {
		this.setFloatable(false);
		this.setRollover(true);
		
		this.add(new ActionChangeView(24, 24));
		this.add(new ActionChangeViewCalendar(24, 24));
		this.addSeparator(new Dimension(20, 20));
		this.add(new ActionAddNote(24, 24));
		this.add(new ActionAddTask(24, 24));
		this.add(new ActionAddSubTask(24, 24));
		this.initializeTemplates();
		this.add(new ActionDelete(24, 24));
		this.addSeparator(new Dimension(20, 20));
		this.add(new ActionSynchronize(false, 24, 24));
		this.add(new ActionScheduledSync(24, 24));
		this.addSeparator(new Dimension(20, 20));
		this.add(new ActionConfiguration(24, 24));
		
		this.add(Box.createHorizontalGlue());
		
		final JLabel accountLabel = new JLabel();
		accountLabel.setText(SynchronizerUtils.getPlugin().getAccountLabel());
		
		Main.SETTINGS.addSavePropertiesListener(new SavePropertiesListener() {
			
			@Override
			public void saveProperties() {
				accountLabel.setText(SynchronizerUtils.getPlugin().getAccountLabel());
			}
			
		});
		
		this.add(accountLabel);
		
		this.add(Box.createHorizontalStrut(10));
	}
	
	private void initializeTemplates() {
		final JPopupMenu popupMenu = new JPopupMenu(
				Translations.getString("action.add_template_task"));
		
		TemplateUtils.updateTemplateList(null, popupMenu);
		
		TaskTemplateFactory.getInstance().addListChangeListener(
				new ListChangeListener() {
					
					@Override
					public void listChange(ListChangeEvent event) {
						TemplateUtils.updateTemplateList(null, popupMenu);
					}
					
				});
		
		final JButton addTemplateTaskButton = new JButton();
		
		Action actionAddTemplateTask = new AbstractAction() {
			
			{
				this.putValue(
						SHORT_DESCRIPTION,
						Translations.getString("action.add_template_task"));
				
				this.putValue(
						SMALL_ICON,
						Images.getResourceImage("duplicate.png", 24, 24));
			}
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				popupMenu.show(
						addTemplateTaskButton,
						addTemplateTaskButton.getX(),
						addTemplateTaskButton.getY());
			}
			
		};
		
		addTemplateTaskButton.setAction(actionAddTemplateTask);
		
		this.add(addTemplateTaskButton);
	}
	
}
