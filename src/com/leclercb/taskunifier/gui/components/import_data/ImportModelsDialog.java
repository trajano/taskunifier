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
