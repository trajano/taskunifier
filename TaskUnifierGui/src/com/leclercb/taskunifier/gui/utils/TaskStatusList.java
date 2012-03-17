package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class TaskStatusList implements ListChangeSupported, PropertyChangeListener, SavePropertiesListener {
	
	private static TaskStatusList INSTANCE;
	
	public static TaskStatusList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskStatusList();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	
	private List<String> statuses;
	
	public TaskStatusList() {
		this.listChangeSupport = new ListChangeSupport(this);
		
		this.statuses = new ArrayList<String>();
		
		this.initialize();
		
		Main.getUserSettings().addPropertyChangeListener(
				"plugin.synchronizer.id",
				new WeakPropertyChangeListener(Main.getUserSettings(), this));
		
		Main.getUserSettings().addSavePropertiesListener(
				new WeakSavePropertiesListener(Main.getUserSettings(), this));
	}
	
	private void initialize() {
		for (String status : this.getStatuses()) {
			this.removeStatus(status);
		}
		
		String[] statuses = SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getStatusValues();
		
		if (statuses == null) {
			String value = Main.getSettings().getStringProperty("taskstatuses");
			statuses = value.split(";");
		}
		
		for (String status : statuses) {
			this.addStatus(status);
		}
	}
	
	public boolean isEditable() {
		return SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getStatusValues() == null;
	}
	
	public List<String> getStatuses() {
		return new ArrayList<String>(this.statuses);
	}
	
	public void addStatus(String status) {
		CheckUtils.isNotNull(status);
		
		if (this.statuses.contains(status))
			return;
		
		this.statuses.add(status);
		int index = this.statuses.indexOf(status);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				status);
	}
	
	public void removeStatus(String status) {
		CheckUtils.isNotNull(status);
		
		int index = this.statuses.indexOf(status);
		if (this.statuses.remove(status)) {
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					status);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.initialize();
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	@Override
	public void saveProperties() {
		Main.getSettings().setStringProperty(
				"taskstatuses",
				StringUtils.join(this.statuses, ";"));
	}
	
}
