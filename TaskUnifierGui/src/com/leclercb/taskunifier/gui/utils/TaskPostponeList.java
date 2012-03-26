package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public final class TaskPostponeList implements ListChangeSupported, PropertyChangeSupported, SavePropertiesListener, PropertyChangeListener {
	
	private static TaskPostponeList INSTANCE;
	
	public static TaskPostponeList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskPostponeList();
		
		return INSTANCE;
	}
	
	private transient ListChangeSupport listChangeSupport;
	private transient PropertyChangeSupport propertyChangeSupport;
	
	@XStreamAlias("postponelist")
	private List<PostponeItem> items;
	
	public TaskPostponeList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.items = new ArrayList<PostponeItem>();
		
		this.initialize();
		
		Main.getUserSettings().addSavePropertiesListener(
				new WeakSavePropertiesListener(Main.getUserSettings(), this));
	}
	
	private void initialize() {
		String input = Main.getSettings().getStringProperty("taskpostponelist");
		
		if (input == null)
			return;
		
		try {
			String[] items = input.split(";");
			for (String item : items) {
				if (item.length() == 0)
					continue;
				
				String[] values = item.split("\\|");
				this.add(new PostponeItem(
						Integer.parseInt(values[0]),
						Integer.parseInt(values[1])));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public List<PostponeItem> getPostponeItems() {
		return new ArrayList<PostponeItem>(this.items);
	}
	
	public void add(PostponeItem item) {
		CheckUtils.isNotNull(item);
		
		if (this.items.contains(item))
			return;
		
		this.items.add(item);
		item.addPropertyChangeListener(this);
		int index = this.items.indexOf(item);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				item);
	}
	
	public void remove(PostponeItem item) {
		CheckUtils.isNotNull(item);
		
		int index = this.items.indexOf(item);
		if (this.items.remove(item)) {
			item.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					item);
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
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.propertyChangeSupport.firePropertyChange(evt);
	}
	
	@Override
	public void saveProperties() {
		StringBuffer value = new StringBuffer();
		for (PostponeItem item : this.items) {
			value.append(item.getField() + "|" + item.getAmount() + ";");
		}
		
		Main.getSettings().setStringProperty(
				"taskpostponelist",
				value.toString());
	}
	
	@XStreamAlias("postponeitem")
	public static class PostponeItem implements TimeValue, PropertyChangeSupported {
		
		public static final String PROP_FIELD = "field";
		public static final String PROP_AMOUNT = "amount";
		
		private transient PropertyChangeSupport propertyChangeSupport;
		
		@XStreamAlias("field")
		private int field;
		
		@XStreamAlias("amount")
		private int amount;
		
		public PostponeItem() {
			this(Calendar.DAY_OF_MONTH, 0);
		}
		
		public PostponeItem(int field, int amount) {
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
		public String toString() {
			if (this.amount == 0)
				return Translations.getString("date.today");
			
			if (this.amount == 1) {
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
