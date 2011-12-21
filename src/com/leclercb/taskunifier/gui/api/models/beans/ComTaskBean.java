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

public class ComTaskBean extends GuiTaskBean {
	
	@XStreamAlias("comcontacts")
	private ComContactGroupBean comContacts;
	
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
		this((ModelId) null);
	}
	
	public ComTaskBean(ModelId modelId) {
		super(modelId);
		
		this.setComContacts(null);
		this.setFolderTitle(null);
		this.setContextTitle(null);
		this.setGoalTitle(null);
		this.setLocationTitle(null);
		this.setParentTitle(null);
	}
	
	public ComTaskBean(TaskBean bean) {
		super(bean);
		
		if (bean instanceof ComTaskBean) {
			this.setComContacts(((ComTaskBean) bean).getComContacts());
			this.setFolderTitle(((ComTaskBean) bean).getFolderTitle());
			this.setContextTitle(((ComTaskBean) bean).getContextTitle());
			this.setGoalTitle(((ComTaskBean) bean).getGoalTitle());
			this.setLocationTitle(((ComTaskBean) bean).getLocationTitle());
			this.setParentTitle(((ComTaskBean) bean).getParentTitle());
		}
	}
	
	public ComContactGroupBean getComContacts() {
		return this.comContacts;
	}
	
	public void setComContacts(ComContactGroupBean comContacts) {
		this.comContacts = comContacts;
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
	
	public void loadTitles(boolean removeModelId) {
		if (this.getContacts() != null) {
			this.setComContacts(new ComContactGroupBean(this.getContacts()));
			
			if (removeModelId)
				this.setContacts(null);
		}
		
		if (this.getContext() != null) {
			Context context = ContextFactory.getInstance().get(
					this.getContext());
			if (context != null)
				this.setContextTitle(context.getTitle());
			
			if (removeModelId)
				this.setContext(null);
		}
		
		if (this.getFolder() != null) {
			Folder folder = FolderFactory.getInstance().get(this.getFolder());
			if (folder != null)
				this.setFolderTitle(folder.getTitle());
			
			if (removeModelId)
				this.setFolder(null);
		}
		
		if (this.getGoal() != null) {
			Goal goal = GoalFactory.getInstance().get(this.getGoal());
			if (goal != null)
				this.setGoalTitle(goal.getTitle());
			
			if (removeModelId)
				this.setGoal(null);
		}
		
		if (this.getLocation() != null) {
			Location location = LocationFactory.getInstance().get(
					this.getLocation());
			if (location != null)
				this.setLocationTitle(location.getTitle());
			
			if (removeModelId)
				this.setLocation(null);
		}
		
		if (this.getParent() != null) {
			Task parent = TaskFactory.getInstance().get(this.getParent());
			if (parent != null)
				this.setParentTitle(parent.getTitle());
			
			if (removeModelId)
				this.setParent(null);
		}
	}
	
	public void loadModels(boolean removeTitle) {
		if (this.getContacts() == null) {
			if (this.getComContacts() != null) {
				this.setContacts(this.getComContacts().toContactGroupBean());
				
				if (removeTitle)
					this.setComContacts(null);
			}
		}
		
		if (this.getContext() == null) {
			if (this.getContextTitle() != null) {
				List<Context> models = ContextFactory.getInstance().getList();
				for (Context model : models) {
					if (!model.getModelStatus().isEndUserStatus())
						continue;
					
					if (model.getTitle().equalsIgnoreCase(
							this.getContextTitle())) {
						this.setContext(model.getModelId());
						break;
					}
				}
				
				if (removeTitle)
					this.setContextTitle(null);
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
				
				if (removeTitle)
					this.setFolderTitle(null);
			}
		}
		
		if (this.getGoal() == null) {
			if (this.getGoalTitle() != null) {
				List<Goal> models = GoalFactory.getInstance().getList();
				for (Goal model : models) {
					if (!model.getModelStatus().isEndUserStatus())
						continue;
					
					if (model.getTitle().equalsIgnoreCase(this.getGoalTitle())) {
						this.setGoal(model.getModelId());
						break;
					}
				}
				
				if (removeTitle)
					this.setGoalTitle(null);
			}
		}
		
		if (this.getLocation() == null) {
			if (this.getLocationTitle() != null) {
				List<Location> models = LocationFactory.getInstance().getList();
				for (Location model : models) {
					if (!model.getModelStatus().isEndUserStatus())
						continue;
					
					if (model.getTitle().equalsIgnoreCase(
							this.getLocationTitle())) {
						this.setLocation(model.getModelId());
						break;
					}
				}
				
				if (removeTitle)
					this.setLocationTitle(null);
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
				
				if (removeTitle)
					this.setParentTitle(null);
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
