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

public class PluginsUtils {
	
	public static void loadPlugin(File file) throws PluginException {
		if (!file.isFile()
				|| !FileUtils.getExtention(file.getAbsolutePath()).equals("jar"))
			return;
		
		try {
			File tmpFile = File.createTempFile("taskunifier_plugin_", ".jar");
			org.apache.commons.io.FileUtils.copyFile(file, tmpFile);
			
			List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.loadJar(
					tmpFile,
					file,
					false);
			
			if (plugins.size() == 0) {
				throw new PluginException(PluginExceptionType.NO_VALID_PLUGIN);
			}
			
			if (plugins.size() > 1) {
				throw new PluginException(
						PluginExceptionType.MORE_THAN_ONE_PLUGIN);
			}
			
			SynchronizerGuiPlugin plugin = plugins.get(0);
			List<SynchronizerGuiPlugin> existingPlugins = Main.API_PLUGINS.getPlugins();
			
			for (SynchronizerGuiPlugin p : existingPlugins) {
				if (EqualsUtils.equals(p.getId(), plugin.getId())
						&& EqualsUtils.equals(
								p.getVersion(),
								plugin.getVersion())) {
					throw new PluginException(PluginExceptionType.PLUGIN_FOUND);
				}
			}
			
			Main.API_PLUGINS.addPlugin(file, plugin);
			
			GuiLogger.getLogger().info(
					"Plugin loaded: "
							+ plugin.getName()
							+ " - "
							+ plugin.getVersion());
			
			return;
		} catch (PluginException e) {
			throw e;
		} catch (Exception e) {
			throw new PluginException(PluginExceptionType.ERROR_LOADING_PLUGIN);
		}
	}
	
	public static void installPlugin(Plugin plugin) {
		File file = null;
		
		try {
			file = new File(Main.RESOURCES_FOLDER
					+ File.separator
					+ "plugins"
					+ File.separator
					+ UUID.randomUUID().toString()
					+ ".jar");
			
			file.createNewFile();
			
			org.apache.commons.io.FileUtils.copyURLToFile(
					new URL(plugin.getDownloadUrl()),
					file);
			
			PluginsUtils.loadPlugin(file);
			
			plugin.setStatus(PluginStatus.INSTALLED);
		} catch (PluginException e) {
			file.delete();
			
			ErrorDialog dialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					e.getMessage(),
					e,
					false);
			dialog.setVisible(true);
		} catch (Exception e) {
			file.delete();
			
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
		List<SynchronizerGuiPlugin> existingPlugins = new ArrayList<SynchronizerGuiPlugin>(
				Main.API_PLUGINS.getPlugins());
		for (SynchronizerGuiPlugin existingPlugin : existingPlugins) {
			if (existingPlugin.getId().equals(plugin.getId())) {
				File file = Main.API_PLUGINS.getFile(existingPlugin);
				file.delete();
				Main.API_PLUGINS.removePlugin(existingPlugin);
				plugin.setStatus(PluginStatus.DELETED);
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
