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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang3.SystemUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.plugins.PluginLoader;
import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.SingleInstanceUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.commons.gui.swing.lookandfeel.types.DefaultLookAndFeelDescriptor;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.api.settings.ModelIdSettingsCoder;
import com.leclercb.taskunifier.gui.actions.ActionCheckPluginVersion;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionImportComFile;
import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.actions.ActionReview;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.api.models.GuiContact;
import com.leclercb.taskunifier.gui.api.models.GuiContext;
import com.leclercb.taskunifier.gui.api.models.GuiFolder;
import com.leclercb.taskunifier.gui.api.models.GuiGoal;
import com.leclercb.taskunifier.gui.api.models.GuiLocation;
import com.leclercb.taskunifier.gui.api.models.GuiNote;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.api.models.beans.ComBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiContactBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiContextBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiFolderBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiGoalBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiLocationBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiNoteBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiTaskBean;
import com.leclercb.taskunifier.gui.api.plugins.PluginsUtils;
import com.leclercb.taskunifier.gui.api.plugins.exc.PluginException;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizingException;
import com.leclercb.taskunifier.gui.components.tips.TipsDialog;
import com.leclercb.taskunifier.gui.components.welcome.LanguageDialog;
import com.leclercb.taskunifier.gui.components.welcome.WelcomeDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.plugins.PluginLogger;
import com.leclercb.taskunifier.gui.resources.Resources;
import com.leclercb.taskunifier.gui.settings.SettingsVersion;
import com.leclercb.taskunifier.gui.settings.UserSettingsVersion;
import com.leclercb.taskunifier.gui.swing.lookandfeel.JTattooLookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.BackupUtils;
import com.leclercb.taskunifier.gui.utils.CommunicatorUtils;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.UserUtils;

public class Main {
	
	private static boolean QUIT;
	
	private static boolean DEVELOPER_MODE;
	private static boolean FIRST_EXECUTION;
	
	private static String USER_ID;
	
	private static String RESOURCES_FOLDER;
	private static String DATA_FOLDER;
	private static String USER_FOLDER;
	private static String BACKUP_FOLDER;
	private static String PLUGINS_FOLDER;
	
	private static PropertyMap INIT_SETTINGS;
	private static PropertyMap SETTINGS;
	private static PropertyMap USER_SETTINGS;
	
	private static PluginLoader<SynchronizerGuiPlugin> API_PLUGINS;
	
	public static ActionSupport AFTER_START;
	public static ActionSupport BEFORE_EXIT;
	
	public static String getUserId() {
		return USER_ID;
	}
	
	public static boolean isDeveloperMode() {
		return DEVELOPER_MODE;
	}
	
	private static void setDeveloperMode(boolean developerMode) {
		DEVELOPER_MODE = developerMode;
	}
	
	public static boolean isFirstExecution() {
		return FIRST_EXECUTION;
	}
	
	private static void setFirstExecution(boolean firstExecution) {
		FIRST_EXECUTION = firstExecution;
	}
	
	public static String getInitSettingsFile() {
		return RESOURCES_FOLDER + File.separator + "taskunifier.properties";
	}
	
	public static String getSettingsFile() {
		return DATA_FOLDER + File.separator + "settings.properties";
	}
	
	public static String getUserSettingsFile() {
		return USER_FOLDER + File.separator + "settings.properties";
	}
	
	public static String getResourcesFolder() {
		return RESOURCES_FOLDER;
	}
	
	public static String getDataFolder() {
		return DATA_FOLDER;
	}
	
	public static String getUserFolder() {
		return USER_FOLDER;
	}
	
	public static String getUserFolder(String userId) {
		return getDataFolder()
				+ File.separator
				+ "users"
				+ File.separator
				+ userId;
	}
	
	public static String getBackupFolder() {
		return BACKUP_FOLDER;
	}
	
	public static String getPluginsFolder() {
		return PLUGINS_FOLDER;
	}
	
	public static PropertyMap getInitSettings() {
		return INIT_SETTINGS;
	}
	
