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
package com.leclercb.taskunifier.gui.commons.models;

import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.commons.gui.swing.models.DefaultSortedComboBoxModel;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.commons.comparators.SynchronizerGuiPluginComparator;
import com.leclercb.taskunifier.gui.main.Main;

public class SynchronizerGuiPluginModel extends DefaultSortedComboBoxModel implements ListChangeListener {
	
	private boolean publisher;
	private boolean synchronizer;
	
	public SynchronizerGuiPluginModel(boolean publisher, boolean synchronizer) {
		super(SynchronizerGuiPluginComparator.INSTANCE);
		
		this.publisher = publisher;
		this.synchronizer = synchronizer;
		
		this.initialize();
	}
	
	private void initialize() {
		List<SynchronizerGuiPlugin> plugins = Main.getApiPlugins().getPlugins();
		for (SynchronizerGuiPlugin plugin : plugins) {
			if ((this.publisher && plugin.isPublisher())
					|| (this.synchronizer && plugin.isSynchronizer()))
				this.addElement(plugin);
		}
		
		Main.getApiPlugins().addListChangeListener(
				new WeakListChangeListener(Main.getApiPlugins(), this));
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) event.getValue();
		
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			if ((this.publisher && plugin.isPublisher())
					|| (this.synchronizer && plugin.isSynchronizer()))
				this.addElement(plugin);
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.removeElement(plugin);
		}
	}
	
}
