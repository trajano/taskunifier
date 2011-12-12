package com.leclercb.taskunifier.gui.api.models.beans;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("com")
public class ComBean {
	
	@XStreamAlias("applicationname")
	private String applicationName;
	
	@XStreamAlias("notes")
	private ComNoteBean[] notes;
	
	@XStreamAlias("tasks")
	private ComTaskBean[] tasks;
	
	@XStreamAlias("quicktasks")
	private ComQuickTaskBean[] quickTasks;
	
	public ComBean() {
		this.setApplicationName(null);
		this.setNotes(null);
		this.setTasks(null);
		this.setQuickTasks(null);
	}
	
	public String getApplicationName() {
		return this.applicationName;
	}
	
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public ComNoteBean[] getNotes() {
		return this.notes;
	}
	
	public void setNotes(ComNoteBean[] notes) {
		this.notes = notes;
	}
	
	public ComTaskBean[] getTasks() {
		return this.tasks;
	}
	
	public void setTasks(ComTaskBean[] tasks) {
		this.tasks = tasks;
	}
	
	public ComQuickTaskBean[] getQuickTasks() {
		return this.quickTasks;
	}
	
	public void setQuickTasks(ComQuickTaskBean[] quickTasks) {
		this.quickTasks = quickTasks;
	}
	
	public static ComBean decodeFromXML(InputStream input) {
		XStream xstream = new XStream(
				new PureJavaReflectionProvider(),
				new DomDriver("UTF-8"));
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.processAnnotations(ComBean.class);
		xstream.alias("note", ComNoteBean.class);
		xstream.alias("task", ComTaskBean.class);
		xstream.alias("quicktask", ComQuickTaskBean.class);
		
		return (ComBean) xstream.fromXML(input);
	}
	
}
