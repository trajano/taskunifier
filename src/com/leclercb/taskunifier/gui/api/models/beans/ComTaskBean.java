package com.leclercb.taskunifier.gui.api.models.beans;

import java.io.InputStream;
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
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ComTaskBean extends TaskBean {
	
	@XStreamAlias("foldertitle")
	private String folderTitle;
	
	@XStreamAlias("contexttitle")
	private String contextTitle;
	
	@XStreamAlias("goaltitle")
	private String goalTitle;
	
	@XStreamAlias("locationtitle")
	private String locationTitle;
	
	@XStreamAlias("parenttitle")
	private String parentTitle;
	
	public ComTaskBean() {
		this(null);
	}
	
	public ComTaskBean(ModelId modelId) {
		super(modelId);
		this.setFolderTitle(null);
		this.setContextTitle(null);
		this.setGoalTitle(null);
		this.setLocationTitle(null);
		this.setParentTitle(null);
	}
	
	public String getFolderTitle() {
		return this.folderTitle;
	}
	
	public void setFolderTitle(String folderTitle) {
		this.folderTitle = folderTitle;
	}
	
	public String getContextTitle() {
		return this.contextTitle;
	}
	
	public void setContextTitle(String contextTitle) {
		this.contextTitle = contextTitle;
	}
	
	public String getGoalTitle() {
		return this.goalTitle;
	}
	
	public void setGoalTitle(String goalTitle) {
		this.goalTitle = goalTitle;
	}
	
	public String getLocationTitle() {
		return this.locationTitle;
	}
	
	public void setLocationTitle(String locationTitle) {
		this.locationTitle = locationTitle;
	}
	
	public String getParentTitle() {
		return this.parentTitle;
	}
	
	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}
	
	public void setModels() {
		if (this.getContext() == null) {
			if (this.getContextTitle() != null) {
				List<Context> models = ContextFactory.getInstance().getList();
				for (Context model : models) {
					if (model.getTitle().equalsIgnoreCase(
							this.getContextTitle())) {
						this.setContext(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getFolder() == null) {
			if (this.getFolderTitle() != null) {
				List<Folder> models = FolderFactory.getInstance().getList();
				for (Folder model : models) {
					if (model.getTitle().equalsIgnoreCase(this.getFolderTitle())) {
						this.setFolder(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getGoal() == null) {
			if (this.getGoalTitle() != null) {
				List<Goal> models = GoalFactory.getInstance().getList();
				for (Goal model : models) {
					if (model.getTitle().equalsIgnoreCase(this.getGoalTitle())) {
						this.setGoal(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getLocation() == null) {
			if (this.getLocationTitle() != null) {
				List<Location> models = LocationFactory.getInstance().getList();
				for (Location model : models) {
					if (model.getTitle().equalsIgnoreCase(
							this.getLocationTitle())) {
						this.setLocation(model.getModelId());
						break;
					}
				}
			}
		}
		
		if (this.getParent() == null) {
			if (this.getParentTitle() != null) {
				List<Task> models = TaskFactory.getInstance().getList();
				for (Task model : models) {
					if (model.getTitle().equalsIgnoreCase(this.getParentTitle())) {
						this.setParent(model.getModelId());
						break;
					}
				}
			}
		}
	}
	
	public static ComTaskBean decodeFromXML(InputStream input) {
		XStream xstream = new XStream(
				new PureJavaReflectionProvider(),
				new DomDriver("UTF-8"));
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("task", ComTaskBean.class);
		xstream.processAnnotations(ComTaskBean.class);
		
		return (ComTaskBean) xstream.fromXML(input);
	}
	
}
