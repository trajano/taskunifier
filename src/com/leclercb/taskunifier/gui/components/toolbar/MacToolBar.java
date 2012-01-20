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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.UnifiedToolBar;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTaskMenu;
import com.leclercb.taskunifier.gui.actions.ActionChangeView;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionList;
import com.leclercb.taskunifier.gui.actions.ActionScheduledSync;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class MacToolBar extends UnifiedToolBar {
	
	public MacToolBar() {
		this.initialize();
	}
	
	private void initialize() {
		this.initializeActions();
		
		final JLabel accountLabel = MacWidgetFactory.createEmphasizedLabel("");
		accountLabel.setText(this.getAccountLabelText());
		
		Main.getUserSettings().addSavePropertiesListener(
				new SavePropertiesListener() {
					
					@Override
					public void saveProperties() {
						accountLabel.setText(MacToolBar.this.getAccountLabelText());
					}
					
				});
		
		Main.getUserSettings().addPropertyChangeListener(
				"general.user.name",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						accountLabel.setText(MacToolBar.this.getAccountLabelText());
					}
					
				});
		
		Main.getUserSettings().addPropertyChangeListener(
				"api.id",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						accountLabel.setText(MacToolBar.this.getAccountLabelText());
					}
					
				});
		
		this.addComponentToRight(accountLabel);
	}
	
	private String getAccountLabelText() {
		String user = Main.getUserSettings().getStringProperty(
				"general.user.name");
		String account = SynchronizerUtils.getSynchronizerPlugin().getAccountLabel();
		
		if (user == null)
			user = "";
		
		if (account == null)
			account = "";
		
		if (user.length() == 0 && account.length() == 0)
			return "";
		
		if (user.length() == 0)
			return account;
		
		if (account.length() == 0)
			return user;
		
		return user + " - " + account;
	}
	
	private void initializeActions() {
		try {
			boolean added = false;
			String value = Main.getSettings().getStringProperty(
					"general.toolbar");
			
			if (value == null)
				throw new Exception();
			
			String[] actions = StringUtils.split(value, ';');
			for (String action : actions) {
				action = action.trim();
				
				if ("SEPARATOR".equalsIgnoreCase(action)) {
					this.addComponentToLeft(new JSeparator());
					continue;
				}
				
				try {
					ActionList l = ActionList.valueOf(action);
					
					if (!l.isFitToolBar())
						continue;
					
					Action a = l.newInstance(24, 24);
					
					if (a == null)
						continue;
					
					this.addComponentToLeft(this.createButton(a));
					added = true;
				} catch (Throwable t) {
					GuiLogger.getLogger().warning(
							"Cannot add action \"" + action + "\" to toolbar");
				}
			}
			
			if (!added)
				throw new Exception();
		} catch (Throwable t) {
			this.addComponentToLeft(this.createButton(new ActionChangeView(
					24,
					24)));
			this.addComponentToLeft(new JSeparator());
			this.addComponentToLeft(this.createButton(new ActionAddNote(24, 24)));
			this.addComponentToLeft(this.createButton(new ActionAddTask(24, 24)));
			this.addComponentToLeft(this.createButton(new ActionAddSubTask(
					24,
					24)));
			this.addComponentToLeft(this.createButton(new ActionAddTemplateTaskMenu(
					ActionAddTemplateTask.ADD_TASK_LISTENER,
					24,
					24)));
			this.addComponentToLeft(this.createButton(new ActionDelete(24, 24)));
			this.addComponentToLeft(new JSeparator());
			this.addComponentToLeft(this.createButton(new ActionSynchronize(
					false,
					24,
					24)));
			this.addComponentToLeft(this.createButton(new ActionScheduledSync(
					24,
					24)));
			this.addComponentToLeft(new JSeparator());
			this.addComponentToLeft(this.createButton(new ActionConfiguration(
					24,
					24)));
		}
	}
	
	private void formatButton(JButton button) {
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		
		// String text = button.getText() == null ? "" : button.getText();
		// text = text.length() > 30 ? text.substring(0, 30 - 2) + "..." : text;
		
		button.setText("");
	}
	
	private JButton createButton(Action action) {
		JButton button = new JButton(action);
		this.formatButton(button);
		return button;
	}
	
}
