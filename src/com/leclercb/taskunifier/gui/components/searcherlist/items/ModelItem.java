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
package com.leclercb.taskunifier.gui.components.searcherlist.items;

import java.util.List;

import javax.swing.SortOrder;

import com.explodingpixels.macwidgets.SourceListItem;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.components.searcherlist.TaskSearcherElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.ColorBadgeIcon;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ModelItem extends SourceListItem implements TaskSearcherElement {
	
	private ModelType modelType;
	private Model model;
	
	public ModelItem(ModelType modelType, Model model) {
		super(
				(model == null ? Translations.getString("searcherlist.none") : model.getTitle()));
		
		CheckUtils.isNotNull(modelType, "Model type cannot be null");
		
		if (model != null && model instanceof GuiModel)
			this.setIcon(new ColorBadgeIcon(
					((GuiModel) model).getColor(),
					12,
					12));
		else
			this.setIcon(new ColorBadgeIcon(null, 12, 12));
		
		this.modelType = modelType;
		this.model = model;
	}
	
	public ModelType getModelType() {
		return this.modelType;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void updateBadgeCount() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		int count = 0;
		for (Task task : tasks)
			if (TaskUtils.showTask(task, searcher.getFilter()))
				count++;
		
		this.setCounterValue(count);
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		Template template = new Template("ModelTemplate");
		TaskColumn column = null;
		TaskSearcherType type = null;
		
		switch (this.modelType) {
			case CONTEXT:
				column = TaskColumn.CONTEXT;
				type = TaskSearcherType.CONTEXT;
				if (this.model != null)
					template.setTaskContext(this.model.getModelId());
				break;
			case FOLDER:
				column = TaskColumn.FOLDER;
				type = TaskSearcherType.FOLDER;
				if (this.model != null)
					template.setTaskFolder(this.model.getModelId());
				break;
			case GOAL:
				column = TaskColumn.GOAL;
				type = TaskSearcherType.GOAL;
				if (this.model != null)
					template.setTaskGoal(this.model.getModelId());
				break;
			case LOCATION:
				column = TaskColumn.LOCATION;
				type = TaskSearcherType.LOCATION;
				if (this.model != null)
					template.setTaskLocation(this.model.getModelId());
				break;
		}
		
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
		
		TaskFilter filter = new TaskFilter();
		filter.addElement(new TaskFilterElement(
				column,
				ModelCondition.EQUALS,
				this.model));
		
		if (Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks") != null
				&& !Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks")) {
			filter.addElement(new TaskFilterElement(
					TaskColumn.COMPLETED,
					StringCondition.EQUALS,
					"false"));
			
			template.setTaskCompleted(false);
		}
		
		String title = (this.model == null ? Translations.getString("searcherlist.none") : this.model.getTitle());
		TaskSearcher searcher = new TaskSearcher(
				type,
				title,
				null,
				filter,
				sorter,
				template);
		
		return searcher;
	}
	
}
