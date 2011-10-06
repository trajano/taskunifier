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
package com.leclercb.taskunifier.gui.constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.logging.Level;

import javax.swing.SortOrder;

import org.apache.commons.io.IOUtils;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.swing.undo.TransferActionListener;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSorterXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorterElement;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public final class Constants {
	
	private Constants() {
		
	}
	
	public static final String TITLE = "TaskUnifier";
	public static final String VERSION = "1.5.1";
	
	public static final int PLUGIN_API_VERSION = 11;
	public static final int WAIT_NO_LICENSE_TIME = 30;
	public static final int WAIT_NO_LICENSE_ADDED_TIME = 15;
	
	public static final String PLUGINS_FILE = "http://www.taskunifier.com/plugins/plugins.xml";
	public static final String VERSION_FILE = "http://taskunifier.sourceforge.net/version.txt";
	public static final String DOWNLOAD_URL = "http://sourceforge.net/projects/taskunifier/files/binaries/";
	public static final String DONATE_URL = "http://sourceforge.net/donate/index.php?group_id=380204";
	public static final String REVIEW_URL = "http://sourceforge.net/projects/taskunifier/reviews/";
	public static final String BUG_URL = "http://sourceforge.net/tracker/?group_id=380204";
	public static final String FEATURE_REQUEST_URL = "http://sourceforge.net/tracker/?group_id=380204";
	public static final String TEST_CONNECTION = "http://www.google.com";
	
	private static NoteSorter DEFAULT_NOTE_SORTER;
	private static NoteSearcher DEFAULT_NOTE_SEARCHER;
	
	private static TaskSorter DEFAULT_TASK_SORTER;
	private static TaskSearcher DEFAULT_TASK_SEARCHER;
	
	public static final ProgressMonitor PROGRESS_MONITOR = new ProgressMonitor();
	
	public static final TransferActionListener TRANSFER_ACTION_LISTENER = new TransferActionListener();
	
	public static NoteSorter getDefaultNoteSorter() {
		return DEFAULT_NOTE_SORTER.clone();
	}
	
	public static NoteSearcher getDefaultNoteSearcher() {
		return DEFAULT_NOTE_SEARCHER.clone();
	}
	
	public static TaskSorter getDefaultTaskSorter() {
		return DEFAULT_TASK_SORTER.clone();
	}
	
	public static TaskSearcher getDefaultTaskSearcher() {
		return DEFAULT_TASK_SEARCHER.clone();
	}
	
	public static void initialize() {
		Main.AFTER_START.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// NOTE
				DEFAULT_NOTE_SORTER = new NoteSorter();
				
				DEFAULT_NOTE_SORTER.addElement(new NoteSorterElement(
						NoteColumn.FOLDER,
						SortOrder.ASCENDING));
				
				DEFAULT_NOTE_SORTER.addElement(new NoteSorterElement(
						NoteColumn.TITLE,
						SortOrder.ASCENDING));
				
				DEFAULT_NOTE_SEARCHER = new NoteSearcher(
						NoteSearcherType.DEFAULT,
						0,
						Translations.getString("searcherlist.general.all"),
						Images.getResourceFile("note.png"),
						new NoteFilter(),
						DEFAULT_NOTE_SORTER.clone());
				
				// TASK
				DEFAULT_TASK_SORTER = new TaskSorter();
				
				DEFAULT_TASK_SORTER.addElement(new TaskSorterElement(
						TaskColumn.DUE_DATE,
						SortOrder.ASCENDING));
				DEFAULT_TASK_SORTER.addElement(new TaskSorterElement(
						TaskColumn.PRIORITY,
						SortOrder.DESCENDING));
				DEFAULT_TASK_SORTER.addElement(new TaskSorterElement(
						TaskColumn.TITLE,
						SortOrder.ASCENDING));
				
				String value = Main.SETTINGS.getStringProperty("searcher.default_sorter");
				
				if (value != null && value.length() != 0) {
					try {
						InputStream input = IOUtils.toInputStream(
								value,
								"UTF-8");
						DEFAULT_TASK_SORTER = new TaskSorterXMLCoder().decode(input);
					} catch (Exception e) {
						GuiLogger.getLogger().log(
								Level.SEVERE,
								"Error while loading default task sorter",
								e);
					}
				}
				
				DEFAULT_TASK_SEARCHER = new TaskSearcher(
						TaskSearcherType.DEFAULT,
						0,
						Translations.getString("searcherlist.general.all"),
						Images.getResourceFile("task.png"),
						new TaskFilter(),
						DEFAULT_TASK_SORTER.clone());
			}
			
		});
	}
	
}
