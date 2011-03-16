package com.leclercb.taskunifier.gui.components.plugins;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.api.utils.HttpUtils;
import com.leclercb.commons.api.utils.http.HttpResponse;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.components.plugins.Plugin.PluginStatus;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException.PluginExceptionType;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginsUtils {
	
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
				
				if (plugins.size() > 1) {
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
					Main.API_PLUGINS.addPlugin(file, plugin);
					
					GuiLogger.getLogger().info(
							"Plugin loaded: "
							+ plugin.getName()
							+ " - "
							+ plugin.getVersion());
				}
				
				return;
			} catch (PluginException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				throw new PluginException(
						PluginExceptionType.ERROR_LOADING_PLUGIN,
						"Could not load plugin jar file: "
						+ file.getAbsolutePath());
			}
		}
	}
	
	public static void installPlugin(Plugin plugin) {
		try {
			File tmpFile = File.createTempFile("taskunifier_plugin_", ".jar");
			
			org.apache.commons.io.FileUtils.copyURLToFile(
					new URL(plugin.getDownloadUrl()),
					tmpFile);
			
			PluginsUtils.loadPlugin(tmpFile, false);
			
			File outFile = new File(Main.RESOURCES_FOLDER
					+ File.separator
					+ "plugins"
					+ File.separator
					+ UUID.randomUUID().toString()
					+ ".jar");
			
			outFile.createNewFile();
			
			org.apache.commons.io.FileUtils.copyFile(tmpFile, outFile);
			
			PluginsUtils.loadPlugin(outFile, true);
			
			plugin.setStatus(PluginStatus.INSTALLED);
		} catch (PluginException e) {
			String message = null;
			
			switch (e.getType()) {
				case ERROR_LOADING_PLUGIN:
					message = Translations.getString("error.cannot_install_plugin");
					break;
				case NO_VALID_PLUGIN:
					message = Translations.getString("error.no_valid_plugin");
					break;
				case MORE_THAN_ONE_PLUGIN:
					message = Translations.getString("error.more_than_one_plugin");
					break;
				case PLUGIN_FOUND:
					message = Translations.getString("error.plugin_already_installed");
					break;
			}
			
			GuiLogger.getLogger().warning(e.getMessage());
			
			ErrorDialog dialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					message,
					e,
					false);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			
			ErrorDialog dialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					null,
					e,
					true);
			dialog.setVisible(true);
		}
	}
	
	public static void updatePlugin(Plugin plugin) {
		deletePlugin(plugin);
		installPlugin(plugin);
	}
	
	public static void deletePlugin(Plugin plugin) {
		List<SynchronizerGuiPlugin> existingPlugins = new ArrayList<SynchronizerGuiPlugin>(Main.API_PLUGINS.getPlugins());
		for (SynchronizerGuiPlugin existingPlugin : existingPlugins) {
			if (existingPlugin.getId().equals(plugin.getId())) {
				File file = Main.API_PLUGINS.getFile(existingPlugin);
				file.deleteOnExit();
				Main.API_PLUGINS.removePlugin(existingPlugin);
				plugin.setStatus(PluginStatus.TO_INSTALL);
			}
		}
	}
	
	public static Plugin[] loadPluginsFromXML() {
		try {
			HttpResponse response = null;
			
			Boolean proxyEnabled = Main.SETTINGS.getBooleanProperty("proxy.enabled");
			if (proxyEnabled != null && proxyEnabled) {
				response = HttpUtils.getHttpGetResponse(
						new URI(Constants.PLUGINS_FILE),
						Main.SETTINGS.getStringProperty("proxy.host"),
						Main.SETTINGS.getIntegerProperty("proxy.port"),
						Main.SETTINGS.getStringProperty("proxy.login"),
						Main.SETTINGS.getStringProperty("proxy.password"));
			} else {
				response = HttpUtils.getHttpGetResponse(new URI(
						Constants.PLUGINS_FILE));
			}
			
			if (!response.isSuccessfull()) {
				ErrorDialog dialog = new ErrorDialog(
						MainFrame.getInstance().getFrame(),
						"Cannot load plugin database",
						null,
						false);
				dialog.setVisible(true);
				
				return new Plugin[0];
			}
			
			String content = response.getContent();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			factory.setIgnoringComments(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(IOUtils.toInputStream(content));
			
			document.getDocumentElement().normalize();
			
			if (!document.getChildNodes().item(0).getNodeName().equals(
			"plugins"))
				throw new Exception("Root name must be \"plugins\"");
			
			Node root = document.getChildNodes().item(0);
			
			NodeList nPlugins = root.getChildNodes();
			
			List<Plugin> plugins = new ArrayList<Plugin>();
			
			for (int i = 0; i < nPlugins.getLength(); i++) {
				if (!nPlugins.item(i).getNodeName().equals("plugin"))
					continue;
				
				NodeList nPlugin = nPlugins.item(i).getChildNodes();
				
				String id = null;
				String name = null;
				String author = null;
				String version = null;
				String serviceProvider = null;
				String downloadUrl = null;
				
				for (int j = 0; j < nPlugin.getLength(); j++) {
					Node element = nPlugin.item(j);
					
					if (element.getNodeName().equals("id"))
						id = element.getTextContent();
					
					if (element.getNodeName().equals("name"))
						name = element.getTextContent();
					
					if (element.getNodeName().equals("author"))
						author = element.getTextContent();
					
					if (element.getNodeName().equals("version"))
						version = element.getTextContent();
					
					if (element.getNodeName().equals("serviceProvider"))
						serviceProvider = element.getTextContent();
					
					if (element.getNodeName().equals("downloadUrl"))
						downloadUrl = element.getTextContent();
				}
				
				Plugin plugin = new Plugin(
						PluginStatus.TO_INSTALL,
						id,
						name,
						author,
						version,
						serviceProvider,
						downloadUrl);
				
				plugins.add(plugin);
			}
			
			return plugins.toArray(new Plugin[0]);
		} catch (Exception e) {
			e.printStackTrace();
			
			ErrorDialog dialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					"Cannot load plugin database",
					null,
					false);
			dialog.setVisible(true);
			
			return new Plugin[0];
		}
	}
	
}
