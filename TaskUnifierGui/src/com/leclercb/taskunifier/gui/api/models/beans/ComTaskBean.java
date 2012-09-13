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
package com.leclercb.taskunifier.gui.api.models.beans;

import java.util.ArrayList;
import java.util.List;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.beans.ModelBeanList;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ComTaskBean extends GuiTaskBean {
	
	@XStreamAlias("foldertitle")
	private String folderTitle;
	
	@XStreamAlias("contexttitles")
	@XStreamImplicit(
			itemFieldName = "contexttitle")
	private String[] contextTitles;
	
	@XStreamAlias("goaltitles")
	@XStreamImplicit(
			itemFieldName = "goaltitle")
	private String[] goalTitles;
	
	@XStreamAlias("locationtitles")
	@XStreamImplicit(
			itemFieldName = "locationtitle")
	private String[] locationTitles;
	
	@XStreamAlias("parenttitle")
	private String parentTitle;
	
	public ComTaskBean() {
		this((ModelId) null);
	}
	
	public ComTaskBean(ModelId modelId) {
		super(modelId);
		
		this.setFolderTitle(null);
		this.setContextTitles(null);
		this.setGoalTitles(null);
		this.setLocationTitles(null);
		this.setParentTitle(null);
	}
	
	public ComTaskBean(TaskBean bean) {
		super(bean);
		
		if (bean instanceof ComTaskBean) {
			this.setFolderTitle(((ComTaskBean) bean).getFolderTitle());
			this.setContextTitles(((ComTaskBean) bean).getContextTitles());
			this.setGoalTitles(((ComTaskBean) bean).getGoalTitles());
			this.setLocationTitles(((ComTaskBean) bean).getLocationTitles());
			this.setParentTitle(((ComTaskBean) bean).getParentTitle());
		}
	}
	
	public String getFolderTitle() {
		return this.folderTitle;
	}
	
	public void setFolderTitle(String folderTitle) {
		this.folderTitle = folderTitle;
	}
	
	public String[] getContextTitles() {
		return this.contextTitles;
	}
	
	public void setContextTitles(String[] contextTitles) {
		this.contextTitles = contextTitles;
	}
	
	public String[] getGoalTitles() {
		return this.goalTitles;
	}
	
	public void setGoalTitles(String[] goalTitles) {
		this.goalTitles = goalTitles;
	}
	
	public String[] getLocationTitles() {
		return this.locationTitles;
	}
	
	public void setLocationTitles(String[] locationTitles) {
		this.locationTitles = locationTitles;
	}
	
	public String getParentTitle() {
		return this.parentTitle;
	}
	
	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}
	
	public void loadTitles(boolean removeModelId) {
		if (this.getContexts() != null) {
			List<String> titles = new ArrayList<String>();
			for (ModelId modelId : this.getContexts()) {
				Context context = ContextFactory.getInstance().get(modelId);
				if (context != null)
					titles.add(context.getTitle());
			}
			
			this.setContextTitles(titles.toArray(new String[0]));
		}
		
		if (this.getFolder() != null) {
			Folder folder = FolderFactory.getInstance().get(this.getFolder());
			if (folder != null)
				this.setFolderTitle(folder.getTitle());
		}
		
		if (this.getGoals() != null) {
			List<String> titles = new ArrayList<String>();
			for (ModelId modelId : this.getGoals()) {
				Goal goal = GoalFactory.getInstance().get(modelId);
				if (goal != null)
					titles.add(goal.getTitle());
			}
			
			this.setGoalTitles(titles.toArray(new String[0]));
		}
		
		if (this.getLocations() != null) {
			List<String> titles = new ArrayList<String>();
			for (ModelId modelId : this.getLocations()) {
				Location location = LocationFactory.getInstance().get(modelId);
				if (location != null)
					titles.add(location.getTitle());
			}
			
			this.setLocationTitles(titles.toArray(new String[0]));
		}
		
		if (this.getParent() != null) {
			Task parent = TaskFactory.getInstance().get(this.getParent());
			if (parent != null)
				this.setParentTitle(parent.getTitle());
		}
		
		if (removeModelId) {
			this.setContexts(null);
			this.setFolder(null);
			this.setGoals(null);
			this.setLocations(null);
			this.setParent(null);
		}
	}
	
	public void loadModels(boolean removeTitle) {
		if (this.getContextTitles() != null) {
			if (this.getContexts() == null)
				this.setContexts(new ModelBeanList());
			
			List<Context> models = ContextFactory.getInstance().getList();
			for (Context model : models) {
				if (!model.getModelStatus().isEndUserStatus())
					continue;
				
				for (String title : this.getContextTitles()) {
					if (model.getTitle().equalsIgnoreCase(title)) {
						this.getContexts().add(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getFolder() == null) {
			if (this.getFolderTitle() != null) {
				List<Folder> models = FolderFactory.getInstance().getList();
				for (Folder model : models) {
					if (!model.getModelStatus().isEndUserStatus())
						continue;
					
					if (model.getTitle().equalsIgnoreCase(this.getFolderTitle())) {
						this.setFolder(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getGoalTitles() != null) {
			if (this.getGoals() == null)
				this.setGoals(new ModelBeanList());
			
			List<Goal> models = GoalFactory.getInstance().getList();
			for (Goal model : models) {
				if (!model.getModelStatus().isEndUserStatus())
					continue;
				
				for (String title : this.getGoalTitles()) {
					if (model.getTitle().equalsIgnoreCase(title)) {
						this.getGoals().add(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getLocationTitles() != null) {
			if (this.getLocations() == null)
				this.setLocations(new ModelBeanList());
			
			List<Location> models = LocationFactory.getInstance().getList();
			for (Location model : models) {
				if (!model.getModelStatus().isEndUserStatus())
					continue;
				
				for (String title : this.getLocationTitles()) {
					if (model.getTitle().equalsIgnoreCase(title)) {
						this.getLocations().add(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getParent() == null) {
			if (this.getParentTitle() != null) {
				List<Task> models = TaskFactory.getInstance().getList();
				for (Task model : models) {
					if (!model.getModelStatus().isEndUserStatus())
						continue;
					
					if (model.getTitle().equalsIgnoreCase(this.getParentTitle())) {
						this.setParent(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (removeTitle) {
			this.setContextTitles(null);
			this.setFolderTitle(null);
			this.setGoalTitles(null);
			this.setLocationTitles(null);
			this.setParentTitle(null);
		}
	}
	
}
