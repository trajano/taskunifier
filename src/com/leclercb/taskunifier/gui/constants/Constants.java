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

import javax.swing.SortOrder;
import javax.swing.undo.UndoableEditSupport;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.gui.swing.undo.TransferActionListener;
import com.leclercb.commons.gui.swing.undo.UndoFireManager;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class Constants {
	
	private Constants() {

	}
	
	public static final String TITLE = "TaskUnifier";
	public static final String VERSION = "0.9.5";
	
	public static final int PLUGIN_API_VERSION = 4;
	public static final int WAIT_NO_LICENSE_TIME = 30;
	
	public static final String PLUGINS_FILE = "http://www.taskunifier.com/plugins/plugins.xml";
	public static final String VERSION_FILE = "http://taskunifier.sourceforge.net/version.txt";
	public static final String DOWNLOAD_URL = "http://sourceforge.net/projects/taskunifier/files/binaries/";
	public static final String DONATE_URL = "http://sourceforge.net/donate/index.php?group_id=380204";
	public static final String REVIEW_URL = "http://sourceforge.net/projects/taskunifier/reviews/";
	public static final String BUG_URL = "http://sourceforge.net/tracker/?group_id=380204";
	public static final String FEATURE_REQUEST_URL = "http://sourceforge.net/tracker/?group_id=380204";
	
	public static final TaskSearcher DEFAULT_SEARCHER;
	
	public static final ProgressMonitor PROGRESS_MONITOR = new ProgressMonitor();
	
	public static final UndoFireManager UNDO_MANAGER = new UndoFireManager();
	public static final UndoableEditSupport UNDO_EDIT_SUPPORT = new UndoableEditSupport();
	
	public static final TransferActionListener TRANSFER_ACTION_LISTENER = new TransferActionListener();
	
	static {
		UNDO_EDIT_SUPPORT.addUndoableEditListener(UNDO_MANAGER);
		
		TaskSorter sorter = new TaskSorter();
		
		sorter.addElement(new TaskSorterElement(
				1,
				TaskColumn.DUE_DATE,
				SortOrder.ASCENDING));
		sorter.addElement(new TaskSorterElement(
				2,
				TaskColumn.PRIORITY,
				SortOrder.DESCENDING));
		sorter.addElement(new TaskSorterElement(
				3,
				TaskColumn.TITLE,
				SortOrder.ASCENDING));
		
		DEFAULT_SEARCHER = new TaskSearcher(
				TaskSearcherType.DEFAULT,
				0,
				Translations.getString("searcherlist.general.all_tasks"),
				Images.getResourceFile("document.png"),
				new TaskFilter(),
				sorter.clone());
	}
	
}
