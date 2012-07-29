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
package com.leclercb.taskunifier.gui.components.tasksearchertree.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUColorBadgeIcon;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ModelItem extends DefaultMutableTreeNode implements SearcherNode {
	
	private ModelType modelType;
	private TaskSearcher searcher;
	private BadgeCount badgeCount;
	
	public ModelItem(ModelType modelType, Model model) {
		super(model);
		
		CheckUtils.isNotNull(modelType);
		this.modelType = modelType;
		
		this.initializeTaskSearcher();
		this.updateBadgeCount();
	}
	
	public ModelType getModelType() {
		return this.modelType;
	}
	
	public Model getModel() {
		return (Model) this.getUserObject();
	}
	
	private void initializeTaskSearcher() {
		final Model model = this.getModel();
		final TaskTemplate template = new TaskTemplate("ModelTemplate");
		TaskColumn column = null;
		TaskSearcherType type = null;
		
		switch (this.modelType) {
			case CONTEXT:
				column = TaskColumn.CONTEXTS;
				type = TaskSearcherType.CONTEXT;
				
				ModelList<Context> contexts = new ModelList<Context>();
				
				if (model != null)
					contexts.add((Context) model);
				
				template.setTaskContexts(contexts);
				
				break;
			case FOLDER:
				column = TaskColumn.FOLDER;
				type = TaskSearcherType.FOLDER;
				
				template.setTaskFolder((Folder) model);
				
				break;
			case GOAL:
				column = TaskColumn.GOALS;
				type = TaskSearcherType.GOAL;
				
				ModelList<Goal> goals = new ModelList<Goal>();
				
				if (model != null)
					goals.add((Goal) model);
				
				template.setTaskGoals(goals);
				
				break;
			case LOCATION:
				column = TaskColumn.LOCATIONS;
				type = TaskSearcherType.LOCATION;
				
				ModelList<Location> locations = new ModelList<Location>();
				
				if (model != null)
					locations.add((Location) model);
				
				template.setTaskLocations(locations);
				
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		TaskSearcher defaultTaskSearcher = Constants.getDefaultTaskSearcher();
		
		TaskFilter filter = new TaskFilter();
		filter.setLink(FilterLink.AND);
		filter.addElement(new TaskFilterElement(
				column,
				ModelCondition.EQUALS,
				this.getModel()));
		filter.addFilter(defaultTaskSearcher.getFilter());
		
		String title = Translations.getString("searcherlist.none");
		
		if (model != null)
			title = model.getTitle();
		
		this.searcher = new TaskSearcher(
				type,
				0,
				title,
				null,
				filter,
				defaultTaskSearcher.getSorter(),
				template);
		
		if (model != null) {
			model.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					if (event.getPropertyName().equals(BasicModel.PROP_TITLE)) {
						ModelItem.this.searcher.setTitle(model.getTitle());
						return;
					}
				}
				
			});
		}
	}
	
	@Override
	public TaskSearcher getTaskSearcher() {
		return this.searcher;
	}
	
	@Override
	public Icon getIcon() {
		if (this.getModel() != null && this.getModel() instanceof GuiModel)
			return new TUColorBadgeIcon(
					((GuiModel) this.getModel()).getColor(),
					12,
					12);
		else
			return new TUColorBadgeIcon(null, 12, 12);
	}
	
	@Override
	public String getText() {
		return (this.getModel() == null ? Translations.getString("searcherlist.none") : this.getModel().getTitle());
	}
	
	@Override
	public void updateBadgeCount() {
		if (!Main.getSettings().getBooleanProperty("tasksearcher.show_badges")) {
			this.badgeCount = null;
			return;
		}
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		TaskSearcher searcher = this.getTaskSearcher();
		
		boolean useDueTime = Main.getSettings().getBooleanProperty(
				"date.use_due_time");
		
		int count = 0;
		int countOverdue = 0;
		for (Task task : tasks) {
			if (TaskUtils.badgeTask(task, searcher.getFilter())) {
				count++;
				
				if (!task.isCompleted() && task.isOverDue(!useDueTime))
					countOverdue++;
			}
		}
		
		this.badgeCount = new BadgeCount(count, countOverdue);
	}
	
	@Override
	public BadgeCount getBadgeCount() {
		return this.badgeCount;
	}
	
	@Override
	public boolean getAllowsChildren() {
		return true;
	}
	
}
