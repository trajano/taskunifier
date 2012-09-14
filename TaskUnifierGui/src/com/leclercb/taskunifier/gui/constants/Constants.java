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
import com.leclercb.taskunifier.gui.api.searchers.coders.NoteSearcherXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorterElement;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.synchronize.progress.GrowlSynchronizerProgressMessageListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.threads.communicator.progress.GrowlCommunicatorProgressMessageListener;
import com.leclercb.taskunifier.gui.threads.reminder.progress.GrowlReminderProgressMessageListener;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public final class Constants {
	
	private Constants() {
		
	}
	
	public static final String TITLE = "TaskUnifier";
	public static final String VERSION = "3.0.0";
	public static final boolean BETA = true;
	public static final String DEFAULT_SUFFIX = "_v3";
	
	public static final int PLUGIN_API_VERSION = 30;
	public static final int WAIT_NO_LICENSE_TIME = 30;
	public static final int WAIT_NO_LICENSE_ADDED_TIME = 15;
	
	public static final String PLUGINS_FILE = "http://www.taskunifier.com/plugins/plugins.xml";
	public static final String VERSION_FILE = "http://www.taskunifier.com/version.txt";
	public static final String DOWNLOAD_URL = "http://www.taskunifier.com/index.php?page=download";
	public static final String DONATE_URL = "http://sourceforge.net/donate/index.php?group_id=380204";
	public static final String REVIEW_URL = "http://sourceforge.net/projects/taskunifier/reviews/";
	public static final String BUG_URL = "http://sourceforge.net/tracker/?group_id=380204";
	public static final String FEATURE_REQUEST_URL = "http://sourceforge.net/tracker/?group_id=380204";
	public static final String TEST_CONNECTION = "http://www.google.com";
	
	private static NoteSearcher DEFAULT_NOTE_SEARCHER;
	private static NoteSearcher MAIN_NOTE_SEARCHER;
	
	private static TaskSearcher DEFAULT_TASK_SEARCHER;
	private static TaskSearcher MAIN_TASK_SEARCHER;
	
	public static final ProgressMonitor PROGRESS_MONITOR = new ProgressMonitor();
	
	public static final TransferActionListener TRANSFER_ACTION_LISTENER = new TransferActionListener();
	
	public static final UndoSupport UNDO_SUPPORT = new UndoSupport();
	
	public static NoteSearcher getDefaultNoteSearcher() {
		return DEFAULT_NOTE_SEARCHER.clone();
	}
	
	public static NoteSearcher getMainNoteSearcher() {
		return MAIN_NOTE_SEARCHER.clone();
	}
	
	public static TaskSearcher getDefaultTaskSearcher() {
		return DEFAULT_TASK_SEARCHER.clone();
	}
	
	public static TaskSearcher getMainTaskSearcher() {
		return MAIN_TASK_SEARCHER.clone();
	}
	
	public static void initialize() {
		// Growl Listeners
		Constants.PROGRESS_MONITOR.addListChangeListener(new GrowlCommunicatorProgressMessageListener());
		Constants.PROGRESS_MONITOR.addListChangeListener(new GrowlSynchronizerProgressMessageListener());
		Constants.PROGRESS_MONITOR.addListChangeListener(new GrowlReminderProgressMessageListener());
		
		// Initialize Searchers
		initializeNoteSearcher();
		initializeTaskSearcher();
	}
	
	private static void initializeNoteSearcher() {
		// Default Note Sorter
		NoteSorter defaultNoteSorter = new NoteSorter();
		
		defaultNoteSorter.addElement(new NoteSorterElement(
				NoteColumn.FOLDER,
				SortOrder.ASCENDING));
		
		defaultNoteSorter.addElement(new NoteSorterElement(
				NoteColumn.TITLE,
				SortOrder.ASCENDING));
		
		// Default Note Filter
		NoteFilter defaultNoteFilter = new NoteFilter();
		
		// Default Note Searcher
		DEFAULT_NOTE_SEARCHER = new NoteSearcher(
				NoteSearcherType.DEFAULT,
				0,
				"",
				defaultNoteFilter,
				defaultNoteSorter);
		
		String value = Main.getSettings().getStringProperty(
				"notesearcher.default_searcher");
		
		if (value != null && value.length() != 0) {
			try {
				InputStream input = IOUtils.toInputStream(value, "UTF-8");
				DEFAULT_NOTE_SEARCHER = new NoteSearcherXMLCoder().decode(input);
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						"Error while loading default note sorter",
						e);
			}
		}
		
		// Main Note Searcher
		MAIN_NOTE_SEARCHER = new NoteSearcher(
				NoteSearcherType.DEFAULT,
				0,
				Translations.getString("searcherlist.general.all"),
				ImageUtils.getResourceFile("note.png"),
				DEFAULT_NOTE_SEARCHER.getFilter().clone(),
				DEFAULT_NOTE_SEARCHER.getSorter().clone());
	}
	
	private static void initializeTaskSearcher() {
		// Default Task Sorter
		TaskSorter defaultTaskSorter = new TaskSorter();
		
		defaultTaskSorter.addElement(new TaskSorterElement(
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		defaultTaskSorter.addElement(new TaskSorterElement(
				TaskColumn.PRIORITY,
				SortOrder.DESCENDING));
		defaultTaskSorter.addElement(new TaskSorterElement(
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		// Default Task Filter
		TaskFilter defaultTaskFilter = new TaskFilter();
		
		// Default Task Searcher
		DEFAULT_TASK_SEARCHER = new TaskSearcher(
				TaskSearcherType.DEFAULT,
				0,
				"",
				defaultTaskFilter,
				defaultTaskSorter);
		
		String value = Main.getSettings().getStringProperty(
				"tasksearcher.default_searcher");
		
		if (value != null && value.length() != 0) {
			try {
				InputStream input = IOUtils.toInputStream(value, "UTF-8");
				DEFAULT_TASK_SEARCHER = new TaskSearcherXMLCoder().decode(input);
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						"Error while loading default task sorter",
						e);
			}
		}
		
		// Main Task Searcher
		MAIN_TASK_SEARCHER = new TaskSearcher(
				TaskSearcherType.DEFAULT,
				0,
				Translations.getString("searcherlist.general.all"),
				ImageUtils.getResourceFile("task.png"),
				DEFAULT_TASK_SEARCHER.getFilter().clone(),
				DEFAULT_TASK_SEARCHER.getSorter().clone());
	}
	
}