	public static PropertyMap getSettings() {
		return SETTINGS;
	}
	
	public static PropertyMap getUserSettings() {
		return USER_SETTINGS;
	}
	
	public static PluginLoader<SynchronizerGuiPlugin> getApiPlugins() {
		return API_PLUGINS;
	}
	
	public static void main(final String[] args) {
		try {
			checkSingleInstance();
		} catch (RuntimeException e) {
			secondaryMain(args);
			throw e;
		}
		
		boolean outdatedPlugins;
		
		try {
			initialize();
			loadDeveloperMode();
			loadResourceFolder();
			loadInitSettings();
			loadDataFolder();
			loadPluginsFolder();
			loadLoggers();
			loadSettings();
			loadUserId();
			loadUserFolder();
			loadBackupFolder();
			SettingsVersion.updateSettings();
			loadUserSettings();
			UserSettingsVersion.updateSettings();
			loadLoggerLevels();
			loadProxies();
			loadLocale();
			loadModels();
			loadLookAndFeel();
			outdatedPlugins = loadApiPlugins();
			loadSynchronizer();
			loadShutdownHook();
			
			Main.getSettings().setStringProperty(
					"general.version",
					Constants.VERSION);
			
			autoBackup();
			cleanBackups();
			
			Constants.initialize();
			
			AFTER_START.fireActionPerformed(0, "AFTER_START");
		} catch (Exception e) {
			GuiLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
			
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
				try {
					String lookAndFeel = SETTINGS.getStringProperty("theme.lookandfeel");
					
					if (lookAndFeel != null) {
						LookAndFeelDescriptor laf = LookAndFeelUtils.getLookAndFeel(lookAndFeel);
						if (laf != null)
							laf.setLookAndFeel();
					} else {
						LookAndFeelDescriptor laf = LookAndFeelUtils.getLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						
						if (SystemUtils.IS_OS_WINDOWS)
							laf = LookAndFeelUtils.getLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel$Default");
						
						if (SystemUtils.IS_OS_LINUX)
							laf = LookAndFeelUtils.getLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel$Blue");
						
						if (laf != null) {
							laf.setLookAndFeel();
							
							SETTINGS.setStringProperty(
									"theme.lookandfeel",
									laf.getIdentifier());
						}
					}
				} catch (Throwable t) {
					GuiLogger.getLogger().log(
							Level.WARNING,
							"Error while setting look and feel",
							t);
					
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							t.getMessage(),
							null,
							null,
							t,
							null,
							null);
					
					JXErrorPane.showDialog(null, info);
				}
				
				try {
					if (isFirstExecution()) {
						new LanguageDialog(null).setVisible(true);
						MainFrame.getInstance();
						new WelcomeDialog(null).setVisible(true);
					}
					
					MainFrame.getInstance().getFrame().setVisible(true);
					ActionCheckVersion.checkVersion(true);
					ActionCheckPluginVersion.checkPluginVersion(true);
					
					Boolean showed = SETTINGS.getBooleanProperty("review.showed");
					if (showed == null || !showed)
						ActionReview.review();
					
					SETTINGS.setBooleanProperty("review.showed", true);
					
					if (finalOutdatedPlugins)
						ActionManagePlugins.managePlugins();
					
					TipsDialog.getInstance().showTipsDialog(true);
					
					handleArguments(args);
					
					Boolean syncStart = USER_SETTINGS.getBooleanProperty("synchronizer.sync_start");
					if (syncStart != null && syncStart)
						ActionSynchronize.synchronize(false);
				} catch (Throwable t) {
					GuiLogger.getLogger().log(
							Level.WARNING,
							"Error while loading gui",
							t);
					
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							"Error while loading gui",
							null,
							null,
							t,
							null,
							null);
					
					JXErrorPane.showDialog(null, info);
					
					QUIT = true;
					System.exit(1);
				}
			}
			
		});
	}
	
	private static void secondaryMain(String[] args) {
		try {
			loadDeveloperMode();
			loadResourceFolder();
			loadInitSettings();
			loadDataFolder();
			loadBackupFolder();
			loadPluginsFolder();
			loadSettings();
			
			ComBean bean = new ComBean();
			bean.setApplicationName(Constants.TITLE);
			bean.setArguments(args);
			
			CommunicatorUtils.send(
					bean,
					"127.0.0.1",
					SETTINGS.getIntegerProperty("general.communicator.port"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void handleArguments(String[] args) {
		if (args == null)
			return;
		
		for (String arg : args) {
			if (arg == null)
				continue;
			
			ActionImportComFile.importComFile(new File(arg));
		}
	}
	
	private static void checkSingleInstance() {
		if (!SingleInstanceUtils.isSingleInstance()) {
			String message = "There is another instance of "
					+ Constants.TITLE
					+ " running.";
			
			throw new RuntimeException(message);
		}
	}
	
	private static void loadDeveloperMode() {
		String developerMode = System.getProperty("com.leclercb.taskunifier.developer_mode");
		setDeveloperMode("true".equals(developerMode));
		
		if (isDeveloperMode())
			GuiLogger.getLogger().severe("DEVELOPER MODE");
	}
	
	private static void initialize() throws Exception {
		setDeveloperMode(false);
		setFirstExecution(false);
		
		Properties defaultProperties = null;
		
		defaultProperties = new Properties();
		defaultProperties.load(Resources.class.getResourceAsStream("default_settings.properties"));
		
		SETTINGS = new PropertyMap(
				new Properties(defaultProperties),
				defaultProperties);
		
		SETTINGS.addCoder(new ModelIdSettingsCoder());
		
		defaultProperties = new Properties();
		defaultProperties.load(Resources.class.getResourceAsStream("default_user_settings.properties"));
		
		USER_SETTINGS = new PropertyMap(
				new Properties(defaultProperties),
				defaultProperties);
		
		USER_SETTINGS.addCoder(new ModelIdSettingsCoder());
		
		AFTER_START = new ActionSupport(Main.class);
		BEFORE_EXIT = new ActionSupport(Main.class);
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
		INIT_SETTINGS = new PropertyMap(new Properties());
		
		try {
			INIT_SETTINGS.load(new FileInputStream(getInitSettingsFile()));
		} catch (FileNotFoundException e) {
			try {
				new File(getInitSettingsFile()).createNewFile();
			} catch (Throwable t) {
				
			}
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading init settings",
					e);
		}
	}
	
	private static void loadDataFolder() throws Exception {
		DATA_FOLDER = getInitSettings().getStringProperty(
				"com.leclercb.taskunifier.data_folder");
		
		if (DATA_FOLDER == null)
			DATA_FOLDER = System.getProperty("com.leclercb.taskunifier.data_folder");
		
		if (DATA_FOLDER == null)
			DATA_FOLDER = "data";
		
		if (loadFolder(DATA_FOLDER))
			setFirstExecution(true);
		
		loadFolder(DATA_FOLDER + File.separator + "users");
	}
	
	private static void loadUserFolder() throws Exception {
		USER_FOLDER = getUserFolder(USER_ID);
		loadFolder(USER_FOLDER);
	}
	
	private static void loadBackupFolder() throws Exception {
		BACKUP_FOLDER = USER_FOLDER + File.separator + "backup";
		loadFolder(BACKUP_FOLDER);
	}
	
	private static void loadPluginsFolder() throws Exception {
		PLUGINS_FOLDER = DATA_FOLDER + File.separator + "plugins";
		loadFolder(PLUGINS_FOLDER);
	}
	
	public static boolean loadFolder(String f) throws Exception {
		File folder = new File(f);
		if (!folder.exists()) {
			if (!folder.mkdir())
				throw new Exception(String.format(
						"Error while creating folder \"%1s\"",
						f));
			
			try {
				folder.setExecutable(true, true);
				folder.setReadable(true, true);
				folder.setWritable(true, true);
			} catch (Throwable t) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						String.format(
								"Cannot change folder permissions \"%1s\"",
								f),
						t);
			}
			
			return true;
		} else if (!folder.isDirectory()) {
			throw new Exception(String.format("\"%1s\" is not a folder", f));
		}
		
		return false;
	}
	
	private static void loadLoggers() {
		Level apiLogLevel = Level.INFO;
		Level guiLogLevel = Level.INFO;
		Level pluginLogLevel = Level.INFO;
		
		String apiLogFile = getDataFolder()
				+ File.separator
				+ "taskunifier_api.log";
		String guiLogFile = getDataFolder()
				+ File.separator
				+ "taskunifier_gui.log";
		String pluginLogFile = getDataFolder()
				+ File.separator
				+ "taskunifier_plugin.log";
		
		apiLogFile = apiLogFile.replace("%", "%%");
		guiLogFile = guiLogFile.replace("%", "%%");
		pluginLogFile = pluginLogFile.replace("%", "%%");
		
		try {
			FileHandler handler = new FileHandler(apiLogFile, 50000, 1, true);
			
			handler.setLevel(apiLogLevel);
			handler.setFormatter(new SimpleFormatter());
			
			ApiLogger.getLogger().addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileHandler handler = new FileHandler(guiLogFile, 50000, 1, true);
			
			handler.setLevel(guiLogLevel);
			handler.setFormatter(new SimpleFormatter());
			
			GuiLogger.getLogger().addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileHandler handler = new FileHandler(pluginLogFile, 50000, 1, true);
			
			handler.setLevel(pluginLogLevel);
			handler.setFormatter(new SimpleFormatter());
			
			PluginLogger.getLogger().addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadSettings() throws Exception {
		try {
			SETTINGS.load(new FileInputStream(getSettingsFile()));
		} catch (Exception e) {
			SETTINGS.load(Resources.class.getResourceAsStream("default_settings.properties"));
			
			if (!isFirstExecution())
				JOptionPane.showMessageDialog(
						null,
						"Settings file not found. A default settings file is loaded.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			
			setFirstExecution(true);
		}
	}
	
	private static void loadUserId() throws Exception {
		USER_ID = SETTINGS.getStringProperty("general.user.last_user_id");
		
		String[] userIds = UserUtils.getInstance().getUserIds();
		for (String userId : userIds) {
			if (EqualsUtils.equals(USER_ID, userId))
				return;
		}
		
		if (userIds.length == 0) {
			USER_ID = UserUtils.getInstance().createNewUser("Default");
			return;
		}
		
		USER_ID = userIds[0];
	}
	
	private static void loadUserSettings() throws Exception {
		try {
			USER_SETTINGS.load(new FileInputStream(getUserSettingsFile()));
		} catch (Exception e) {
			USER_SETTINGS.load(Resources.class.getResourceAsStream("default_user_settings.properties"));
		}
	}
	
	private static void reloadUserSettings() throws Exception {
		USER_SETTINGS.clear();
		
		try {
			USER_SETTINGS.load(new FileInputStream(getUserSettingsFile()));
		} catch (Exception e) {
			USER_SETTINGS.load(Resources.class.getResourceAsStream("default_user_settings.properties"));
		}
	}
	
	private static void loadLoggerLevels() {
		try {
			Level apiLogLevel = Level.parse(SETTINGS.getStringProperty("logger.api.level"));
			Level guiLogLevel = Level.parse(SETTINGS.getStringProperty("logger.gui.level"));
			Level pluginLogLevel = Level.parse(SETTINGS.getStringProperty("logger.plugin.level"));
			
			Handler[] handlers;
			
			handlers = ApiLogger.getLogger().getHandlers();
			for (Handler handler : handlers)
				handler.setLevel(apiLogLevel);
			
			handlers = GuiLogger.getLogger().getHandlers();
			for (Handler handler : handlers)
				handler.setLevel(guiLogLevel);
			
			handlers = PluginLogger.getLogger().getHandlers();
			for (Handler handler : handlers)
				handler.setLevel(pluginLogLevel);
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading logger levels",
					t);
		}
	}
	
	private static void loadProxies() {
		boolean p = SETTINGS.getBooleanProperty("proxy.use_system_proxies");
		System.setProperty("java.net.useSystemProxies", p + "");
		
		SETTINGS.addPropertyChangeListener(
				"proxy.use_system_proxies",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						boolean p = SETTINGS.getBooleanProperty("proxy.use_system_proxies");
						System.setProperty("java.net.useSystemProxies", p + "");
					}
					
				});
	}
	
	private static void loadLocale() throws Exception {
		try {
			Translations.setLocale(SETTINGS.getLocaleProperty("general.locale"));
		} catch (Throwable t) {
			SETTINGS.remove("general.locale");
			Translations.setLocale(Translations.DEFAULT_LOCALE);
		}
		
		SETTINGS.setLocaleProperty("general.locale", Translations.getLocale());
	}
	
	private static void loadModels() throws Exception {
		ContactFactory.initializeWithClass(
				GuiContact.class,
				GuiContactBean.class);
		ContextFactory.initializeWithClass(
				GuiContext.class,
				GuiContextBean.class);
		FolderFactory.initializeWithClass(GuiFolder.class, GuiFolderBean.class);
		GoalFactory.initializeWithClass(GuiGoal.class, GuiGoalBean.class);
		LocationFactory.initializeWithClass(
				GuiLocation.class,
				GuiLocationBean.class);
		NoteFactory.initializeWithClass(GuiNote.class, GuiNoteBean.class);
		TaskFactory.initializeWithClass(GuiTask.class, GuiTaskBean.class);
		
		loadAllData(getUserFolder());
	}
	
	public static void loadAllData(String folder) {
		loadModels(folder);
		loadTaskTemplates(folder);
		loadTaskSearchers(folder);
	}
	
	public static void loadModels(String folder) {
		try {
			ContactFactory.getInstance().deleteAll();
			
			ContactFactory.getInstance().decodeFromXML(
					new FileInputStream(folder
							+ File.separator
							+ "contacts.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading contacts",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			ContextFactory.getInstance().deleteAll();
			
			ContextFactory.getInstance().decodeFromXML(
					new FileInputStream(folder
							+ File.separator
							+ "contexts.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading contexts",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			FolderFactory.getInstance().deleteAll();
			
			FolderFactory.getInstance().decodeFromXML(
					new FileInputStream(folder + File.separator + "folders.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading folders",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			GoalFactory.getInstance().deleteAll();
			
			GoalFactory.getInstance().decodeFromXML(
					new FileInputStream(folder + File.separator + "goals.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading goals",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			LocationFactory.getInstance().deleteAll();
			
			LocationFactory.getInstance().decodeFromXML(
					new FileInputStream(folder
							+ File.separator
							+ "locations.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading locations",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			NoteFactory.getInstance().deleteAll();
			
			NoteFactory.getInstance().decodeFromXML(
					new FileInputStream(folder + File.separator + "notes.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading notes",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			TaskFactory.getInstance().deleteAll();
			
			TaskFactory.getInstance().decodeFromXML(
					new FileInputStream(folder + File.separator + "tasks.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading tasks",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void loadTaskTemplates(String folder) {
		try {
			TaskTemplateFactory.getInstance().deleteAll();
			
			TaskTemplateFactory.getInstance().decodeFromXML(
					new FileInputStream(folder
							+ File.separator
							+ "task_templates.xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading task templates",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void loadTaskSearchers(String folder) {
		try {
			TaskSearcherFactory.getInstance().deleteAll();
			
			new TaskSearcherFactoryXMLCoder().decode(new FileInputStream(folder
					+ File.separator
					+ "task_searchers.xml"));
		} catch (FileNotFoundException e) {
			ActionResetGeneralSearchers.resetGeneralSearchers();
		} catch (Throwable e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading task searchers",
					e);
			
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
		
		for (Object key : jgoodies.keySet()) {
			LookAndFeelUtils.addLookAndFeel(new DefaultLookAndFeelDescriptor(
					"jGoodies - " + jgoodies.getProperty(key.toString()),
					key.toString()));
		}
		
		// jTattoo
		Properties jtattoo = new Properties();
		jtattoo.load(Resources.class.getResourceAsStream("jtattoo_themes.properties"));
		
		for (Object key : jtattoo.keySet()) {
			LookAndFeelUtils.addLookAndFeel(new JTattooLookAndFeelDescriptor(
					"jTattoo - " + jtattoo.getProperty(key.toString()),
					key.toString()));
		}
	}
	
	private static boolean loadApiPlugins() {
		API_PLUGINS = new PluginLoader<SynchronizerGuiPlugin>(
				SynchronizerGuiPlugin.class);
		
		API_PLUGINS.addPlugin(null, DummyGuiPlugin.getInstance());
		
		File pluginsFolder = new File(getPluginsFolder());
		
		boolean outdatedPlugins = false;
		File[] pluginFiles = pluginsFolder.listFiles();
		
		for (File file : pluginFiles) {
			try {
				PluginsUtils.loadPlugin(file);
			} catch (PluginException e) {
				switch (e.getType()) {
					case MORE_THAN_ONE_PLUGIN:
					case NO_VALID_PLUGIN:
					case OUTDATED_PLUGIN:
						outdatedPlugins = true;
				}
				
				GuiLogger.getLogger().warning(e.getMessage());
			} catch (Throwable t) {
				GuiLogger.getLogger().log(
						Level.WARNING,
						"Unknown plugin error",
						t);
			}
		}
		
		USER_SETTINGS.setStringProperty(
				"api.id",
				SynchronizerUtils.getPlugin().getId());
		
		API_PLUGINS.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent evt) {
				SynchronizerGuiPlugin plugin = (SynchronizerGuiPlugin) evt.getValue();
				
				if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED) {
					USER_SETTINGS.setStringProperty("api.id", plugin.getId());
				}
				
				if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
					if (EqualsUtils.equals(
							USER_SETTINGS.getStringProperty("api.id"),
							plugin.getId()))
						USER_SETTINGS.setStringProperty(
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
	
	private static void loadShutdownHook() {
		QUIT = false;
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				quit();
			}
			
		});
	}
	
	private static void autoBackup() {
		int nbHours = SETTINGS.getIntegerProperty("general.backup.auto_backup_every");
		BackupUtils.getInstance().autoBackup(nbHours);
	}
	
	private static void cleanBackups() {
		int nbToKeep = SETTINGS.getIntegerProperty("general.backup.keep_backups");
		BackupUtils.getInstance().cleanBackups(nbToKeep);
	}
	
	public static void quit() {
		if (Synchronizing.isSynchronizing()) {
			JOptionPane.showMessageDialog(
					null,
					Translations.getString("general.synchronization_ongoing"),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		synchronized (Main.class) {
			if (QUIT)
				return;
			
			QUIT = true;
		}
		
		Boolean syncExit = USER_SETTINGS.getBooleanProperty("synchronizer.sync_exit");
		if (syncExit != null && syncExit)
			ActionSynchronize.synchronize(false);
		
		BEFORE_EXIT.fireActionPerformed(0, "BEFORE_EXIT");
		
		SETTINGS.setStringProperty("general.user.last_user_id", USER_ID);
		
		SETTINGS.setCalendarProperty(
				"general.last_exit_date",
				Calendar.getInstance());
		
		saveAllData();
		
		MainFrame.getInstance().getFrame().dispose();
		
		GuiLogger.getLogger().info("Exiting " + Constants.TITLE);
		
		System.exit(0);
	}
	
	public static void copyAllData(String folder) {
		saveModels(folder);
		saveTaskTemplates(folder);
		saveTaskSearchers(folder);
	}
	
	public static void saveAllData() {
		saveModels(getUserFolder());
		saveTaskTemplates(getUserFolder());
		saveTaskSearchers(getUserFolder());
		saveInitSettings();
		saveSettings();
		saveUserSettings();
	}
	
	public static void saveInitSettings() {
		try {
			File f = new File(getInitSettingsFile());
			
			if (!isDeveloperMode() && f.exists() && f.canWrite()) {
				INIT_SETTINGS.store(
						new FileOutputStream(getInitSettingsFile()),
						Constants.TITLE + " Init Settings");
				
				GuiLogger.getLogger().log(Level.INFO, "Saving init settings");
			}
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving init settings",
					e);
		}
	}
	
	public static void saveSettings() {
		try {
			SETTINGS.store(
					new FileOutputStream(getSettingsFile()),
					Constants.TITLE + " Settings");
			
			GuiLogger.getLogger().log(Level.INFO, "Saving settings");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving settings",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void saveUserSettings() {
		try {
			USER_SETTINGS.store(
					new FileOutputStream(getUserSettingsFile()),
					Constants.TITLE + " User Settings");
			
			GuiLogger.getLogger().log(Level.INFO, "Saving user settings");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving user settings",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void saveModels(String folder) {
		try {
			ContactFactory.getInstance().cleanFactory();
			ContextFactory.getInstance().cleanFactory();
			FolderFactory.getInstance().cleanFactory();
			GoalFactory.getInstance().cleanFactory();
			LocationFactory.getInstance().cleanFactory();
			NoteFactory.getInstance().cleanFactory();
			TaskFactory.getInstance().cleanFactory();
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while cleaning factories",
					e);
		}
		
		try {
			ContactFactory.getInstance().encodeToXML(
					new FileOutputStream(folder
							+ File.separator
							+ "contacts.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving contacts (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving contacts",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			ContextFactory.getInstance().encodeToXML(
					new FileOutputStream(folder
							+ File.separator
							+ "contexts.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving contexts (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving contexts",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			FolderFactory.getInstance().encodeToXML(
					new FileOutputStream(folder
							+ File.separator
							+ "folders.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving folders (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving folders",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			GoalFactory.getInstance().encodeToXML(
					new FileOutputStream(folder + File.separator + "goals.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving goals (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving goals",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			LocationFactory.getInstance().encodeToXML(
					new FileOutputStream(folder
							+ File.separator
							+ "locations.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving locations (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving locations",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			NoteFactory.getInstance().encodeToXML(
					new FileOutputStream(folder + File.separator + "notes.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving notes (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving notes",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			TaskFactory.getInstance().encodeToXML(
					new FileOutputStream(folder + File.separator + "tasks.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving tasks (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving tasks",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void saveTaskTemplates(String folder) {
		try {
			TaskTemplateFactory.getInstance().encodeToXML(
					new FileOutputStream(folder
							+ File.separator
							+ "task_templates.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving task templates (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving task templates",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void saveTaskSearchers(String folder) {
		try {
			new TaskSearcherFactoryXMLCoder().encode(new FileOutputStream(
					folder + File.separator + "task_searchers.xml"));
			
			GuiLogger.getLogger().log(
					Level.INFO,
					"Saving task searchers (" + folder + ")");
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving task searchers",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static boolean changeUser(String userId) {
		saveAllData();
		
		boolean set = false;
		
		try {
			set = Synchronizing.setSynchronizing(true);
		} catch (SynchronizingException e) {
			
		}
		
		if (!set) {
			return false;
		}
		
		boolean result = false;
		
		String oldUserId = USER_ID;
		
		try {
			USER_ID = userId;
			loadUserFolder();
			
			SynchronizerUtils.resetSynchronizerAndDeleteModels();
			
			reloadUserSettings();
			
			SynchronizerUtils.setTaskRepeatEnabled(false);
			loadAllData(getUserFolder());
			SynchronizerUtils.setTaskRepeatEnabled(true);
			
			result = true;
		} catch (Exception e) {
			USER_ID = oldUserId;
			USER_FOLDER = DATA_FOLDER
					+ File.separator
					+ "users"
					+ File.separator
					+ USER_ID;
			
			GuiLogger.getLogger().log(
					Level.SEVERE,
					String.format("Error while switching user %1s", userId),
					e);
		}
		
		if (set) {
			try {
				Synchronizing.setSynchronizing(false);
			} catch (SynchronizingException e) {
				
			}
		}
		
		return result;
	}
	
}
