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
import java.util.Calendar;

import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.GoalBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;

public class Goal extends AbstractModelParent<Goal> implements PropertyChangeListener {
	
	public static final String PROP_LEVEL = "level";
	public static final String PROP_CONTRIBUTES = "contributes";
	
	private GoalLevel level;
	private Goal contributes;
	
	protected Goal(GoalBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Goal(String title) {
		this(new ModelId(), title);
	}
	
	protected Goal(ModelId modelId, String title) {
		this(modelId, title, GoalLevel.LIFE_TIME);
	}
	
	protected Goal(String title, GoalLevel level) {
		this(new ModelId(), title, level);
	}
	
	protected Goal(ModelId modelId, String title, GoalLevel level) {
		super(modelId, title);
		
		this.setLevel(level);
		this.setContributes(null);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Goal clone(ModelId modelId) {
		Goal goal = this.getFactory().create(modelId, this.getTitle());
		
		goal.setLevel(this.getLevel());
		goal.setContributes(this.getContributes());
		goal.setParent(this.getParent());
		
		// After all other setXxx methods
		goal.setOrder(this.getOrder());
		goal.addProperties(this.getProperties());
		goal.setModelStatus(this.getModelStatus());
		goal.setModelCreationDate(Calendar.getInstance());
		goal.setModelUpdateDate(Calendar.getInstance());
		
		return goal;
	}
	
	@Override
	public GoalFactory<Goal, GoalBean> getFactory() {
		return GoalFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.GOAL;
	}
	
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		CheckUtils.isInstanceOf(b, GoalBean.class);
		
		GoalBean bean = (GoalBean) b;
		
		Goal contributes = null;
		
		if (bean.getContributes() != null) {
			contributes = GoalFactory.getInstance().get(bean.getContributes());
			if (contributes == null)
				contributes = GoalFactory.getInstance().createShell(
						bean.getContributes());
		}
		
		this.setLevel(bean.getLevel());
		this.setContributes(contributes);
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public GoalBean toBean() {
		GoalBean bean = (GoalBean) super.toBean();
		
		bean.setLevel(this.getLevel());
		bean.setContributes(this.getContributes() == null ? null : this.getContributes().getModelId());
		
		return bean;
	}
	
	public GoalLevel getLevel() {
		return this.level;
	}
	
	public void setLevel(GoalLevel level) {
		CheckUtils.isNotNull(level);
		
		if (!this.checkBeforeSet(this.getLevel(), level))
			return;
		
		if (level.equals(GoalLevel.LIFE_TIME))
			this.setContributes(null);
		
		GoalLevel oldLevel = this.level;
		this.level = level;
		this.updateProperty(PROP_LEVEL, oldLevel, level);
	}
	
	public Goal getContributes() {
		return this.contributes;
	}
	
	public void setContributes(Goal contributes) {
		if (!this.checkBeforeSet(this.getContributes(), contributes))
			return;
		
		if (contributes != null) {
			if (contributes.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| contributes.getModelStatus().equals(ModelStatus.DELETED)) {
				ApiLogger.getLogger().severe(
						"You cannot assign a deleted model");
				contributes = null;
			}
		}
		
		if (this.contributes != null)
			this.contributes.removePropertyChangeListener(this);
		
		Goal oldContributes = this.contributes;
		this.contributes = contributes;
		
		if (this.contributes != null)
			this.contributes.addPropertyChangeListener(this);
		
		this.updateProperty(PROP_CONTRIBUTES, oldContributes, contributes);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PROP_LEVEL)) {
			Goal goal = (Goal) event.getSource();
			
			if (!goal.getLevel().equals(GoalLevel.LIFE_TIME))
				this.setContributes(null);
		}
		
		if (event.getPropertyName().equals(PROP_MODEL_STATUS)) {
			Goal goal = (Goal) event.getSource();
			
			if (goal.getModelStatus().equals(ModelStatus.TO_DELETE)
					|| goal.getModelStatus().equals(ModelStatus.DELETED))
				this.setContributes(null);
		}
	}
	
}
