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
package com.leclercb.taskunifier.gui.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.SystemUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.plugins.PluginLoader;
import com.leclercb.commons.api.properties.PropertiesConfiguration;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.commons.gui.swing.lookandfeel.types.DefaultLookAndFeelDescriptor;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.coders.NoteFactoryXMLCoder;
import com.leclercb.taskunifier.api.settings.ModelIdSettingsCoder;
import com.leclercb.taskunifier.gui.actions.ActionCheckPluginVersion;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.actions.ActionReview;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.models.GuiContext;
import com.leclercb.taskunifier.gui.api.models.GuiFolder;
import com.leclercb.taskunifier.gui.api.models.GuiGoal;
import com.leclercb.taskunifier.gui.api.models.GuiLocation;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.api.models.coders.GuiContextFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiFolderFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiGoalFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiLocationFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiTaskFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.api.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.api.plugins.exc.PluginException.PluginExceptionType;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.api.templates.coders.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.welcome.LanguageDialog;
import com.leclercb.taskunifier.gui.components.welcome.WelcomeDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.resources.Resources;
import com.leclercb.taskunifier.gui.settings.SettingsVersion;
import com.leclercb.taskunifier.gui.swing.lookandfeel.JTattooLookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class Main {
	
	public static PluginLoader<SynchronizerGuiPlugin> API_PLUGINS;
	public static PropertiesConfiguration INIT_SETTINGS;
	public static PropertiesConfiguration SETTINGS;
	public static boolean FIRST_EXECUTION;
	public static String RESOURCES_FOLDER;
	public static String DATA_FOLDER;
	public static String PLUGINS_FOLDER;
	
	public static ActionSupport AFTER_START;
	public static ActionSupport BEFORE_EXIT;
	
	private static PrintStream ORIGINAL_OUT_STREAM;
	private static PrintStream ORIGINAL_ERR_STREAM;
	private static OutputStream LOG_STREAM;
	private static PrintStream NEW_STREAM;
	
	public static String getInitSettingsFile() {
		return RESOURCES_FOLDER + File.separator + "taskunifier.properties";
	}
	
	public static String getSettingsFile() {
		return DATA_FOLDER + File.separator + "settings.properties";
	}
	
	public static void main(String[] args) {
		FIRST_EXECUTION = false;
		
		AFTER_START = new ActionSupport(Main.class);
		BEFORE_EXIT = new ActionSupport(Main.class);
		
		boolean outdatedPlugins;
		
		try {
			loadStreamRedirection();
			loadResourceFolder();
			loadInitSettings();
			loadDataFolder();
			loadPluginsFolder();
			loadSettings();
			loadLocale();
			loadModels();
			loadLookAndFeel();
			outdatedPlugins = loadApiPlugins();
			loadSynchronizer();
			
			Constants.initialize();
			
			AFTER_START.fireActionPerformed(0, "AFTER_START");
		} catch (Exception e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		final boolean finalOutdatedPlugins = outdatedPlugins;
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				String lookAndFeel = SETTINGS.getStringProperty("theme.lookandfeel");
				
				try {
					if (lookAndFeel != null) {
						LookAndFeelDescriptor laf = LookAndFeelUtils.getLookAndFeel(lookAndFeel);
						if (laf != null)
							laf.setLookAndFeel();
					} else {
						LookAndFeelDescriptor laf = LookAndFeelUtils.getLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						
						if (SystemUtils.IS_OS_WINDOWS)
							laf = LookAndFeelUtils.getLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel$Blue");
						
						if (laf != null) {
							laf.setLookAndFeel();
							
							SETTINGS.setStringProperty(
									"theme.lookandfeel",
									laf.getIdentifier());
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							t.getMessage(),
							null,
							null,
							t,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
				
				if (FIRST_EXECUTION) {
					new LanguageDialog(null).setVisible(true);
					new WelcomeDialog(null).setVisible(true);
				}
				
				MainFrame.getInstance().getFrame().setVisible(true);
				ActionCheckVersion.checkVersion(true);
				ActionCheckPluginVersion.checkPluginVersion(true);
				
				Boolean showed = Main.SETTINGS.getBooleanProperty("review.showed");
				if (showed == null || !showed)
					ActionReview.review();
				
				Main.SETTINGS.setBooleanProperty("review.showed", true);
				
				if (finalOutdatedPlugins)
					ActionManagePlugins.managePlugins();
				
				Boolean syncStart = Main.SETTINGS.getBooleanProperty("synchronizer.sync_start");
				if (syncStart != null && syncStart)
					ActionSynchronize.synchronize(false);
			}
			
		});
	}
	
	private static void loadStreamRedirection() {
		if (!EqualsUtils.equals(
				System.getProperty("com.leclercb.taskunifier.debug_mode"),
				"true")) {
			ORIGINAL_OUT_STREAM = System.out;
			ORIGINAL_ERR_STREAM = System.err;
			
			LOG_STREAM = new ByteArrayOutputStream();
			NEW_STREAM = new PrintStream(LOG_STREAM, true);
			
			System.setOut(NEW_STREAM);
			System.setErr(NEW_STREAM);
		}
	}
	
	private static void loadResourceFolder() throws Exception {
		RESOURCES_FOLDER = System.getProperty("com.leclercb.taskunifier.resource_folder");
		
		if (RESOURCES_FOLDER == null)
			RESOURCES_FOLDER = "resources";
		
		File file = new File(RESOURCES_FOLDER);
		if (!file.exists() || !file.isDirectory())
			throw new Exception(String.format(
					"Resources folder \"%1s\" does not exist",
					RESOURCES_FOLDER));
	}
	
	private static void loadInitSettings() {
		INIT_SETTINGS = new PropertiesConfiguration(new Properties());
		
		try {
			INIT_SETTINGS.load(new FileInputStream(getInitSettingsFile()));
		} catch (Exception e) {

		}
	}
	
	private static void loadDataFolder() throws Exception {
		DATA_FOLDER = INIT_SETTINGS.getStringProperty("com.leclercb.taskunifier.data_folder");
		
		if (DATA_FOLDER == null)
			DATA_FOLDER = System.getProperty("com.leclercb.taskunifier.data_folder");
		
		if (DATA_FOLDER == null)
			DATA_FOLDER = "data";
		
		File file = new File(DATA_FOLDER);
		if (!file.exists()) {
			if (!file.mkdir())
				throw new Exception(String.format(
						"Error while creating folder \"%1s\"",
						DATA_FOLDER));
			
			try {
				file.setExecutable(true, true);
				file.setReadable(true, true);
				file.setWritable(true, true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
			FIRST_EXECUTION = true;
			return;
		} else if (!file.isDirectory()) {
			throw new Exception(String.format(
					"\"%1s\" is not a folder",
					DATA_FOLDER));
		}
	}
	
	private static void loadPluginsFolder() {
		PLUGINS_FOLDER = DATA_FOLDER + File.separator + "plugins";
	}
	
	private static void loadSettings() throws Exception {
		try {
			Properties defaultProperties = new Properties();
			defaultProperties.load(Resources.class.getResourceAsStream("default_settings.properties"));
			
			SETTINGS = new PropertiesConfiguration(new Properties(
					defaultProperties));
			
			SETTINGS.addCoder(new ModelIdSettingsCoder());
			
			SETTINGS.load(new FileInputStream(getSettingsFile()));
			
			SettingsVersion.updateSettings();
		} catch (Exception e) {
			SETTINGS = new PropertiesConfiguration(new Properties());
			
			SETTINGS.addCoder(new ModelIdSettingsCoder());
			
			SETTINGS.load(Resources.class.getResourceAsStream("default_settings.properties"));
			
			if (!FIRST_EXECUTION)
				JOptionPane.showMessageDialog(
						null,
						"Settings file not found. A default settings file is loaded.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			
			FIRST_EXECUTION = true;
		}
	}
	
	private static void loadLocale() throws Exception {
		Translations.setLocale(SETTINGS.getLocaleProperty("general.locale"));
		SETTINGS.setLocaleProperty("general.locale", Translations.getLocale());
	}
	
	private static void loadModels() throws Exception {
		ContextFactory.initializeWithClass(GuiContext.class);
		FolderFactory.initializeWithClass(GuiFolder.class);
		GoalFactory.initializeWithClass(GuiGoal.class);
		LocationFactory.initializeWithClass(GuiLocation.class);
		TaskFactory.initializeWithClass(GuiTask.class);
		
		try {
			new GuiContextFactoryXMLCoder().decode(new FileInputStream(
					DATA_FOLDER + File.separator + "contexts.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new GuiFolderFactoryXMLCoder().decode(new FileInputStream(
					DATA_FOLDER + File.separator + "folders.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new GuiGoalFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "goals.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new GuiLocationFactoryXMLCoder().decode(new FileInputStream(
					DATA_FOLDER + File.separator + "locations.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new NoteFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "notes.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new GuiTaskFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "tasks.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new TemplateFactoryXMLCoder(false).decode(new FileInputStream(
					DATA_FOLDER + File.separator + "templates.xml"));
		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			new TaskSearcherFactoryXMLCoder().decode(new FileInputStream(
					DATA_FOLDER + File.separator + "searchers.xml"));
		} catch (FileNotFoundException e) {
			ActionResetGeneralSearchers.resetGeneralSearchers();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadLookAndFeel() throws Exception {
		// jGoodies
		Properties jgoodies = new Properties();
		jgoodies.load(Resources.class.getResourceAsStream("jgoodies_themes.properties"));
		
		for (Object key : jgoodies.keySet())
			LookAndFeelUtils.addLookAndFeel(new DefaultLookAndFeelDescriptor(
					"jGoodies - " + jgoodies.getProperty(key.toString()),
					key.toString()));
		
		// jTattoo
		Properties jtattoo = new Properties();
		jtattoo.load(Resources.class.getResourceAsStream("jtattoo_themes.properties"));
		
		for (Object key : jtattoo.keySet())
			LookAndFeelUtils.addLookAndFeel(new JTattooLookAndFeelDescriptor(
					"jTattoo - " + jtattoo.getProperty(key.toString()),
					key.toString()));
	}
	
	public static boolean loadApiPlugins() {
		API_PLUGINS = new PluginLoader<SynchronizerGuiPlugin>(
				SynchronizerGuiPlugin.class);
		
		API_PLUGINS.addPlugin(null, DummyGuiPlugin.getInstance());
		
		File pluginsFolder = new File(PLUGINS_FOLDER);
		
		if (!pluginsFolder.exists()) {
			pluginsFolder.mkdir();
			
			try {
				pluginsFolder.setExecutable(true, true);
				pluginsFolder.setReadable(true, true);
				pluginsFolder.setWritable(true, true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		boolean outdatedPlugins = false;
		if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
			File[] pluginFiles = pluginsFolder.listFiles();
			
			for (File file : pluginFiles) {
				try {
					PluginsUtils.loadPlugin(file);
				} catch (PluginException e) {
					if (e.getType() == PluginExceptionType.OUTDATED_PLUGIN)
						outdatedPlugins = true;
					
					GuiLogger.getLogger().warning(e.getMessage());
				} catch (Throwable t) {
					GuiLogger.getLogger().warning("Plugin unknown exception");
					t.printStackTrace();
				}
			}
		}
		
		API_PLUGINS.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent evt) {
				SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) evt.getValue();
				
				if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED) {
					Main.SETTINGS.setStringProperty("api.id", plugin.getId());
				}
				
				if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
					if (EqualsUtils.equals(
							Main.SETTINGS.getStringProperty("api.id"),
							plugin.getId()))
						Main.SETTINGS.setStringProperty(
								"api.id",
								DummyGuiPlugin.getInstance().getId());
				}
			}
			
		});
		
		return outdatedPlugins;
	}
	
	private static void loadSynchronizer() throws Exception {
		SynchronizerUtils.setTaskRepeatEnabled(true);
	}
	
	public static void stop() {
		if (Synchronizing.isSynchronizing()) {
			JOptionPane.showMessageDialog(
					null,
					Translations.getString("general.synchronization_ongoing"),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Boolean syncExit = Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit");
		if (syncExit != null && syncExit)
			ActionSynchronize.synchronize(false);
		
		BEFORE_EXIT.fireActionPerformed(0, "BEFORE_EXIT");
		
		GuiLogger.getLogger().info("Exiting " + Constants.TITLE);
		
		try {
			try {
				new GuiContextFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "contexts.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new GuiFolderFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "folders.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new GuiGoalFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "goals.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new GuiLocationFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "locations.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new NoteFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "notes.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new GuiTaskFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "tasks.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new TemplateFactoryXMLCoder(false).encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "templates.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				new TaskSearcherFactoryXMLCoder().encode(new FileOutputStream(
						DATA_FOLDER + File.separator + "searchers.xml"));
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				File f = new File(getInitSettingsFile());
				
				if (f.canWrite())
					saveInitSettings();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				saveSettings();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						null,
						e.getMessage(),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
			
			MainFrame.getInstance().getFrame().dispose();
		} finally {
			try {
				if (ORIGINAL_OUT_STREAM != null) {
					System.setOut(ORIGINAL_OUT_STREAM);
					System.setErr(ORIGINAL_ERR_STREAM);
					
					NEW_STREAM.close();
					LOG_STREAM.close();
					
					File logFile = new File(DATA_FOLDER
							+ File.separator
							+ "taskunifier.log");
					
					if (!logFile.exists())
						logFile.createNewFile();
					
					String logFileContent = FileUtils.readFileToString(
							logFile,
							"UTF-8");
					
					String log = LOG_STREAM.toString();
					log = "\n\n\n---------- "
							+ DateUtils.getDateAsString("dd/MM/yyyy HH:mm:ss")
							+ " ----------\n\n"
							+ log;
					
					FileUtils.writeStringToFile(logFile, logFileContent + log);
				}
			} catch (Exception e) {
				GuiLogger.getLogger().severe(
						"Could not copy log information into log file");
			}
		}
		
		System.exit(0);
	}
	
	private static void saveInitSettings() throws FileNotFoundException,
			IOException {
		INIT_SETTINGS.store(
				new FileOutputStream(getInitSettingsFile()),
				Constants.TITLE + " Init Settings");
	}
	
	public static void saveSettings() throws FileNotFoundException, IOException {
		SETTINGS.store(new FileOutputStream(getSettingsFile()), Constants.TITLE
				+ " Settings");
	}
	
}
