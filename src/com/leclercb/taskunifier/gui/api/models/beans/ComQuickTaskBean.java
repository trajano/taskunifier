package com.leclercb.taskunifier.gui.api.models.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ComQuickTaskBean {
	
	@XStreamAlias("title")
	private String title;
	
	public ComQuickTaskBean() {
		
	}
	
	public ComQuickTaskBean(String title) {
		this.setTitle(title);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}
