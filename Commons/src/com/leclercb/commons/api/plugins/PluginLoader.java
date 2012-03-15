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
package com.leclercb.commons.api.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.FileUtils;

public class PluginLoader<PluginClass> implements ListChangeSupported {
	
	private ListChangeSupport listChangeSupport;
	
	private Class<?> pluginClass;
	private ArrayList<PluginClass> plugins;
	private ArrayList<File> files;
	
	public PluginLoader(Class<PluginClass> pluginClass) {
		CheckUtils.isNotNull(pluginClass);
		
		this.listChangeSupport = new ListChangeSupport(this);
		
		this.pluginClass = pluginClass;
		this.plugins = new ArrayList<PluginClass>();
		this.files = new ArrayList<File>();
	}
	
	public File getFile(PluginClass plugin) {
		int index = this.plugins.indexOf(plugin);
		
		if (index == -1)
			return null;
		
		return this.files.get(index);
	}
	
	public List<PluginClass> getPlugins() {
		return Collections.unmodifiableList(new ArrayList<PluginClass>(
				this.plugins));
	}
	
	public void addPlugin(File originFile, PluginClass plugin) {
		this.plugins.add(plugin);
		this.files.add(originFile);
		
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				this.plugins.size() - 1,
				plugin);
	}
	
	public void removePlugin(PluginClass plugin) {
		int index = this.plugins.indexOf(plugin);
		
		if (index == -1)
			return;
		
		this.plugins.remove(index);
		this.files.remove(index);
		
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_REMOVED,
				index,
				plugin);
	}
	
	public List<PluginClass> loadJar(File file, File origin) throws Exception {
		return this.loadJar(file, origin, true);
	}
	
	@SuppressWarnings("unchecked")
	public List<PluginClass> loadJar(File tmpFile, File originFile, boolean add)
			throws Exception {
		CheckUtils.isNotNull(tmpFile);
		
		if (!tmpFile.exists())
			throw new IllegalArgumentException("File must exist");
		
		JarFile jar = null;
		
		try {
			URL url = new URL("jar", "", -1, tmpFile.toURI().toString() + "!/");
			URLClassLoader loader = new URLClassLoader(new URL[] { url });
			
			jar = new JarFile(tmpFile.getAbsolutePath());
			Enumeration<JarEntry> entries = jar.entries();
			
			List<PluginClass> addedPlugins = new ArrayList<PluginClass>();
			while (entries.hasMoreElements()) {
				String tmp = entries.nextElement().getName();
				
				if (FileUtils.getExtention(tmp).equals("class")) {
					tmp = tmp.substring(0, tmp.length() - 6);
					tmp = tmp.replaceAll("/", ".");
					
					try {
						Class<?> tmpClass = Class.forName(tmp, true, loader);
						
						if (this.pluginClass.isAssignableFrom(tmpClass))
							addedPlugins.add((PluginClass) tmpClass.newInstance());
					} catch (Throwable t) {}
				}
			}
			
			if (add) {
				for (PluginClass plugin : addedPlugins) {
					this.addPlugin(originFile, plugin);
				}
			}
			
			return Collections.unmodifiableList(new ArrayList<PluginClass>(
					addedPlugins));
		} finally {
			try {
				if (jar != null)
					jar.close();
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
}
