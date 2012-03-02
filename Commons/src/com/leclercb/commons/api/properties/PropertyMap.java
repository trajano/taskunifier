/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.commons.api.properties;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.ReloadPropertiesListener;
import com.leclercb.commons.api.properties.events.ReloadPropertiesSupport;
import com.leclercb.commons.api.properties.events.ReloadPropertiesSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.SavePropertiesSupport;
import com.leclercb.commons.api.properties.events.SavePropertiesSupported;
import com.leclercb.commons.api.properties.exc.PropertiesException;
import com.leclercb.commons.api.utils.CheckUtils;

public class PropertyMap extends Properties implements PropertyChangeSupported, SavePropertiesSupported, ReloadPropertiesSupported {
	
	private transient PropertyChangeSupport propertyChangeSupport;
	private transient SavePropertiesSupport savePropertiesSupport;
	private transient ReloadPropertiesSupport reloadPropertiesSupport;
	
	private Properties properties;
	private PropertyMap exceptionProperties;
	private transient final Map<Class<?>, PropertiesCoder<?>> coders;
	
	public PropertyMap() {
		this(new Properties(), null);
	}
	
	public PropertyMap(Properties properties) {
		this(properties, null);
	}
	
	public PropertyMap(Properties properties, Properties exceptionProperties) {
		this(properties, (exceptionProperties == null ? null : new PropertyMap(
				exceptionProperties,
				null)));
	}
	
	public PropertyMap(Properties properties, PropertyMap exceptionProperties) {
		CheckUtils.isNotNull(properties);
		
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.savePropertiesSupport = new SavePropertiesSupport();
		this.reloadPropertiesSupport = new ReloadPropertiesSupport();
		
		this.properties = properties;
		this.exceptionProperties = exceptionProperties;
		
		this.coders = new HashMap<Class<?>, PropertiesCoder<?>>();
	}
	
	public void replaceKey(String oldKey, String newKey) {
		String value = this.getStringProperty(oldKey);
		this.setStringProperty(newKey, value);
		this.remove(oldKey);
	}
	
	@Override
	public void addSavePropertiesListener(SavePropertiesListener listener) {
		this.savePropertiesSupport.addSavePropertiesListener(listener);
	}
	
	@Override
	public void removeSavePropertiesListener(SavePropertiesListener listener) {
		this.savePropertiesSupport.removeSavePropertiesListener(listener);
	}
	
	@Override
	public void addReloadPropertiesListener(ReloadPropertiesListener listener) {
		this.reloadPropertiesSupport.addReloadPropertiesListener(listener);
	}
	
