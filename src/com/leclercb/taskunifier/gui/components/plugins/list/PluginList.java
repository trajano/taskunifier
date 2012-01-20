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
package com.leclercb.taskunifier.gui.components.plugins.list;

import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;

import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PluginList extends JXList {
	
	public PluginList() {
		this.initialize();
	}
	
	public Plugin[] getPlugins() {
		return ((PluginListModel) this.getModel()).getPlugins();
	}
	
	public void setPlugins(Plugin[] plugins) {
		((PluginListModel) this.getModel()).setPlugins(plugins);
		
		String currentId = SynchronizerUtils.getSynchronizerPlugin().getId();
		Plugin currentPlugin = null;
		for (Plugin plugin : plugins) {
			if (plugin.getId().equals(currentId)) {
				currentPlugin = plugin;
				break;
			}
		}
		
		this.setSelectedValue(currentPlugin, true);
	}
	
	public Plugin getSelectedPlugin() {
		return (Plugin) this.getSelectedValue();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setModel(new PluginListModel());
		this.setCellRenderer(new PluginListRenderer());
	}
	
}
