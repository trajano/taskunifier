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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.beans.ModelBean;

public abstract class AbstractModel implements Model {
	
	private transient PropertyChangeSupport propertyChangeSupport;
	
	private ModelId modelId;
	private Map<String, String> modelReferenceIds;
	private ModelStatus modelStatus;
	private Calendar modelCreationDate;
	private Calendar modelUpdateDate;
	private String title;
	private int order;
	private PropertyMap properties;
	
	public AbstractModel(ModelId modelId, String title) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		if (modelId == null)
			modelId = new ModelId();
		
		this.setModelId(modelId);
		this.setModelReferenceIds(new HashMap<String, String>());
		this.setModelStatus(ModelStatus.LOADED);
		this.setModelCreationDate(Calendar.getInstance());
		this.setModelUpdateDate(Calendar.getInstance());
		this.setTitle(title);
		this.setProperties(new PropertyMap());
	}
	
	/**
	 * Returns the factory of the model.
	 * 
	 * @return the factory of the model
	 */
	public abstract AbstractModelFactory<?, ?, ?, ?> getFactory();
	
	/**
	 * Loads the data from the bean into the model.
	 * 
	 * @param bean
	 *            the bean
	 */
	@Override
	public void loadBean(ModelBean bean, boolean loadReferenceIds) {
		CheckUtils.isNotNull(bean);
		
		this.setTitle(bean.getTitle());
		this.setOrder(bean.getOrder());
		this.addProperties(bean.getProperties());
		
		if (loadReferenceIds) {
			this.modelReferenceIds.clear();
			
			if (bean.getModelReferenceIds() != null) {
				Map<String, String> beanReferenceIds = bean.getModelReferenceIds();
				for (String key : beanReferenceIds.keySet()) {
					this.addModelReferenceId(key, beanReferenceIds.get(key));
				}
			}
		}
		
		this.setModelStatus(bean.getModelStatus());
		this.setModelCreationDate(bean.getModelCreationDate());
		this.setModelUpdateDate(bean.getModelUpdateDate());
	}
	
	/**
	 * Convert the data from the model into a bean.
	 * 
	 * @return the bean
	 */
	@Override
	public ModelBean toBean() {
		ModelBean bean = this.getFactory().createBean();
		
		bean.setTitle(this.getTitle());
		bean.setOrder(this.getOrder());
		bean.setProperties(this.getProperties().clone());
		
		bean.setModelId(this.getModelId());
		bean.setModelReferenceIds(this.getModelReferenceIds());
		bean.setModelStatus(this.getModelStatus());
		bean.setModelCreationDate(this.getModelCreationDate());
		bean.setModelUpdateDate(this.getModelUpdateDate());
		
		return bean;
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
	 * @param factory
	 *            the factory for this type of model
	 * @exception IllegalArgumentException
	 *                if the current id is not a new id or if the given id is a
	 *                new id
	 * @exception IllegalArgumentException
	 *                if another model with the same id is found in the factory
	 */
	@Override
	public final void setModelId(ModelId modelId) {
		CheckUtils.isNotNull(modelId);
		
		if (!this.checkBeforeSet(this.getModelId(), modelId))
			return;
		
		Model newModel = this.getFactory().get(modelId);
		
		if (newModel != null && newModel != this)
			throw new IllegalArgumentException(
					"The model id you try to assign to this model already exists in the factory");
		
		ModelId oldModelId = this.modelId;
		this.modelId = modelId;
		this.propertyChangeSupport.firePropertyChange(
				PROP_MODEL_ID,
				oldModelId,
				modelId);
	}
	
	@Override
	public Map<String, String> getModelReferenceIds() {
		return Collections.unmodifiableMap(this.modelReferenceIds);
	}
	
	private void setModelReferenceIds(Map<String, String> modelReferenceIds) {
		CheckUtils.isNotNull(modelReferenceIds);
		this.modelReferenceIds = modelReferenceIds;
	}
	
	@Override
	public String getModelReferenceId(String key) {
		CheckUtils.isNotNull(key);
		return this.modelReferenceIds.get(key);
	}
	
	@Override
	public void addModelReferenceId(String key, String referenceId) {
		CheckUtils.isNotNull(key);
		CheckUtils.isNotNull(referenceId);
		this.modelReferenceIds.put(key, referenceId);
	}
	
	@Override
	public void removeModelReferenceId(String key) {
		CheckUtils.isNotNull(key);
		this.modelReferenceIds.remove(key);
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
	 * Returns the order of the model.
	 * 
	 * @return the order of the model
	 */
	@Override
	public int getOrder() {
		return this.order;
	}
	
	/**
	 * Sets the order of the model. Property name : {@link Model#PROP_ORDER}
	 * 
	 * @param order
	 *            the order of the model
	 */
	@Override
	public void setOrder(int order) {
		if (!this.checkBeforeSet(this.getOrder(), order))
			return;
		
		int oldOrder = this.order;
		this.order = order;
		this.updateProperty(PROP_ORDER, oldOrder, order);
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
					this.getModelType(),
					model.getModelType()).append(
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
	public int compareTo(Model model) {
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
