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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.ModelParentBean;

public abstract class AbstractModelParent<M extends AbstractModelParent<M>> extends AbstractModel implements ModelParent<M> {
	
	private ParentPropertyChangeListener parentPropertyChangeListener;
	
	private M parent;
	private List<M> children;
	
	public AbstractModelParent(ModelId modelId, String title) {
		super(modelId, title);
		
		this.parentPropertyChangeListener = new ParentPropertyChangeListener();
		
		this.children = new ArrayList<M>();
		
		this.setParent(null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		
		ModelParentBean bean = (ModelParentBean) b;
		
		M parent = null;
		
		if (bean.getParent() != null) {
			parent = (M) this.getFactory().get(bean.getParent());
			if (parent == null)
				parent = (M) this.getFactory().createShell(bean.getParent());
		}
		
		this.setParent(parent);
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public ModelBean toBean() {
		ModelParentBean bean = (ModelParentBean) super.toBean();
		
		bean.setParent(this.getParent() == null ? null : this.getParent().getModelId());
		
		return bean;
	}
	
	@Override
	public M getParent() {
		return this.parent;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setParent(M parent) {
		if (!this.checkBeforeSet(this.getParent(), parent))
			return;
		
		if (parent != null) {
			if (parent.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| parent.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				parent = null;
			}
		}
		
		if (parent != null && this.getModelId().equals(parent.getModelId())) {
			ApiLogger.getLogger().severe(
					"Current model cannot be its own parent");
			parent = null;
		}
		
		if (parent != null) {
			for (M p : parent.getAllParents()) {
				if (this.equals(p)) {
					ApiLogger.getLogger().severe(
							"Parent is a child of current model");
					parent = null;
				}
			}
		}
		
		for (M c : this.getAllChildren()) {
			if (this.equals(c)) {
				ApiLogger.getLogger().severe(
						"Current model cannot be a child of one of its children");
				parent = null;
			}
		}
		
		if (this.parent != null) {
			this.parent.removeChild((M) this);
			this.parent.removePropertyChangeListener(this.parentPropertyChangeListener);
		}
		
		M oldParent = this.parent;
		this.parent = parent;
		
		if (this.parent != null) {
			this.parent.addChild((M) this);
			this.parent.addPropertyChangeListener(this.parentPropertyChangeListener);
		}
		
		this.updateProperty(PROP_PARENT, oldParent, parent);
	}
	
	@Override
	public M getRoot() {
		if (this.getParent() == null)
			return null;
		
		M parent = this.getParent();
		while (true) {
			if (parent.getParent() == null)
				break;
			
			parent = parent.getParent();
		}
		
		return parent;
	}
	
	@Override
	public List<M> getAllParents() {
		List<M> parents = new ArrayList<M>();
		M parent = this.getParent();
		while (parent != null) {
			parents.add(parent);
			parent = parent.getParent();
		}
		
		return Collections.unmodifiableList(parents);
	}
	
	@Override
	public List<M> getAllChildren() {
		List<M> children = new ArrayList<M>(this.children);
		for (M child : this.children) {
			children.addAll(child.getAllChildren());
		}
		
		return Collections.unmodifiableList(children);
	}
	
	@Override
	public List<M> getChildren() {
		return Collections.unmodifiableList(this.children);
	}
	
	private void addChild(M child) {
		CheckUtils.isNotNull(child);
		
		if (this.children.contains(child))
			return;
		
		this.children.add(child);
	}
	
	private void removeChild(M child) {
		this.children.remove(child);
	}
	
	private class ParentPropertyChangeListener implements PropertyChangeListener {
		
		@SuppressWarnings("unchecked")
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals(PROP_MODEL_STATUS)) {
				M model = (M) event.getSource();
				
				if (model.getModelStatus().equals(ModelStatus.TO_DELETE)
						|| model.getModelStatus().equals(ModelStatus.DELETED))
					AbstractModelParent.this.setParent(null);
			}
		}
		
	}
	
}
