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

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import com.leclercb.taskunifier.api.models.coders.NoteFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiContextFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiFolderFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiGoalFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiLocationFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.models.coders.GuiTaskFactoryXMLCoder;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.WaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ImportModelsDialog extends AbstractImportDialog {
	
	private static ImportModelsDialog INSTANCE;
	
	public static ImportModelsDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ImportModelsDialog();
		
		return INSTANCE;
	}
	
	private ImportModelsDialog() {
		super(
				Translations.getString("general.import_models"),
				true,
				"zip",
				Translations.getString("general.zip_files"));
	}
	
	@Override
	protected void deleteExistingValue() {
		SynchronizerUtils.resetSynchronizerAndDeleteModels();
	}
	
	@Override
	protected void importFromFile(final String file) throws Exception {
		final WaitDialog dialog = new WaitDialog(
				MainFrame.getInstance().getFrame(),
				"general.import_models");
		
		dialog.setRunnable(new Runnable() {
			
			@Override
			public void run() {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					
					@Override
					protected Void doInBackground() throws Exception {
						if (!Synchronizing.setSynchronizing(true)) {
							JOptionPane.showMessageDialog(
									null,
									Translations.getString("general.synchronization_ongoing"),
									Translations.getString("general.error"),
									JOptionPane.ERROR_MESSAGE);
							return null;
						}
						
						dialog.appendToProgressStatus(Translations.getString("general.import_models"));
						
						SynchronizerUtils.setTaskRepeatEnabled(false);
						
						ZipFile zip = new ZipFile(new File(file));
						
						for (Enumeration<?> e = zip.getEntries(); e.hasMoreElements();) {
							ZipArchiveEntry entry = (ZipArchiveEntry) e.nextElement();
							
							if (entry.getName().equals("contexts.xml"))
								new GuiContextFactoryXMLCoder().decode(zip.getInputStream(entry));
							
							if (entry.getName().equals("folders.xml"))
								new GuiFolderFactoryXMLCoder().decode(zip.getInputStream(entry));
							
							if (entry.getName().equals("goals.xml"))
								new GuiGoalFactoryXMLCoder().decode(zip.getInputStream(entry));
							
							if (entry.getName().equals("locations.xml"))
								new GuiLocationFactoryXMLCoder().decode(zip.getInputStream(entry));
							
							if (entry.getName().equals("notes.xml"))
								new NoteFactoryXMLCoder().decode(zip.getInputStream(entry));
							
							if (entry.getName().equals("tasks.xml"))
								new GuiTaskFactoryXMLCoder().decode(zip.getInputStream(entry));
						}
						
						Thread.sleep(1000);
						
						return null;
					}
					
					@Override
					protected void done() {
						SynchronizerUtils.setTaskRepeatEnabled(true);
						Synchronizing.setSynchronizing(false);
						dialog.dispose();
					}
					
				};
				
				worker.execute();
			}
			
		});
		
		dialog.setVisible(true);
	}
	
}
