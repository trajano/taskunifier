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
package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.leclercb.taskunifier.api.models.coders.ContextFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.FolderFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.GoalFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.LocationFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.TaskFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportModelsDialog extends AbstractExportDialog {
	
	public ExportModelsDialog(Frame frame, boolean modal) {
		super(
				Translations.getString("general.export_models"),
				frame,
				modal,
				"zip",
				Translations.getString("general.zip_files"));
	}
	
	@Override
	protected void exportToFile(String file) throws Exception {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
		
		// CONTEXTS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new ContextFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(zos, "contexts.xml", new ByteArrayInputStream(
					output.toByteArray()));
		}
		
		// FOLDERS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new FolderFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(zos, "folders.xml", new ByteArrayInputStream(
					output.toByteArray()));
		}
		
		// GOALS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new GoalFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(
					zos,
					"goals.xml",
					new ByteArrayInputStream(output.toByteArray()));
		}
		
		// LOCATIONS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new LocationFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(zos, "locations.xml", new ByteArrayInputStream(
					output.toByteArray()));
		}
		
		// TASKS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new TaskFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(
					zos,
					"tasks.xml",
					new ByteArrayInputStream(output.toByteArray()));
		}
		
		zos.close();
	}
	
	private void writeIntoZip(
			ZipOutputStream output,
			String name,
			InputStream input) throws Exception {
		output.putNextEntry(new ZipEntry(name));
		
		int size = 0;
		byte[] buffer = new byte[1024];
		
		while ((size = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, size);
		}
		
		output.closeEntry();
		input.close();
	}
	
}
