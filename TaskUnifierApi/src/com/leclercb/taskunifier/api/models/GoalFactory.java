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

import java.util.logging.Level;

import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.taskunifier.api.models.beans.GoalBean;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;

public class GoalFactory<G extends Goal, GB extends GoalBean> extends AbstractModelFactory<Goal, GoalBean, G, GB> {
	
	private static GoalFactory<Goal, GoalBean> FACTORY;
	
	@SuppressWarnings("unchecked")
	public static <G extends Goal, GB extends GoalBean> void initializeWithClass(
			Class<G> modelClass,
			Class<GB> modelBeanClass) {
		if (FACTORY == null) {
			FACTORY = (GoalFactory<Goal, GoalBean>) new GoalFactory<G, GB>(
					modelClass,
					modelBeanClass);
		}
	}
	
	public static GoalFactory<Goal, GoalBean> getInstance() {
		if (FACTORY == null)
			FACTORY = new GoalFactory<Goal, GoalBean>(
					Goal.class,
					GoalBean.class);
		
		return FACTORY;
	}
	
	private GoalFactory(Class<G> modelClass, Class<GB> modelBeanClass) {
		super(Goal.class, GoalBean.class, modelClass, modelBeanClass);
	}
	
	public synchronized G create(String title, GoalLevel level) {
		try {
			G goal = this.getModelClass().getDeclaredConstructor(
					String.class,
					GoalLevel.class).newInstance(title, level);
			this.register(goal);
			return goal;
		} catch (Exception e) {
			ApiLogger.getLogger().log(Level.SEVERE, "Cannot instantiate", e);
			return null;
		}
	}
	
	public synchronized G create(ModelId modelId, String title, GoalLevel level) {
		try {
			G goal = this.getModelClass().getDeclaredConstructor(
					ModelId.class,
					String.class,
					GoalLevel.class).newInstance(modelId, title, level);
			this.register(goal);
			return goal;
		} catch (Exception e) {
			ApiLogger.getLogger().log(Level.SEVERE, "Cannot instantiate", e);
			return null;
		}
	}
	
	@Override
	protected String getModelNodeName() {
		return "goal";
	}
	
	@Override
	protected String getModelListNodeName() {
		return "goals";
	}
	
}
