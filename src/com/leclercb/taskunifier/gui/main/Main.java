/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
import com.leclercb.taskunifier.api.models.coders.TaskFactoryXMLCoder;
import com.leclercb.taskunifier.api.settings.ModelIdSettingsCoder;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionReview;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.models.GuiContext;
import com.leclercb.taskunifier.gui.api.models.GuiFolder;
import com.leclercb.taskunifier.gui.api.models.GuiGoal;
import com.leclercb.taskunifier.gui.api.models.GuiLocation;
import com.leclercb.taskunifier.gui.api.models.coders.GuiContextFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiFolderFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiGoalFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiLocationFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.api.templates.coders.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.components.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.components.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.components.welcome.LanguageDialog;
import com.leclercb.taskunifier.gui.components.welcome.WelcomeDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.resources.Resources;
import com.leclercb.taskunifier.gui.settings.SettingsVersion;
import com.leclercb.taskunifier.gui.swing.lookandfeel.JTattooLookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.translations.Translations;

public class Main {
	
	public static PluginLoader<SynchronizerGuiPlugin> API_PLUGINS;
	public static PropertiesConfiguration SETTINGS;
	public static boolean FIRST_EXECUTION;
	public static String RESOURCES_FOLDER;
	public static String DATA_FOLDER;
	
	private static PrintStream ORIGINAL_OUT_STREAM;
	private static PrintStream ORIGINAL_ERR_STREAM;
	private static File LOG_FILE;
	private static OutputStream LOG_FILE_STREAM;
	private static PrintStream NEW_STREAM;
	
