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
package com.leclercb.taskunifier.gui.components.export_data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.leclercb.taskunifier.api.models.coders.NoteFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiContextFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiFolderFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiGoalFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiLocationFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiTaskFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ExportModelsDialog extends AbstractExportDialog {
	
	private static ExportModelsDialog INSTANCE;
	
	public static ExportModelsDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ExportModelsDialog();
		
		return INSTANCE;
	}
	
	private ExportModelsDialog() {
		super(
				Translations.getString("general.export_models"),
				"zip",
				Translations.getString("general.zip_files"));
	}
	
	@Override
	protected void exportToFile(String file) throws Exception {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
		
		// CONTEXTS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new GuiContextFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(zos, "contexts.xml", new ByteArrayInputStream(
					output.toByteArray()));
		}
		
		// FOLDERS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new GuiFolderFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(zos, "folders.xml", new ByteArrayInputStream(
					output.toByteArray()));
		}
		
		// GOALS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new GuiGoalFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(
					zos,
					"goals.xml",
					new ByteArrayInputStream(output.toByteArray()));
		}
		
		// LOCATIONS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new GuiLocationFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(zos, "locations.xml", new ByteArrayInputStream(
					output.toByteArray()));
		}
		
		// NOTES
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new NoteFactoryXMLCoder().encode(output);
			
			this.writeIntoZip(
					zos,
					"notes.xml",
					new ByteArrayInputStream(output.toByteArray()));
		}
		
		// TASKS
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			new GuiTaskFactoryXMLCoder().encode(output);
			
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
