package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class TaskStatusList implements PropertyChangeListener, SavePropertiesListener {
	
	private static TaskStatusList INSTANCE;
	
	public static TaskStatusList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskStatusList();
		
		return INSTANCE;
	}
	
	private EventList<String> statuses;
	
	public TaskStatusList() {
		this.statuses = new BasicEventList<String>();
		
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
	
	public EventList<String> getEventList() {
		return GlazedLists.readOnlyList(this.statuses);
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
	}
	
	public void removeStatus(String status) {
		CheckUtils.isNotNull(status);
		this.statuses.remove(status);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.initialize();
	}
	
	@Override
	public void saveProperties() {
		Main.getSettings().setStringProperty(
				"taskstatuses",
				StringUtils.join(this.statuses, ";"));
	}
	
}
