package com.leclercb.taskunifier.gui.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.leclercb.commons.api.utils.CheckUtils;

public class PluginLoader<PluginClass> {
	
	private Class<?> pluginClass;
	private ArrayList<PluginClass> plugins;
	
	public PluginLoader(Class<PluginClass> pluginClass) {
		CheckUtils.isNotNull(pluginClass, "Plugin class cannot be null");
		this.pluginClass = pluginClass;
		this.plugins = new ArrayList<PluginClass>();
	}
	
	public List<PluginClass> getPlugins() {
		return new ArrayList<PluginClass>(this.plugins);
	}
	
	public void addPlugin(PluginClass plugin) {
		this.plugins.add(plugin);
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlugin(File file) throws Exception {
		CheckUtils.isNotNull(file, "File cannot be null");
		
		if (!file.exists())
			throw new IllegalArgumentException("File must exist");
		
		URL url = file.toURI().toURL();
		URLClassLoader loader = new URLClassLoader(new URL[] { url });
		JarFile jar = new JarFile(file.getAbsolutePath());
		Enumeration<JarEntry> entries = jar.entries();
		
		while (entries.hasMoreElements()) {
			String tmp = entries.nextElement().getName();
			
			if (tmp.length() > 6
					&& tmp.substring(tmp.length() - 6).compareTo(".class") == 0) {
				tmp = tmp.substring(0, tmp.length() - 6);
				tmp = tmp.replaceAll("/", ".");
				
				Class<?> tmpClass = Class.forName(tmp, true, loader);
				
				if (tmpClass.isAssignableFrom(this.pluginClass))
					this.plugins.add((PluginClass) tmpClass.newInstance());
			}
		}
	}
	
}
