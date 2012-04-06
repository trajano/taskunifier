package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.event.ListEventListener;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.glazedlists.ListEventSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TaskSnoozeList.SnoozeItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public final class TaskSnoozeList implements ListEventSupported<SnoozeItem>, SavePropertiesListener {
	
	private static TaskSnoozeList INSTANCE;
	
	public static TaskSnoozeList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskSnoozeList();
		
		return INSTANCE;
	}
	
	@XStreamAlias("snoozelist")
	private EventList<SnoozeItem> items;
	
	public TaskSnoozeList() {
		ObservableElementList.Connector<SnoozeItem> connector = GlazedLists.beanConnector(SnoozeItem.class);
		
		this.items = new ObservableElementList<SnoozeItem>(
				new BasicEventList<SnoozeItem>(),
				connector);
		
		this.initialize();
		
		Main.getUserSettings().addSavePropertiesListener(
				new WeakSavePropertiesListener(Main.getUserSettings(), this));
	}
	
	private void initialize() {
		String input = Main.getSettings().getStringProperty("tasksnoozelist");
		
		if (input == null)
			return;
		
		try {
			String[] items = input.split(";");
			for (String item : items) {
				if (item.length() == 0)
					continue;
				
				String[] values = item.split("\\|");
				this.add(new SnoozeItem(
						Integer.parseInt(values[0]),
						Integer.parseInt(values[1])));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public EventList<SnoozeItem> getEventList() {
		return GlazedLists.readOnlyList(this.items);
	}
	
	public List<SnoozeItem> getSnoozeItems() {
		return new ArrayList<SnoozeItem>(this.items);
	}
	
	public void add(SnoozeItem item) {
		CheckUtils.isNotNull(item);
		
		if (this.items.contains(item))
			return;
		
		this.items.add(item);
	}
	
	public void remove(SnoozeItem item) {
		CheckUtils.isNotNull(item);
		this.items.remove(item);
	}
	
	@Override
	public void addListEventListener(ListEventListener<SnoozeItem> listener) {
		this.items.addListEventListener(listener);
	}
	
	@Override
	public void removeListEventListener(ListEventListener<SnoozeItem> listener) {
		this.items.removeListEventListener(listener);
	}
	
	@Override
	public void saveProperties() {
		StringBuffer value = new StringBuffer();
		for (SnoozeItem item : this.items) {
			value.append(item.getField() + "|" + item.getAmount() + ";");
		}
		
		Main.getSettings().setStringProperty("tasksnoozelist", value.toString());
	}
	
	@XStreamAlias("snoozeitem")
	public static class SnoozeItem implements TimeValue, PropertyChangeSupported {
		
		public static final String PROP_FIELD = "field";
		public static final String PROP_AMOUNT = "amount";
		
		private transient PropertyChangeSupport propertyChangeSupport;
		
		@XStreamAlias("field")
		private int field;
		
		@XStreamAlias("amount")
		private int amount;
		
		public SnoozeItem() {
			this(Calendar.DAY_OF_MONTH, 0);
		}
		
		public SnoozeItem(int field, int amount) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setField(field);
			this.setAmount(amount);
		}
		
		@Override
		public int getField() {
			return this.field;
		}
		
		@Override
		public void setField(int field) {
			if (field != Calendar.MINUTE
					&& field != Calendar.HOUR_OF_DAY
					&& field != Calendar.DAY_OF_MONTH
					&& field != Calendar.WEEK_OF_YEAR
					&& field != Calendar.MONTH
					&& field != Calendar.YEAR)
				throw new IllegalArgumentException();
			
			int oldField = this.field;
			this.field = field;
			this.propertyChangeSupport.firePropertyChange(
					PROP_FIELD,
					oldField,
					field);
		}
		
		@Override
		public int getAmount() {
			return this.amount;
		}
		
		@Override
		public void setAmount(int amount) {
			CheckUtils.isPositive(amount);
			int oldAmount = this.amount;
			this.amount = amount;
			this.propertyChangeSupport.firePropertyChange(
					PROP_AMOUNT,
					oldAmount,
					amount);
		}
		
		@Override
		public String getLabel() {
			if (this.amount == 0 || this.amount == 1) {
				if (this.field == Calendar.MINUTE)
					return Translations.getString("date.1_minute");
				
				if (this.field == Calendar.HOUR_OF_DAY)
					return Translations.getString("date.1_hour");
				
				if (this.field == Calendar.DAY_OF_MONTH)
					return Translations.getString("date.1_day");
				
				if (this.field == Calendar.WEEK_OF_YEAR)
					return Translations.getString("date.1_week");
				
				if (this.field == Calendar.MONTH)
					return Translations.getString("date.1_month");
				
				if (this.field == Calendar.YEAR)
					return Translations.getString("date.1_year");
			} else {
				if (this.field == Calendar.MINUTE)
					return Translations.getString("date.x_minutes", this.amount);
				
				if (this.field == Calendar.HOUR_OF_DAY)
					return Translations.getString("date.x_hours", this.amount);
				
				if (this.field == Calendar.DAY_OF_MONTH)
					return Translations.getString("date.x_days", this.amount);
				
				if (this.field == Calendar.WEEK_OF_YEAR)
					return Translations.getString("date.x_weeks", this.amount);
				
				if (this.field == Calendar.MONTH)
					return Translations.getString("date.x_months", this.amount);
				
				if (this.field == Calendar.YEAR)
					return Translations.getString("date.x_years", this.amount);
			}
			
			return null;
		}
		
		@Override
		public String toString() {
			return this.getLabel();
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
