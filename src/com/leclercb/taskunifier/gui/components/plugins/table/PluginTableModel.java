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
package com.leclercb.taskunifier.gui.components.plugins.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.plugins.Plugin;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginTableModel extends AbstractTableModel implements PropertyChangeListener {
	
	private Plugin[] plugins;
	
	public PluginTableModel(Plugin[] plugins) {
		CheckUtils.isNotNull(plugins, "Plugins cannot be null");
		this.plugins = plugins;
		
		for (Plugin plugin : plugins) {
			plugin.addPropertyChangeListener(this);
		}
	}
	
	public Plugin getPlugin(int row) {
		return this.plugins[row];
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
			case 0:
				return Translations.getString("plugin.status");
			case 1:
				return Translations.getString("plugin.name");
			case 2:
				return Translations.getString("plugin.author");
			case 3:
				return Translations.getString("plugin.version");
			case 4:
				return Translations.getString("plugin.service_provider");
			default:
				return null;
		}
	}
	
	@Override
	public int getColumnCount() {
		return 5;
	}
	
	@Override
	public int getRowCount() {
		return this.plugins.length;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		if (col == 0)
			return PluginStatus.class;
		
		return String.class;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0:
				return this.plugins[row].getStatus();
			case 1:
				return this.plugins[row].getName();
			case 2:
				return this.plugins[row].getAuthor();
			case 3:
				return this.plugins[row].getVersion();
			case 4:
				return this.plugins[row].getServiceProvider();
			default:
				return null;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		for (int i = 0; i < this.plugins.length; i++) {
			if (this.plugins[i] == evt.getSource()) {
				this.fireTableRowsUpdated(i, i);
			}
		}
	}
	
}
