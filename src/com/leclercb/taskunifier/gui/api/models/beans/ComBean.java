package com.leclercb.taskunifier.gui.api.models.beans;

import java.io.InputStream;
import java.io.OutputStream;

import com.leclercb.taskunifier.api.models.templates.NoteTemplateFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.api.models.beans.converters.ComNoteBeanWithTemplateConverter;
import com.leclercb.taskunifier.gui.api.models.beans.converters.ComTaskBeanWithTemplateConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias("com")
public class ComBean {
	
	@XStreamAlias("applicationname")
	private String applicationName;
	
	@XStreamAlias("arguments")
	private String[] arguments;
	
	@XStreamAlias("notes")
	private ComNoteBean[] notes;
	
	@XStreamAlias("tasks")
	private ComTaskBean[] tasks;
	
	@XStreamAlias("quicktasks")
	private ComQuickTaskBean[] quickTasks;
	
	public ComBean() {
		this.setApplicationName(null);
		this.setArguments(null);
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
	
	public String[] getArguments() {
		return this.arguments;
	}
	
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
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
	
	public void encodeToXML(OutputStream output) {
		encodeToXML(output, this);
	}
	
	public static void encodeToXML(OutputStream output, ComBean bean) {
		XStream xstream = new XStream(
				new PureJavaReflectionProvider(),
				new DomDriver("UTF-8"));
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.processAnnotations(ComBean.class);
		xstream.alias("note", ComNoteBean.class);
		xstream.alias("task", ComTaskBean.class);
		xstream.alias("quicktask", ComQuickTaskBean.class);
		
		xstream.registerConverter(new ComNoteBeanWithTemplateConverter(
				xstream.getMapper(),
				xstream.getReflectionProvider(),
				NoteTemplateFactory.getInstance().getDefaultTemplate()));
		
		xstream.registerConverter(new ComTaskBeanWithTemplateConverter(
				xstream.getMapper(),
				xstream.getReflectionProvider(),
				TaskTemplateFactory.getInstance().getDefaultTemplate()));
		
		xstream.toXML(bean, output);
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
		
		xstream.registerConverter(new ComNoteBeanWithTemplateConverter(
				xstream.getMapper(),
				xstream.getReflectionProvider(),
				NoteTemplateFactory.getInstance().getDefaultTemplate()));
		
		xstream.registerConverter(new ComTaskBeanWithTemplateConverter(
				xstream.getMapper(),
				xstream.getReflectionProvider(),
				TaskTemplateFactory.getInstance().getDefaultTemplate()));
		
		return (ComBean) xstream.fromXML(input);
	}
	
}
