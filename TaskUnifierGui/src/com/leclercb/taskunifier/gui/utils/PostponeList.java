package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class PostponeList implements ListChangeSupported, SavePropertiesListener {
	
	private static PostponeList INSTANCE;
	
	public static PostponeList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PostponeList();
		
		return INSTANCE;
	}
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<PostponeItem> items;
	
	public PostponeList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.items = new ArrayList<PostponeItem>();
		
		this.initialize();
		
		Main.getUserSettings().addSavePropertiesListener(
				new WeakSavePropertiesListener(Main.getUserSettings(), this));
	}
	
	private void initialize() {
		for (String status : this.getStatuses()) {
			this.removeStatus(status);
		}
		
		String[] statuses = SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getStatusValues();
		
		if (statuses == null) {
			String value = Main.getSettings().getStringProperty("postponelist");
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
	
	public void add(PostponeItem item) {
		CheckUtils.isNotNull(item);
		
		if (this.statuses.contains(item))
			return;
		
		this.statuses.add(item);
		int index = this.statuses.indexOf(item);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				item);
	}
	
	public void remove(String status) {
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
	
	public class PostponeItem implements PropertyChangeSupported {
		
		public static final String PROP_LABEL = "label";
		public static final String PROP_DATE = "date";
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private String label;
		private Calendar date;
		
		public PostponeItem() {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			Calendar date = Calendar.getInstance();
			DateUtils.removeTime(date);
			date.set(Calendar.MONTH, 0);
			date.set(Calendar.DAY_OF_MONTH, 0);
			
			this.setDate(date);
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public void setLabel(String label) {
			String oldLabel = this.label;
			this.label = label;
			this.propertyChangeSupport.firePropertyChange(
					PROP_LABEL,
					oldLabel,
					label);
		}
		
		public Calendar getDate() {
			return this.date;
		}
		
		public void setDate(Calendar date) {
			Calendar oldDate = this.date;
			this.date = date;
			this.propertyChangeSupport.firePropertyChange(
					PROP_DATE,
					oldDate,
					date);
		}
		
		@Override
		public String toString() {
			if (this.label != null && this.label.length() != 0)
				return this.label;
			
			SimpleDateFormat format = new SimpleDateFormat(
					"mm'm dd'd hh'h mm'm");
			return format.format(this.date.getTime());
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
		
		@Override
		public void removePropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(
					propertyName,
					listener);
		}
		
	}
	
}
