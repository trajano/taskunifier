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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.plugins.Plugin;

public class PluginListModel extends AbstractListModel implements PropertyChangeListener {
	
	private Plugin[] plugins;
	
	public PluginListModel() {
		this.setPlugins(new Plugin[0]);
	}
	
	public Plugin[] getPlugins() {
		return this.plugins;
	}
	
	public void setPlugins(Plugin[] plugins) {
		CheckUtils.doesNotContainNull(plugins);
		
		if (this.plugins != null)
			for (Plugin plugin : this.plugins)
				plugin.removePropertyChangeListener(this);
		
		this.plugins = plugins;
		
		for (Plugin plugin : this.plugins)
			plugin.addPropertyChangeListener(new WeakPropertyChangeListener(
					plugin,
					this));
		
		this.fireContentsChanged(this, 0, this.getSize());
	}
	
	public Plugin getPlugin(int index) {
		return this.plugins[index];
	}
	
	@Override
	public Object getElementAt(int index) {
		if (index < 0 || index >= this.plugins.length)
			return null;
		
		return this.plugins[index];
	}
	
	@Override
	public int getSize() {
		return this.plugins.length;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		for (int i = 0; i < this.plugins.length; i++) {
			if (this.plugins[i].equals(evt.getSource())) {
				this.fireContentsChanged(this, i, i);
			}
		}
	}
	
}
