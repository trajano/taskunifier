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
package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.coders.ContextFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.FolderFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.GoalFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.LocationFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.TaskFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ImportModelsDialog extends AbstractImportDialog {
	
	public ImportModelsDialog(Frame frame, boolean modal) {
		super(
				Translations.getString("general.import_models"),
				frame,
				modal,
				true,
				"zip",
				Translations.getString("general.zip_files"));
	}
	
	@Override
	protected void deleteExistingValue() {
		SynchronizerUtils.resetSynchronizerAndDeleteModels();
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void importFromFile(String file) throws Exception {
		ZipFile zip = new ZipFile(new File(file));
		
		// TODO: fill in the list
		List<Model> models = new ArrayList<Model>();
		
		for (Enumeration<?> e = zip.getEntries(); e.hasMoreElements();) {
			ZipArchiveEntry entry = (ZipArchiveEntry) e.nextElement();
			
			if (entry.getName().equals("contexts.xml"))
				new ContextFactoryXMLCoder().decode(zip.getInputStream(entry));
			
			if (entry.getName().equals("folders.xml"))
				new FolderFactoryXMLCoder().decode(zip.getInputStream(entry));
			
			if (entry.getName().equals("goals.xml"))
				new GoalFactoryXMLCoder().decode(zip.getInputStream(entry));
			
			if (entry.getName().equals("locations.xml"))
				new LocationFactoryXMLCoder().decode(zip.getInputStream(entry));
			
			if (entry.getName().equals("tasks.xml"))
				new TaskFactoryXMLCoder().decode(zip.getInputStream(entry));
		}
		
		String[] options = new String[] {
				Translations.getString("general.yes"),
				Translations.getString("general.no") };
		
		int result = -1;
		
		if (false) {
			result = JOptionPane.showOptionDialog(
					this.getOwner(),
					"Flag all the imported data as new data ? (Allow you to sync/add them with/to another service provider)",
					Translations.getString("general.question"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
		}
		
		if (result == 0) {
			for (Model model : models) {
				if (model.getModelStatus() == ModelStatus.LOADED
						|| model.getModelStatus() == ModelStatus.TO_UPDATE) {
					// TODO: won't work because cannot replace existing id by
					// new id
					model.setModelId(new ModelId());
					model.setModelStatus(ModelStatus.TO_UPDATE);
				}
			}
		}
	}
	
}
