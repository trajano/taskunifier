package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.IgnoreCaseString;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;

public final class TagList implements PropertyChangeSupported, PropertyChangeListener {
	
	public static final String PROP_TAG_ADDED = "PROP_TAG_ADDED";
	public static final String PROP_TAG_REMOVED = "PROP_TAG_REMOVED";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<IgnoreCaseString> tags;
	
	private TagList() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.tags = new ArrayList<IgnoreCaseString>();
		
		this.initialize();
	}
	
	public String[] getTags() {
		Set<IgnoreCaseString> uniqueTags = new HashSet<IgnoreCaseString>(
				this.tags);
		List<IgnoreCaseString> tags = new ArrayList<IgnoreCaseString>(
				uniqueTags);
		
		// tags = Collections.sort(tags);
		
		return null;
	}
	
	private void initialize() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		
		for (Task task : tasks) {
			if (!task.getModelStatus().equals(ModelStatus.LOADED)
					&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				continue;
			}
			
			this.tags.addAll(Arrays.asList(IgnoreCaseString.as(task.getTags())));
		}
		
		TaskFactory.getInstance().addPropertyChangeListener(
				Task.PROP_TAGS,
				this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		List<IgnoreCaseString> tags = null;
		
		tags = Arrays.asList(IgnoreCaseString.as((String[]) evt.getOldValue()));
		this.tags.removeAll(tags);
		
		for (IgnoreCaseString tag : tags) {
			if (!this.tags.contains(tag))
				this.propertyChangeSupport.firePropertyChange(
						PROP_TAG_REMOVED,
						tag.toString(),
						tag.toString());
		}
		
		tags = Arrays.asList(IgnoreCaseString.as((String[]) evt.getNewValue()));
		
		List<IgnoreCaseString> newTags = new ArrayList<IgnoreCaseString>();
		for (IgnoreCaseString tag : tags) {
			if (!this.tags.contains(tag))
				newTags.add(tag);
		}
		
		this.tags.addAll(tags);
		
		for (IgnoreCaseString tag : newTags) {
			this.propertyChangeSupport.firePropertyChange(
					PROP_TAG_ADDED,
					tag.toString(),
					tag.toString());
		}
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
