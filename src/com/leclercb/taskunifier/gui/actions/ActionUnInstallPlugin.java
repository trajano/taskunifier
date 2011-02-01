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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionUnInstallPlugin extends AbstractAction {
	
	public ActionUnInstallPlugin() {
		this(32, 32);
	}
	
	public ActionUnInstallPlugin(int width, int height) {
		super(
				Translations.getString("action.name.uninstall_plugin"),
				Images.getResourceImage("upload.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.uninstall_plugin"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.unInstallPlugin();
	}
	
	public void unInstallPlugin() {
		File pluginsFolder = new File(Main.RESOURCES_FOLDER
				+ File.separator
				+ "plugins");
		
		if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
			File[] pluginsFiles = pluginsFolder.listFiles();
			
			List<File> files = new ArrayList<File>();
			for (File file : pluginsFiles)
				if (file.isFile()
						&& FileUtils.getExtention(file.getAbsolutePath()).equals(
								"jar"))
					files.add(file);
			
			File selectedFile = (File) JOptionPane.showInputDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("action.uninstall_plugin.which_plugin_to_uninstall"),
					Translations.getString("general.question"),
					JOptionPane.QUESTION_MESSAGE,
					null,
					files.toArray(),
					null);
			
			if (selectedFile != null) {
				Main.API_PLUGINS.unloadJar(selectedFile);
				selectedFile.deleteOnExit();
				
				JOptionPane.showMessageDialog(
						MainFrame.getInstance().getFrame(),
						Translations.getString("action.uninstall_plugin.plugin_uninstalled"),
						Translations.getString("general.information"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
}