	@Override
	public void removeReloadPropertiesListener(ReloadPropertiesListener listener) {
		this.reloadPropertiesSupport.removeReloadPropertiesListener(listener);
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
	
	public <T> void addCoder(PropertiesCoder<T> coder) {
		this.coders.put(coder.getCoderClass(), coder);
	}
	
	public <T> PropertiesCoder<T> removeCoder(Class<T> cls) {
		@SuppressWarnings("unchecked")
		PropertiesCoder<T> coder = (PropertiesCoder<T>) this.coders.remove(cls);
		
		return coder;
	}
	
	public Boolean getBooleanProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getBooleanProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Boolean getBooleanProperty(String key, Boolean def) {
		try {
			Boolean value = this.getBooleanProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Byte getByteProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return Byte.parseByte(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getByteProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Byte getByteProperty(String key, Byte def) {
		try {
			Byte value = this.getByteProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Calendar getCalendarProperty(String key) {
		Long value = this.getLongProperty(key);
		
		if (value == null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(value);
		
		return calendar;
	}
	
	public Calendar getCalendarProperty(String key, Calendar def) {
		try {
			Calendar value = this.getCalendarProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Color getColorProperty(String key) {
		Integer value = this.getIntegerProperty(key);
		
		if (value == null)
			return null;
		
		try {
			return new Color(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getColorProperty(key);
			
			throw new PropertiesException(this.getExceptionMessage(key, value
					+ ""), e);
		}
	}
	
	public Color getColorProperty(String key, Color def) {
		try {
			Color value = this.getColorProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Double getDoubleProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getDoubleProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Double getDoubleProperty(String key, Double def) {
		try {
			Double value = this.getDoubleProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public <T extends Enum<?>> T getEnumProperty(String key, Class<T> enumClass) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		T[] enumConstants = enumClass.getEnumConstants();
		for (T enumConstant : enumConstants)
			if (enumConstant.name().equals(value))
				return enumConstant;
		
		if (this.exceptionProperties != null)
			return this.exceptionProperties.getEnumProperty(key, enumClass);
		
		throw new PropertiesException(this.getExceptionMessage(key, value));
	}
	
	public <T extends Enum<?>> T getEnumProperty(
			String key,
			Class<T> enumClass,
			T def) {
		try {
			T value = this.getEnumProperty(key, enumClass);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Float getFloatProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getFloatProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Float getFloatProperty(String key, Float def) {
		try {
			Float value = this.getFloatProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Integer getIntegerProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getIntegerProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Integer getIntegerProperty(String key, Integer def) {
		try {
			Integer value = this.getIntegerProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Locale getLocaleProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			String[] values = value.split("_");
			
			if (values.length == 1)
				return new Locale(values[0]);
			else
				return new Locale(values[0], values[1]);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getLocaleProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Locale getLocaleProperty(String key, Locale def) {
		try {
			Locale value = this.getLocaleProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public Long getLongProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getLongProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public Long getLongProperty(String key, Long def) {
		try {
			Long value = this.getLongProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public <T> T getObjectProperty(String key, Class<T> cls) {
		String value = this.properties.getProperty(key);
		
		try {
			@SuppressWarnings("unchecked")
			PropertiesCoder<T> coder = (PropertiesCoder<T>) this.coders.get(cls);
			
			if (coder == null)
				throw new PropertiesException("No coder found for class: "
						+ cls);
			
			return coder.decode(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getObjectProperty(key, cls);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public <T> T getObjectProperty(String key, Class<T> cls, T def) {
		try {
			T value = this.getObjectProperty(key, cls);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public SimpleDateFormat getSimpleDateFormatProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		try {
			return new SimpleDateFormat(value);
		} catch (Exception e) {
			if (this.exceptionProperties != null)
				return this.exceptionProperties.getSimpleDateFormatProperty(key);
			
			throw new PropertiesException(
					this.getExceptionMessage(key, value),
					e);
		}
	}
	
	public SimpleDateFormat getSimpleDateFormatProperty(
			String key,
			SimpleDateFormat def) {
		try {
			SimpleDateFormat value = this.getSimpleDateFormatProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	public String getStringProperty(String key) {
		String value = this.properties.getProperty(key);
		
		if (value == null || value.length() == 0)
			return null;
		
		return value;
	}
	
	public String getStringProperty(String key, String def) {
		try {
			String value = this.getStringProperty(key);
			
			if (value == null)
				return def;
			
			return value;
		} catch (PropertiesException e) {
			return def;
		}
	}
	
	private Object setProperty(
			String key,
			String value,
			Object oldValue,
			Object newValue) {
		this.properties.setProperty(key, value);
		
		this.propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(
				this,
				key,
				oldValue,
				newValue));
		
		return oldValue;
	}
	
	public Object setBooleanProperty(String key, Boolean value) {
		Boolean oldValue = this.getBooleanProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value + ""),
				oldValue,
				value);
	}
	
	public Object setByteProperty(String key, Byte value) {
		Byte oldValue = this.getByteProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value + ""),
				oldValue,
				value);
	}
	
	public Object setCalendarProperty(String key, Calendar value) {
		Calendar oldValue = this.getCalendarProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value.getTimeInMillis() + ""),
				oldValue,
				value);
	}
	
	public Object setColorProperty(String key, Color value) {
		Color oldValue = this.getColorProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value.getRGB() + ""),
				oldValue,
				value);
	}
	
	public Object setDoubleProperty(String key, Double value) {
		Double oldValue = this.getDoubleProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value + ""),
				oldValue,
				value);
	}
	
	public Object setEnumProperty(String key, Class<?> enumClass, Enum<?> value) {
		Enum<?> oldValue = this.getEnumProperty(key, value.getClass());
		return this.setProperty(
				key,
				(value == null ? "" : value.name()),
				oldValue,
				value);
	}
	
	public Object setFloatProperty(String key, Float value) {
		Float oldValue = this.getFloatProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value + ""),
				oldValue,
				value);
	}
	
	public Object setIntegerProperty(String key, Integer value) {
		Integer oldValue = this.getIntegerProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value + ""),
				oldValue,
				value);
	}
	
	public Object setLocaleProperty(String key, Locale value) {
		Locale oldValue = this.getLocaleProperty(key);
		return this.setProperty(key, (value == null ? "" : (value.getLanguage()
				+ "_" + value.getCountry())), oldValue, value);
	}
	
	public Object setLongProperty(String key, Long value) {
		Long oldValue = this.getLongProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value + ""),
				oldValue,
				value);
	}
	
	public <T> Object setObjectProperty(String key, Class<T> cls, T value) {
		Object oldValue = this.getObjectProperty(key, cls);
		
		try {
			@SuppressWarnings("unchecked")
			PropertiesCoder<T> coder = (PropertiesCoder<T>) this.coders.get(cls);
			
			if (coder == null)
				throw new PropertiesException("No coder found for class: "
						+ cls);
			
			String encodedValue = coder.encode(value);
			
			return this.setProperty(
					key,
					(encodedValue == null ? "" : encodedValue),
					oldValue,
					value);
		} catch (Exception e) {
			throw new PropertiesException("Cannot encode object of class: "
					+ cls, e);
		}
	}
	
	public Object setSimpleDateFormatProperty(String key, SimpleDateFormat value) {
		SimpleDateFormat oldValue = this.getSimpleDateFormatProperty(key);
		return this.setProperty(
				key,
				(value == null ? "" : value.toPattern()),
				oldValue,
				value);
	}
	
	public Object setStringProperty(String key, String value) {
		return this.setStringProperty(key, value, false);
	}
	
	public Object setStringProperty(String key, String value, boolean force) {
		String oldValue = (force ? null : this.getStringProperty(key));
		return this.setProperty(
				key,
				(value == null ? "" : value),
				oldValue,
				value);
	}
	
	private String getExceptionMessage(String key, String value) {
		return "Property value \""
				+ value
				+ "\" for key \""
				+ key
				+ "\" is invalid";
	}
	
	@Override
	public void clear() {
		this.properties.clear();
	}
	
	@Override
	public PropertyMap clone() {
		Properties properties = null;
		PropertyMap exceptionProperties = null;
		
		properties = (Properties) this.properties.clone();
		
		if (this.exceptionProperties != null)
			exceptionProperties = this.exceptionProperties.clone();
		
		return new PropertyMap(properties, exceptionProperties);
	}
	
	@Override
	public boolean contains(Object value) {
		return this.properties.contains(value);
	}
	
	@Override
	public boolean containsKey(Object key) {
		return this.properties.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return this.properties.containsValue(value);
	}
	
	@Override
	public Enumeration<Object> elements() {
		return this.properties.elements();
	}
	
	@Override
	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return this.properties.entrySet();
	}
	
	@Override
	public boolean equals(Object o) {
		return this.properties.equals(o);
	}
	
	@Override
	public Object get(Object key) {
		return this.properties.get(key);
	}
	
	@Override
	public String getProperty(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}
	
	@Override
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
	
	@Override
	public int hashCode() {
		return this.properties.hashCode();
	}
	
	@Override
	public boolean isEmpty() {
		return this.properties.isEmpty();
	}
	
	@Override
	public Set<Object> keySet() {
		return this.properties.keySet();
	}
	
	@Override
	public Enumeration<Object> keys() {
		return this.properties.keys();
	}
	
	@Override
	public void list(PrintStream out) {
		this.properties.list(out);
	}
	
	@Override
	public void list(PrintWriter out) {
		this.properties.list(out);
	}
	
	@Override
	public void load(InputStream inStream) throws IOException {
		this.properties.load(inStream);
		this.reloadPropertiesSupport.fireReloadPropertiesPerformed();
	}
	
	@Override
	public void load(Reader reader) throws IOException {
		this.properties.load(reader);
		this.reloadPropertiesSupport.fireReloadPropertiesPerformed();
	}
	
	@Override
	public void loadFromXML(InputStream in) throws IOException,
			InvalidPropertiesFormatException {
		this.properties.loadFromXML(in);
		this.reloadPropertiesSupport.fireReloadPropertiesPerformed();
	}
	
	@Override
	public Enumeration<?> propertyNames() {
		return this.properties.propertyNames();
	}
	
	@Override
	public Object put(Object key, Object value) {
		return this.properties.put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends Object, ? extends Object> t) {
		this.properties.putAll(t);
	}
	
	@Override
	public Object remove(Object key) {
		return this.properties.remove(key);
	}
	
	@Override
	@Deprecated
	public void save(OutputStream out, String comments) {
		this.properties.save(out, comments);
	}
	
	@Override
	public synchronized Object setProperty(String key, String value) {
		return this.setStringProperty(key, value);
	}
	
	@Override
	public int size() {
		return this.properties.size();
	}
	
	@Override
	public void store(OutputStream out, String comments) throws IOException {
		this.savePropertiesSupport.fireSavePropertiesPerformed();
		this.properties.store(out, comments);
	}
	
	@Override
	public void store(Writer writer, String comments) throws IOException {
		this.savePropertiesSupport.fireSavePropertiesPerformed();
		this.properties.store(writer, comments);
	}
	
	@Override
	public void storeToXML(OutputStream os, String comment, String encoding)
			throws IOException {
		this.savePropertiesSupport.fireSavePropertiesPerformed();
		this.properties.storeToXML(os, comment, encoding);
	}
	
	@Override
	public void storeToXML(OutputStream os, String comment) throws IOException {
		this.savePropertiesSupport.fireSavePropertiesPerformed();
		this.properties.storeToXML(os, comment);
	}
	
	@Override
	public Set<String> stringPropertyNames() {
		return this.properties.stringPropertyNames();
	}
	
	@Override
	public String toString() {
		return this.properties.toString();
	}
	
	@Override
	public Collection<Object> values() {
		return this.properties.values();
	}
	
}
