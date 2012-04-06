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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.utils.CheckUtils;

public abstract class AbstractBasicModelFactory<M extends BasicModel> implements BasicModelFactory<M>, PropertyChangeListener {
	
	protected ListChangeSupport listChangeSupport;
	protected PropertyChangeSupport propertyChangeSupport;
	
	private List<M> models;
	
	public AbstractBasicModelFactory() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.models = new ArrayList<M>();
	}
	
	/**
	 * Returns true if a model with the given ID is found in the factory.
	 * 
	 * @param modelId
	 *            ID to check
	 * @return true if a model with the given ID is found or false if it is not
	 *         found
	 */
	@Override
	public boolean contains(ModelId modelId) {
		return (this.get(modelId) != null);
	}
	
	/**
	 * Returns the size of the list.
	 * 
	 * @return the size of the list
	 */
	@Override
	public int size() {
		return this.models.size();
	}
	
	/**
	 * Returns an copy of the list containing all the models.
	 * 
	 * @return an copy of the list containing all the models
	 */
	@Override
	public List<M> getList() {
		return new ArrayList<M>(this.models);
	}
	
	/**
	 * Returns the model at the given index.
	 * 
	 * @param index
	 *            index of the model to retrieve
	 * @return the model at the given index
	 */
	@Override
	public M get(int index) {
		return this.models.get(index);
	}
	
	/**
	 * Returns the model with the given ID.
	 * 
	 * @param modelId
	 *            modelId to find
	 * @return the model with the given modelId or null if no model has been
	 *         found
	 */
	@Override
	public M get(ModelId modelId) {
		for (M model : this.models)
			if (model.getModelId().equals(modelId))
				return model;
		
		return null;
	}
	
	/**
	 * Returns the index of the given model. Returns -1 if the model does not
	 * exist.
	 * 
	 * @param model
	 *            model to find
	 * @return the index of the given model or -1 if no model has been found
	 */
	@Override
	public int getIndexOf(M model) {
		return this.models.indexOf(model);
	}
	
	/**
	 * Changes the model status to {@link ModelStatus#TO_DELETE}.
	 * Returns true if a model with the given ID has been found.
	 * 
	 * @param modelId
	 *            model to mark {@link ModelStatus#TO_DELETE}
	 * @return true if a model with the given ID has been found
	 * @exception IllegalArgumentException
	 *                if no model is found for the given ID
	 */
	@Override
	public boolean markToDelete(ModelId modelId) {
		M model = this.get(modelId);
		
		if (model == null)
			return false;
		
		model.setModelStatus(ModelStatus.TO_DELETE);
		return true;
	}
	
	/**
	 * Changes the model status to {@link ModelStatus#TO_DELETE}.
	 * 
	 * @param model
	 *            model to mark {@link ModelStatus#TO_DELETE}
	 */
	@Override
	public void markToDelete(M model) {
		model.setModelStatus(ModelStatus.TO_DELETE);
	}
	
	/**
	 * Changes the model status to {@link ModelStatus#DELETED}.
	 * Returns true if a model with the given ID has been found.
	 * 
	 * @param modelId
	 *            model to mark {@link ModelStatus#DELETED}
	 * @return true if a model with the given ID has been found
	 * @exception IllegalArgumentException
	 *                if no model is found for the given ID
	 * @see AbstractModelFactory#unregister(Model)
	 */
	@Override
	public boolean markDeleted(ModelId modelId) {
		M model = this.get(modelId);
		
		if (model == null)
			return false;
		
		model.setModelStatus(ModelStatus.DELETED);
		return true;
	}
	
	/**
	 * Changes the model status to {@link ModelStatus#DELETED}.
	 * 
	 * @param model
	 *            model to mark {@link ModelStatus#DELETED}
	 * @see AbstractModelFactory#unregister(Model)
	 */
	@Override
	public void markDeleted(M model) {
		model.setModelStatus(ModelStatus.DELETED);
	}
	
	/**
	 * Registers the model to the factory.
	 * 
	 * @param model
	 *            model to register
	 * @exception IllegalArgumentException
	 *                if a model with the same ID already exists in the factory
	 */
	public void register(M model) {
		CheckUtils.isNotNull(model);
		
		if (this.contains(model.getModelId()))
			throw new IllegalArgumentException("ID already exists in factory");
		
		this.models.add(model);
		model.addPropertyChangeListener(this);
		int index = this.models.indexOf(model);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				model);
	}
	
	/**
	 * Unregisters the model from the factory.
	 * 
	 * @param model
	 *            model to unregister
	 */
	protected void unregister(M model) {
		CheckUtils.isNotNull(model);
		
		int index = this.models.indexOf(model);
		if (this.models.remove(model)) {
			model.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					model);
		}
	}
	
	/**
	 * Changes the model status to {@link ModelStatus#DELETED} of the models
	 * with a new id and status {@link ModelStatus#TO_DELETE}.
	 * Then unregisters the models with status {@link ModelStatus#DELETED}.
	 */
	public void cleanFactory() {
		List<M> models = new ArrayList<M>(this.models);
		for (M model : models)
			if (model.getModelStatus() == ModelStatus.DELETED)
				this.unregister(model);
	}
	
	/**
	 * Deletes all the models from the factory. The method
	 * {@link AbstractModelFactory#markDeleted(Model)} is called on each of the
	 * factory's model.
	 * 
	 * Then the method {@link AbstractModelFactory#cleanFactory()} is called.
	 * 
	 * @see AbstractModelFactory#markDeleted(Model)
	 * @see AbstractModelFactory#cleanFactory()
	 */
	@Override
	public void deleteAll() {
		List<M> models = new ArrayList<M>(this.models);
		for (M model : models)
			this.markDeleted(model);
		
		this.cleanFactory();
	}
	
	/**
	 * The listener will be notified when a new model is added to the factory or
	 * when a model is removed from the factory.
	 * 
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	/**
	 * Removes the listener from the list change listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	/**
	 * The listener will be notified when a model is updated.
	 * 
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * The listener will be notified when a model is updated
	 * for the specified property name.
	 * 
	 * @param propertyName
	 *            the property name to listen to
	 * @param listener
	 *            the listener to notify
	 */
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	/**
	 * Removes the listener from the property change listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	/**
	 * Removes the listener from the property change listener list.
	 * 
	 * @param propertyName
	 *            the property name to remove
	 * @param listener
	 *            listener to remove
	 */
	@Override
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
	/**
	 * Called when a model is updated. Shouldn't be called manually.
	 * 
	 * @param evt
	 *            event of the model
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public abstract M create(String title);
	
	@Override
	public abstract M create(ModelId modelId, String title);
	
	@Override
	public abstract void decodeFromXML(InputStream input);
	
	@Override
	public abstract void encodeToXML(OutputStream output);
	
}
