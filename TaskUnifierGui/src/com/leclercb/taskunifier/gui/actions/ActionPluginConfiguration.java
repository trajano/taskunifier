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

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.PluginConfigurationDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ActionPluginConfiguration extends AbstractAction {
	
	private SynchronizerGuiPlugin plugin;
	
	public ActionPluginConfiguration() {
		this(null);
	}
	
	public ActionPluginConfiguration(SynchronizerGuiPlugin plugin) {
		this(32, 32, plugin);
	}
	
	public ActionPluginConfiguration(int width, int height) {
		this(width, height, null);
	}
	
	public ActionPluginConfiguration(
			int width,
			int height,
			SynchronizerGuiPlugin plugin) {
		super(
				Translations.getString("action.plugin_configuration"),
				ImageUtils.getResourceImage("settings.png", width, height));
		
		this.plugin = plugin;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.plugin_configuration"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionPluginConfiguration.pluginConfiguration(this.plugin);
	}
	
	public static void pluginConfiguration() {
		pluginConfiguration(null);
	}
	
	public static void pluginConfiguration(SynchronizerGuiPlugin plugin) {
		if (plugin == null)
			plugin = SynchronizerUtils.getSynchronizerPlugin();
		
		PluginConfigurationDialog.getInstance().setPlugin(plugin);
		PluginConfigurationDialog.getInstance().setVisible(true);
	}
	
}
