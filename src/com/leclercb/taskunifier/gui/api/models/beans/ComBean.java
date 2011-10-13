package com.leclercb.taskunifier.gui.api.models.beans;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("com")
public class ComBean {
	
	@XStreamAlias("notes")
	private ComNoteBean[] notes;
	
	@XStreamAlias("tasks")
	private ComTaskBean[] tasks;
	
	public ComBean() {
		
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
	
	public static ComBean decodeFromXML(InputStream input) {
		XStream xstream = new XStream(
				new PureJavaReflectionProvider(),
				new DomDriver("UTF-8"));
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.processAnnotations(ComBean.class);
		xstream.alias("note", ComNoteBean.class);
		xstream.alias("task", ComTaskBean.class);
		
		return (ComBean) xstream.fromXML(input);
	}
	
}
