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
package com.leclercb.taskunifier.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SkinInfo;

import com.leclercb.taskunifier.api.models.coders.ContextFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.FolderFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.GoalFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.TaskFactoryXMLCoder;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.lookandfeel.types.SubstanceLookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.searcher.coder.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class Main {

	public static String RESOURCE_FOLDER;
	public static String DATA_FOLDER;

	public static void main(String [] args) {
		try {
			loadResourceFolder();
			boolean dataFolderCreated = loadDataFolder();
			loadSettings(dataFolderCreated);
			loadLocale();
			loadModels();
			loadLookAndFeel();
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
				String lookAndFeel = Settings.getStringProperty("theme.lookandfeel");

				try {
					if (lookAndFeel != null) {
						LookAndFeelDescriptor laf = LookAndFeelUtils.getLookAndFeel(lookAndFeel);
						if (laf != null)
							laf.setLookAndFeel();
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(
							null, 
							e.getMessage(), 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				MainFrame.getInstance().getFrame().setVisible(true);
			}
		});
	}

	private static void loadResourceFolder() throws Exception {
		if (System.getProperty("com.leclercb.taskunifier.resource_folder") == null) {
			RESOURCE_FOLDER = "resources";
		} else {
			RESOURCE_FOLDER = System.getProperty("com.leclercb.taskunifier.resource_folder");
		}

		File file = new File(RESOURCE_FOLDER);
		if (!file.exists() || !file.isDirectory())
			throw new Exception("Resource folder \"" + RESOURCE_FOLDER + "\" does not exist");
	}

	private static boolean loadDataFolder() throws Exception {
		if (System.getProperty("com.leclercb.taskunifier.data_folder") == null) {
			DATA_FOLDER = "data";
		} else {
			DATA_FOLDER = System.getProperty("com.leclercb.taskunifier.data_folder");
		}

		File file = new File(DATA_FOLDER);
		if (!file.exists()) {
			int response = JOptionPane.showConfirmDialog(
					null, 
					"Create data folder \"" + DATA_FOLDER + "\" ?", 
					"Create data folder", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);

			if (response == JOptionPane.NO_OPTION)
				throw new Exception("You need a data folder in order to start " + Constants.TITLE);

			if (!file.mkdir())
				throw new Exception("Error while creating folder \"" + DATA_FOLDER + "\"");

			return true;
		} else if (!file.isDirectory()) {
			throw new Exception("\"" + DATA_FOLDER + "\" is not a folder");
		}

		return false;
	}

	private static void loadSettings(boolean dataFolderCreated) throws Exception {
		try {
			Settings.load(new FileInputStream(DATA_FOLDER + File.separator + "settings.properties"));
		} catch (FileNotFoundException e) {
			Settings.load(Main.class.getResourceAsStream("default_settings.properties"));

			if (!dataFolderCreated)
				JOptionPane.showMessageDialog(
						null, 
						"Settings file not found. A default settings file is loaded.",
						"Error", 
						JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void loadLocale() throws Exception {
		Locale.setDefault(Settings.getLocaleProperty("general.locale"));
		Translations.initialize(Settings.getLocaleProperty("general.locale"));
	}

	private static void loadModels() throws Exception {
		try {
			new ContextFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER + File.separator + "contexts.xml"));
		} catch (FileNotFoundException e) {}

		try {
			new FolderFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER + File.separator + "folders.xml"));
		} catch (FileNotFoundException e) {}

		try {
			new GoalFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER + File.separator + "goals.xml"));
		} catch (FileNotFoundException e) {}

		try {
			new TaskFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER + File.separator + "tasks.xml"));
		} catch (FileNotFoundException e) {}

		try {
			new TaskSearcherFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER + File.separator + "searchers.xml"));
		} catch (FileNotFoundException e) {}
	}

	private static void loadLookAndFeel() throws Exception {
		try {
			Class.forName("org.pushingpixels.substance.api.SubstanceLookAndFeel");

			Map<String, SkinInfo> lafs = SubstanceLookAndFeel.getAllSkins();
			for (SkinInfo laf : lafs.values())
				LookAndFeelUtils.addLookAndFeel(new SubstanceLookAndFeelDescriptor(laf.getDisplayName(), laf.getClassName()));
		} catch (ClassNotFoundException exc) {}
	}

	public static void stop() {
		try {
			new ContextFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER + File.separator + "contexts.xml"));
			new FolderFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER + File.separator + "folders.xml"));
			new GoalFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER + File.separator + "goals.xml"));
			new TaskFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER + File.separator + "tasks.xml"));
			new TaskSearcherFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER + File.separator + "searchers.xml"));

			saveSettings();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null, 
					e.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		System.exit(0);
	}

	public static void saveSettings() throws FileNotFoundException, IOException {
		Settings.save(new FileOutputStream(DATA_FOLDER + File.separator + "settings.properties"));
	}

}
