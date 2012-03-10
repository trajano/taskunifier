package com.leclercb.taskunifier.api.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.ModelBeanList;

public class ModelList<M extends Model> implements Cloneable, Serializable, Iterable<M>, ListChangeSupported, PropertyChangeListener {
	
	private ListChangeSupport listChangeSupport;
	
	private List<M> models;
	
	public ModelList() {
		this.listChangeSupport = new ListChangeSupport(this);
		
		this.models = new ArrayList<M>();
	}
	
	@Override
	protected ModelList<M> clone() {
		ModelList<M> list = new ModelList<M>();
		list.models.addAll(this.models);
		return list;
	}
	
	@Override
	public Iterator<M> iterator() {
		return this.models.iterator();
	}
	
	public List<M> getList() {
		return Collections.unmodifiableList(new ArrayList<M>(this.models));
	}
	
	public void add(M model) {
		CheckUtils.isNotNull(model);
		
		if (model.getModelStatus().equals(ModelStatus.TO_DELETE)
				|| model.getModelStatus().equals(ModelStatus.DELETED)) {
			ApiLogger.getLogger().severe("You cannot assign a deleted model");
			return;
		}
		
		if (this.models.contains(model))
			return;
		
		this.models.add(model);
		model.addPropertyChangeListener(this);
		int index = this.models.indexOf(model);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				model);
	}
	
	public void addAll(Collection<M> models) {
		if (models == null)
			return;
		
		for (M model : models)
			this.add(model);
	}
	
	public void addAll(ModelList<M> modelList) {
		this.addAll(modelList.getList());
	}
	
	public void remove(M model) {
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
	
	public void clear() {
		for (M model : this.getList())
			this.remove(model);
	}
	
	public int size() {
		return this.models.size();
	}
	
	public boolean contains(Model model) {
		return this.models.contains(model);
	}
	
	public int getIndexOf(M model) {
		return this.models.indexOf(model);
	}
	
	public M get(int index) {
		return this.models.get(index);
	}
	
	@Override
	public String toString() {
		return StringUtils.join(this.models, ", ");
	}
	
	public ModelBeanList toModelBeanList() {
		ModelBeanList list = new ModelBeanList();
		
		for (M model : this.models)
			list.add(model.getModelId());
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(Model.PROP_MODEL_STATUS)) {
			M model = (M) event.getSource();
			
			if (model.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| model.getModelStatus().equals(ModelStatus.DELETED))
				this.remove(model);
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
	
}
