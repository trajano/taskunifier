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

import java.util.Calendar;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.ContextBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;

public class Context extends AbstractModelParent<Context> {
	
	protected Context(ContextBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Context(String title) {
		this(new ModelId(), title);
	}
	
	protected Context(ModelId modelId, String title) {
		super(modelId, title);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Context clone(ModelId modelId) {
		Context context = this.getFactory().create(modelId, this.getTitle());
		
		context.setParent(this.getParent());
		
		// After all other setXxx methods
		context.setOrder(this.getOrder());
		context.addProperties(this.getProperties());
		context.setModelStatus(this.getModelStatus());
		context.setModelCreationDate(Calendar.getInstance());
		context.setModelUpdateDate(Calendar.getInstance());
		
		return context;
	}
	
	@Override
	public ContextFactory<Context, ContextBean> getFactory() {
		return ContextFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.CONTEXT;
	}
	
	@Override
	public void loadBean(ModelBean bean, boolean loadReferenceIds) {
		CheckUtils.isNotNull(bean);
		CheckUtils.isInstanceOf(bean, ContextBean.class);
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public ContextBean toBean() {
		return (ContextBean) super.toBean();
	}
	
}
