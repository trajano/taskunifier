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
package com.leclercb.taskunifier.gui.utils;

import java.util.List;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;

public final class SettingsUtils {
	
	private SettingsUtils() {
		
	}
	
	public static void resetImportanceColors() {
		Main.getSettings().setStringProperty("theme.color.importance.0", "-1");
		Main.getSettings().setStringProperty("theme.color.importance.1", "-1");
		Main.getSettings().setStringProperty("theme.color.importance.2", "-1");
		Main.getSettings().setStringProperty(
				"theme.color.importance.3",
				"-6684673");
		Main.getSettings().setStringProperty(
				"theme.color.importance.4",
				"-3342337");
		Main.getSettings().setStringProperty(
				"theme.color.importance.5",
				"-6684724");
		Main.getSettings().setStringProperty(
				"theme.color.importance.6",
				"-3342388");
		Main.getSettings().setStringProperty(
				"theme.color.importance.7",
				"-3342439");
		Main.getSettings().setStringProperty("theme.color.importance.8", "-52");
		Main.getSettings().setStringProperty("theme.color.importance.9", "-103");
		Main.getSettings().setStringProperty(
				"theme.color.importance.10",
				"-13159");
		Main.getSettings().setStringProperty(
				"theme.color.importance.11",
				"-13108");
		Main.getSettings().setStringProperty(
				"theme.color.importance.12",
				"-26215");
	}
	
	public static void resetPriorityColors() {
		Main.getSettings().setStringProperty(
				"theme.color.priority.negative",
				"-8355712");
		Main.getSettings().setStringProperty("theme.color.priority.low", "-256");
		Main.getSettings().setStringProperty(
				"theme.color.priority.medium",
				"-16711936");
		Main.getSettings().setStringProperty(
				"theme.color.priority.high",
				"-14336");
		Main.getSettings().setStringProperty(
				"theme.color.priority.top",
				"-65536");
	}
	
	public static void removeNotCompletedCondition() {
		List<TaskSearcher> searchers = TaskSearcherFactory.getInstance().getList();
		for (TaskSearcher searcher : searchers) {
			removeNotCompletedCondition(searcher.getFilter());
		}
	}
	
	private static void removeNotCompletedCondition(TaskFilter filter) {
		List<TaskFilterElement> elements = filter.getElements();
		List<TaskFilter> filters = filter.getFilters();
		
		for (TaskFilterElement e : elements) {
			if (e.getProperty() == TaskColumn.COMPLETED)
				if (e.getValue().equals(Boolean.FALSE))
					filter.removeElement(e);
		}
		
		for (TaskFilter f : filters) {
			removeNotCompletedCondition(f);
		}
	}
	
}
