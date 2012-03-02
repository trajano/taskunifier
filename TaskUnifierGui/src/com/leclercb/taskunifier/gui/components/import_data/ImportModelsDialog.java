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

import java.io.File;
import java.util.Enumeration;

import javax.swing.SwingWorker;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizingException;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUWaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class ImportModelsDialog extends AbstractImportDialog {
	
	private static ImportModelsDialog INSTANCE;
	
	public static ImportModelsDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ImportModelsDialog();
		
		return INSTANCE;
	}
	
	private ImportModelsDialog() {
		super(
				Translations.getString("action.import_models"),
				true,
				"zip",
				Translations.getString("general.zip_files"),
				"import.models.file_name");
	}
	
	@Override
	protected void deleteExistingValue() {
		SynchronizerUtils.resetAllSynchronizersAndDeleteModels();
	}
	
	@Override
	protected void importFromFile(final String file) throws Exception {
		final TUWaitDialog dialog = new TUWaitDialog(
				MainFrame.getInstance().getFrame(),
				"action.import_models");
		
		dialog.setWorker(new SwingWorker<Void, Void>() {
			
			@Override
			protected Void doInBackground() throws Exception {
				boolean set = false;
				
				try {
					set = Synchronizing.setSynchronizing(true);
				} catch (SynchronizingException e) {
					
				}
				
				if (!set) {
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							Translations.getString("general.synchronization_ongoing"),
							null,
							null,
							null,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
					
					return null;
				}
				
				try {
					dialog.appendToProgressStatus(Translations.getString("action.import_models"));
					
					SynchronizerUtils.setTaskRepeatEnabled(false);
					
					ZipFile zip = new ZipFile(new File(file));
					
					for (Enumeration<?> e = zip.getEntries(); e.hasMoreElements();) {
						ZipArchiveEntry entry = (ZipArchiveEntry) e.nextElement();
						
						if (entry.getName().equals("contacts.xml"))
							ContactFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
						
						if (entry.getName().equals("contexts.xml"))
							ContextFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
						
						if (entry.getName().equals("folders.xml"))
							FolderFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
						
						if (entry.getName().equals("goals.xml"))
							GoalFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
						
						if (entry.getName().equals("locations.xml"))
							LocationFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
						
						if (entry.getName().equals("notes.xml"))
							NoteFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
						
						if (entry.getName().equals("tasks.xml"))
							TaskFactory.getInstance().decodeFromXML(
									zip.getInputStream(entry));
					}
					
					Thread.sleep(1000);
					
					return null;
				} finally {
					SynchronizerUtils.setTaskRepeatEnabled(true);
					
					if (set) {
						try {
							Synchronizing.setSynchronizing(false);
						} catch (SynchronizingException e) {
							
						}
					}
				}
			}
			
			@Override
			protected void done() {
				dialog.dispose();
			}
			
		});
		
		dialog.setVisible(true);
	}
	
}
