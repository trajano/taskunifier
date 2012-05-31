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
package com.leclercb.taskunifier.gui.components.configuration.fields.publication;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PublisherPluginModel extends AbstractTableModel implements ListChangeListener {
	
	private List<SynchronizerGuiPlugin> plugins;
	
	public PublisherPluginModel() {
		this.plugins = new ArrayList<SynchronizerGuiPlugin>();
		this.initialize();
	}
	
	private void initialize() {
		List<SynchronizerGuiPlugin> plugins = Main.getApiPlugins().getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			if (plugin.isPublisher())
				this.plugins.add(plugin);
		}
		
		Main.getApiPlugins().addListChangeListener(
				new WeakListChangeListener(Main.getApiPlugins(), this));
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public int getRowCount() {
		return this.plugins.size();
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return Translations.getString("configuration.publication.plugin.enabled");
			case 1:
				return Translations.getString("configuration.publication.plugin.name");
			case 2:
				return Translations.getString("general.configuration");
			default:
				return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0:
				return Boolean.class;
			case 1:
				return String.class;
			case 2:
				return SynchronizerGuiPlugin.class;
			default:
				return null;
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		SynchronizerGuiPlugin plugin = this.plugins.get(row);
		
		if (col == 0) {
			SynchronizerGuiPlugin[] plugins = SynchronizerUtils.getPublisherPlugins();
			for (SynchronizerGuiPlugin p : plugins) {
				if (plugin.getId().equals(p.getId()))
					return true;
			}
			
			return false;
		}
		
		if (col == 1) {
			return plugin.getName();
		}
		
		if (col == 2) {
			return plugin;
		}
		
		return null;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 1)
			return false;
		
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
			case 0:
				if ((Boolean) value)
					SynchronizerUtils.addPublisherPlugin(this.plugins.get(row));
				else
					SynchronizerUtils.removePublisherPlugin(this.plugins.get(row));
				break;
		}
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) event.getValue();
		
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			if (plugin.isPublisher())
				this.plugins.add(plugin);
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.plugins.remove(plugin);
		}
	}
	
}