	public static void main(String[] args) {
		try {
			loadStreamRedirection();
			loadResourceFolder();
			loadDataFolder();
			loadSettings();
			loadLocale();
			loadModels();
			loadLookAndFeel();
			loadApiPlugins();
		} catch (Exception e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
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
						if (laf != null)
							laf.setLookAndFeel();
						
						SETTINGS.setStringProperty(
								"theme.lookandfeel",
								UIManager.getSystemLookAndFeelClassName());
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					ErrorDialog errorDialog = new ErrorDialog(
							MainFrame.getInstance().getFrame(),
							null,
							e,
							true);
					errorDialog.setVisible(true);
					
					return;
				}
				
				if (FIRST_EXECUTION) {
					new LanguageDialog(null, true).setVisible(true);
					new WelcomeDialog(null, true).setVisible(true);
				}
				
				MainFrame.getInstance().getFrame().setVisible(true);
				new ActionCheckVersion(true).checkVersion();
				
				Boolean showed = Main.SETTINGS.getBooleanProperty("review.showed");
				if (showed == null || !showed)
					new ActionReview().review();
				
				Main.SETTINGS.setBooleanProperty("review.showed", true);
				
				Boolean syncStart = Main.SETTINGS.getBooleanProperty("synchronizer.sync_start");
				if (syncStart != null && syncStart)
					new ActionSynchronize(false).synchronize();
			}
			
		});
	}
	
	private static void loadStreamRedirection() {
		ORIGINAL_OUT_STREAM = System.out;
		ORIGINAL_ERR_STREAM = System.err;
		
		try {
			LOG_FILE = File.createTempFile("taskunifier_log_", ".log");
			LOG_FILE_STREAM = new FileOutputStream(LOG_FILE);
			NEW_STREAM = new PrintStream(LOG_FILE_STREAM);
			
			if (!EqualsUtils.equals(
					System.getProperty("com.leclercb.taskunifier.debug_mode"),
					"true")) {
				System.setOut(NEW_STREAM);
				System.setErr(NEW_STREAM);
			}
		} catch (IOException e) {
			GuiLogger.getLogger().severe("Error while creating log file");
		}
	}
	
	private static void loadResourceFolder() throws Exception {
		if (System.getProperty("com.leclercb.taskunifier.resource_folder") == null) {
			RESOURCES_FOLDER = "resources";
		} else {
			RESOURCES_FOLDER = System.getProperty("com.leclercb.taskunifier.resource_folder");
		}
		
		File file = new File(RESOURCES_FOLDER);
		if (!file.exists() || !file.isDirectory())
			throw new Exception(Translations.getString(
					"error.resources_folder_does_not_exist",
					RESOURCES_FOLDER));
	}
	
	private static void loadDataFolder() throws Exception {
		if (System.getProperty("com.leclercb.taskunifier.data_folder") == null) {
			DATA_FOLDER = "data";
		} else {
			DATA_FOLDER = System.getProperty("com.leclercb.taskunifier.data_folder");
		}
		
		File file = new File(DATA_FOLDER);
		if (!file.exists()) {
			/*
			 * int response = JOptionPane.showConfirmDialog( null,
			 * Translations.getString( "general.create_data_folder_question",
			 * DATA_FOLDER),
			 * Translations.getString("general.create_data_folder"),
			 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			 * 
			 * if (response == JOptionPane.NO_OPTION) throw new
			 * Exception(Translations.getString( "error.data_folder_needed",
			 * Constants.TITLE));
			 */

			if (!file.mkdir())
				throw new Exception(Translations.getString(
						"error.create_data_folder",
						DATA_FOLDER));
			
			FIRST_EXECUTION = true;
			return;
		} else if (!file.isDirectory()) {
			throw new Exception(Translations.getString(
					"error.data_folder_not_a_folder",
					DATA_FOLDER));
		}
		
		FIRST_EXECUTION = false;
	}
	
	private static void loadSettings() throws Exception {
		try {
			SETTINGS = new PropertiesConfiguration(new Properties());
			
			SETTINGS.addCoder(new ModelIdSettingsCoder());
			
			SETTINGS.load(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "settings.properties"));
			SettingsVersion.updateSettings();
		} catch (FileNotFoundException e) {
			SETTINGS.load(Resources.class.getResourceAsStream("default_settings.properties"));
			
			if (!FIRST_EXECUTION)
				JOptionPane.showMessageDialog(
						null,
						Translations.getString("error.settings_file"),
						"Error",
						JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadLocale() throws Exception {
		Translations.setLocale(SETTINGS.getLocaleProperty("general.locale"));
	}
	
	private static void loadModels() throws Exception {
		ContextFactory.initializeWithClass(GuiContext.class);
		FolderFactory.initializeWithClass(GuiFolder.class);
		GoalFactory.initializeWithClass(GuiGoal.class);
		LocationFactory.initializeWithClass(GuiLocation.class);
		
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
			new TaskFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
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
	
	public static void loadApiPlugins() {
		API_PLUGINS = new PluginLoader<SynchronizerGuiPlugin>(
				SynchronizerGuiPlugin.class);
		
		API_PLUGINS.addPlugin(null, DummyGuiPlugin.getInstance());
		
		File pluginsFolder = new File(RESOURCES_FOLDER
				+ File.separator
				+ "plugins");
		
		if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
			File[] pluginFiles = pluginsFolder.listFiles();
			
			for (File file : pluginFiles) {
				try {
					PluginsUtils.loadPlugin(file);
				} catch (PluginException e) {
					GuiLogger.getLogger().warning(e.getMessage());
				} catch (Exception e) {
					GuiLogger.getLogger().warning("Plugin unknown exception");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void stop() {
		// TODO: cannot exit if syncing
		
		Boolean syncExit = Main.SETTINGS.getBooleanProperty("synchronizer.sync_exit");
		if (syncExit != null && syncExit)
			new ActionSynchronize(false).synchronize();
		
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
				new TaskFactoryXMLCoder().encode(new FileOutputStream(
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
			MainFrame.getInstance().getFrame().setVisible(false);
		} finally {
			try {
				if (LOG_FILE != null) {
					System.setOut(ORIGINAL_OUT_STREAM);
					System.setErr(ORIGINAL_ERR_STREAM);
					
					if (NEW_STREAM != null)
						NEW_STREAM.close();
					
					if (LOG_FILE_STREAM != null)
						LOG_FILE_STREAM.close();
					
					File logFile = new File(DATA_FOLDER
							+ File.separator
							+ "taskunifier.log");
					
					if (!logFile.exists())
						logFile.createNewFile();
					
					String logFileContent = FileUtils.readFileToString(
							logFile,
							"UTF-8");
					String log = FileUtils.readFileToString(LOG_FILE, "UTF-8");
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
	
	public static void saveSettings() throws FileNotFoundException, IOException {
		SETTINGS.store(new FileOutputStream(DATA_FOLDER
				+ File.separator
				+ "settings.properties"), Constants.TITLE + " Settings");
	}
	
}
