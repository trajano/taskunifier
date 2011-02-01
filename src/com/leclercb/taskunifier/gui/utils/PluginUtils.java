package com.leclercb.taskunifier.gui.utils;

import java.io.File;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.utils.PluginUtils.PluginException.PluginExceptionType;

public final class PluginUtils {
	
	private PluginUtils() {

	}
	
	public static void loadPlugin(File file, boolean add)
			throws PluginException {
		if (file.isFile()
				&& FileUtils.getExtention(file.getAbsolutePath()).equals("jar")) {
			try {
				List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.loadJar(
						file,
						false);
				
				if (plugins.size() == 0) {
					throw new PluginException(
							PluginExceptionType.NO_VALID_PLUGIN,
							"Jar file doesn't contain any valid plugin: "
									+ file.getAbsolutePath());
				}
				
				if (plugins.size() >= 1) {
					throw new PluginException(
							PluginExceptionType.MORE_THAN_ONE_PLUGIN,
							"Jar file contains more than one plugin: "
									+ file.getAbsolutePath());
				}
				
				SynchronizerGuiPlugin plugin = plugins.get(0);
				List<SynchronizerGuiPlugin> existingPlugins = Main.API_PLUGINS.getPlugins();
				
				for (SynchronizerGuiPlugin p : existingPlugins) {
					if (EqualsUtils.equals(p.getId(), plugin.getId())
							&& EqualsUtils.equals(
									p.getVersion(),
									plugin.getVersion())) {
						throw new PluginException(
								PluginExceptionType.PLUGIN_FOUND,
								"A plugin ("
										+ p.getName()
										+ ") with the same ID and version already exists: "
										+ plugin.getName());
					}
				}
				
				if (add) {
					Main.API_PLUGINS.addPlugin(plugin);
					
					GuiLogger.getLogger().info(
							"Plugin loaded: "
									+ plugin.getName()
									+ " - "
									+ plugin.getVersion());
				}
				
				return;
			} catch (Exception e) {
				throw new PluginException(
						PluginExceptionType.ERROR_LOADING_PLUGIN,
						"Could not load plugin jar file: "
								+ file.getAbsolutePath());
			}
		}
	}
	
	public static class PluginException extends Exception {
		
		public static enum PluginExceptionType {
			
			ERROR_LOADING_PLUGIN,
			NO_VALID_PLUGIN,
			MORE_THAN_ONE_PLUGIN,
			PLUGIN_FOUND;
			
		}
		
		private PluginExceptionType type;
		
		private PluginException(PluginExceptionType type, String message) {
			super(message);
			
			CheckUtils.isNotNull(type, "Type cannot be null");
			
			this.type = type;
		}
		
		public PluginExceptionType getType() {
			return this.type;
		}
		
	}
	
}
