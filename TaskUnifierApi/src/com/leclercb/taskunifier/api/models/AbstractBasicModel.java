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
package com.leclercb.taskunifier.api.models;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.beans.converters.CalendarConverter;
import com.leclercb.taskunifier.api.models.beans.converters.PropertyMapConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class AbstractBasicModel implements BasicModel {
	
	@XStreamOmitField
	private transient PropertyChangeSupport propertyChangeSupport;
	
	@XStreamAlias("modelid")
	private ModelId modelId;
	
	@XStreamAlias("modelstatus")
	private ModelStatus modelStatus;
	
	@XStreamAlias("modelcreationdate")
	@XStreamConverter(CalendarConverter.class)
	private Calendar modelCreationDate;
	
	@XStreamAlias("modelupdatedate")
	@XStreamConverter(CalendarConverter.class)
	private Calendar modelUpdateDate;
	
	@XStreamAlias("title")
	private String title;
	
	@XStreamAlias("properties")
	@XStreamConverter(PropertyMapConverter.class)
	private PropertyMap properties;
	
	public AbstractBasicModel(ModelId modelId, String title) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		if (modelId == null)
			modelId = new ModelId();
		
		this.setModelId(modelId);
		this.setModelStatus(ModelStatus.LOADED);
		this.setModelCreationDate(Calendar.getInstance());
		this.setModelUpdateDate(Calendar.getInstance());
		this.setTitle(title);
		this.setProperties(new PropertyMap());
	}
	
	/**
	 * Returns the id of the model.
	 * 
	 * @return the id of the model
	 */
	@Override
	public final ModelId getModelId() {
		return this.modelId;
	}
	
	/**
	 * Sets the id of the model. Property name : {@link Model#PROP_MODEL_ID}
	 * 
	 * @param modelId
	 *            the id of the model
	 */
	private final void setModelId(ModelId modelId) {
		CheckUtils.isNotNull(modelId);
		
		if (!this.checkBeforeSet(this.getModelId(), modelId))
			return;
		
		ModelId oldModelId = this.modelId;
		this.modelId = modelId;
		this.propertyChangeSupport.firePropertyChange(
				PROP_MODEL_ID,
				oldModelId,
				modelId);
	}
	
	/**
	 * Returns the status of the model.
	 * 
	 * @return the status of the model
	 */
	@Override
	public final ModelStatus getModelStatus() {
		return this.modelStatus;
	}
	
	/**
	 * Sets the status of the model. If the status is
	 * {@link ModelStatus#TO_UPDATE} or {@link ModelStatus#TO_DELETE} the last
	 * update date is set to the current date. Property name :
	 * {@link Model#PROP_MODEL_STATUS}
	 * 
	 * @param modelStatus
	 *            the status of the model
	 * @exception IllegalStateException
	 *                if the status of the model is {@link ModelStatus#DELETED}
	 */
	@Override
	public final void setModelStatus(ModelStatus modelStatus) {
		CheckUtils.isNotNull(modelStatus);
		
		if (!this.checkBeforeSet(this.getModelStatus(), modelStatus))
			return;
		
		if (this.modelStatus != null
				&& this.modelStatus.equals(ModelStatus.DELETED)) {
			throw new IllegalStateException(
					"You cannot update the status of a deleted model");
		}
		
		ModelStatus oldModelStatus = this.modelStatus;
		this.modelStatus = modelStatus;
		this.propertyChangeSupport.firePropertyChange(
				PROP_MODEL_STATUS,
				oldModelStatus,
				modelStatus);
		
		if (modelStatus.equals(ModelStatus.TO_UPDATE)
				|| modelStatus.equals(ModelStatus.TO_DELETE))
			this.setModelUpdateDate(Calendar.getInstance());
	}
	
	/**
	 * Returns the creation date of the model.
	 * 
	 * @return the creation date of the model
	 */
	@Override
	public final Calendar getModelCreationDate() {
		return DateUtils.cloneCalendar(this.modelCreationDate);
	}
	
	/**
	 * Sets the creation date of the model. Property name :
	 * {@link Model#PROP_MODEL_CREATION_DATE}
	 * 
	 * @param modelCreationDate
	 *            the creation date of the model
	 */
	@Override
	public final void setModelCreationDate(Calendar modelCreationDate) {
		CheckUtils.isNotNull(modelCreationDate);
		
		if (this.modelCreationDate != null
				&& this.modelCreationDate.compareTo(modelCreationDate) <= 0)
			return;
		
		Calendar oldCreationDate = this.modelCreationDate;
		this.modelCreationDate = DateUtils.cloneCalendar(modelCreationDate);
		this.propertyChangeSupport.firePropertyChange(
				PROP_MODEL_CREATION_DATE,
				oldCreationDate,
				modelCreationDate);
	}
	
	/**
	 * Returns the last update date of the model.
	 * 
	 * @return the last update date of the model
	 */
	@Override
	public final Calendar getModelUpdateDate() {
		return DateUtils.cloneCalendar(this.modelUpdateDate);
	}
	
	/**
	 * Sets the last update date of the model. Property name :
	 * {@link Model#PROP_MODEL_UPDATE_DATE}
	 * 
	 * @param modelUpdateDate
	 *            the last update date of the model
	 */
	@Override
	public final void setModelUpdateDate(Calendar modelUpdateDate) {
		CheckUtils.isNotNull(modelUpdateDate);
		Calendar oldUpdateDate = this.modelUpdateDate;
		this.modelUpdateDate = DateUtils.cloneCalendar(modelUpdateDate);
		this.propertyChangeSupport.firePropertyChange(
				PROP_MODEL_UPDATE_DATE,
				oldUpdateDate,
				modelUpdateDate);
	}
	
	/**
	 * Returns the title of the model.
	 * 
	 * @return the title of the model
	 */
	@Override
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Sets the title of the model. Property name : {@link Model#PROP_TITLE}
	 * 
	 * @param title
	 *            the title of the model
	 */
	@Override
	public void setTitle(String title) {
		CheckUtils.isNotNull(title);
		
		title = title.trim();
		
		if (!this.checkBeforeSet(this.getTitle(), title))
			return;
		
		String oldTitle = this.title;
		this.title = title;
		this.updateProperty(PROP_TITLE, oldTitle, title);
	}
	
	/**
	 * Returns the properties of the model.
	 * 
	 * @return the properties of the model
	 */
	@Override
	public PropertyMap getProperties() {
		return this.properties;
	}
	
	/**
	 * Sets the properties of the model.
	 * 
	 * @param properties
	 *            the properties of the model
	 */
	private void setProperties(PropertyMap properties) {
		CheckUtils.isNotNull(properties);
		this.properties = properties;
	}
	
	public void addProperties(Properties properties) {
		if (properties == null)
			return;
		
		for (Object key : properties.keySet()) {
			this.properties.put(key, properties.get(key));
		}
	}
	
	/**
	 * Performs some checks before a set method. To call at the beginning of
	 * each set method.
	 */
	protected final boolean checkBeforeSet(Object oldValue, Object newValue) {
		if (EqualsUtils.equals(oldValue, newValue))
			return false;
		
		return true;
	}
	
	/**
	 * Performs some actions after a set of a property. To call after each
	 * change of a non-indexed property.
	 * 
	 * @param property
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @param oldValue
	 *            the old value
	 */
	protected final void updateProperty(
			String property,
			Object oldValue,
			Object newValue) {
		this.updateProperty(property, oldValue, newValue, true);
	}
	
	/**
	 * Performs some actions after a set of a property. To call after each
	 * change of a non-indexed property.
	 * 
	 * @param property
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @param oldValue
	 *            the old value
	 * @param updateStatus
	 *            update status or not
	 */
	protected final void updateProperty(
			String property,
			Object oldValue,
			Object newValue,
			boolean updateStatus) {
		if (updateStatus
				&& (this.getModelStatus() == ModelStatus.SHELL || this.getModelStatus() == ModelStatus.LOADED)) {
			if (oldValue == null
					|| newValue == null
					|| !EqualsUtils.equals(oldValue, newValue)) {
				this.setModelStatus(ModelStatus.TO_UPDATE);
			}
		}
		
		this.propertyChangeSupport.firePropertyChange(
				property,
				oldValue,
				newValue);
	}
	
	/**
	 * Performs some actions after a set of a property. To call after each
	 * change of an indexed property.
	 * 
	 * @param property
	 *            the property name
	 * @param index
	 *            the index of the value
	 * @param newValue
	 *            the new value
	 * @param oldValue
	 *            the old value
	 */
	protected final void updateIndexedProperty(
			String property,
			int index,
			Object oldValue,
			Object newValue) {
		this.updateIndexedProperty(property, index, oldValue, newValue, true);
	}
	
	/**
	 * Performs some actions after a set of a property. To call after each
	 * change of an indexed property.
	 * 
	 * @param property
	 *            the property name
	 * @param index
	 *            the index of the value
	 * @param newValue
	 *            the new value
	 * @param oldValue
	 *            the old value
	 * @param updateStatus
	 *            update status or not
	 */
	protected final void updateIndexedProperty(
			String property,
			int index,
			Object oldValue,
			Object newValue,
			boolean updateStatus) {
		if (updateStatus
				&& (this.getModelStatus() == ModelStatus.SHELL || this.getModelStatus() == ModelStatus.LOADED)) {
			if (oldValue == null
					|| newValue == null
					|| !EqualsUtils.equals(oldValue, newValue)) {
				this.setModelStatus(ModelStatus.TO_UPDATE);
			}
		}
		
		this.propertyChangeSupport.fireIndexedPropertyChange(
				property,
				index,
				oldValue,
				newValue);
	}
	
	/**
	 * Check if this object equals the given object. Only the model id is used
	 * to check if two models are equal.
	 * 
	 * @param o
	 *            an object
	 * @return true if this object equals the given object
	 */
	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof Model) {
			Model model = (Model) o;
			
			return new EqualsBuilder().append(
					this.getModelId(),
					model.getModelId()).isEquals();
		}
		
		return false;
	}
	
	/**
	 * Returns the hash code of this model. Only the model id is used to return
	 * the hash code.
	 * 
	 * @return the hash code of this model
	 */
	@Override
	public final int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.modelId);
		
		return hashCode.toHashCode();
	}
	
	@Override
	public int compareTo(BasicModel model) {
		if (model == null)
			return 1;
		
		return this.getModelId().compareTo(model.getModelId());
	}
	
	@Override
	public final String toString() {
		return this.title;
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
