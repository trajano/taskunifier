package com.leclercb.taskunifier.gui.api.models.beans;

import java.io.InputStream;

import com.leclercb.taskunifier.api.models.ModelId;
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
