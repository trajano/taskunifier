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
package com.leclercb.taskunifier.api.models.beans;

import java.util.Calendar;
import java.util.Map;

import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.beans.converters.CalendarConverter;
import com.leclercb.taskunifier.api.models.beans.converters.ModelReferenceIdsConverter;
import com.leclercb.taskunifier.api.models.beans.converters.PropertyMapConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

public abstract class AbstractModelBean implements ModelBean {
	
	@XStreamAlias("modelid")
	private ModelId modelId;
	
	@XStreamAlias("modelreferenceids")
	@XStreamConverter(ModelReferenceIdsConverter.class)
	private Map<String, String> modelReferenceIds;
	
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
	
	@XStreamAlias("order")
	private int order;
	
	@XStreamAlias("properties")
	@XStreamConverter(PropertyMapConverter.class)
	private PropertyMap properties;
	
	public AbstractModelBean() {
		this((ModelId) null);
	}
	
	public AbstractModelBean(ModelId modelId) {
		this.setModelId(modelId);
		this.setModelReferenceIds(null);
		this.setModelStatus(ModelStatus.LOADED);
		this.setModelCreationDate(Calendar.getInstance());
		this.setModelUpdateDate(Calendar.getInstance());
		this.setTitle(null);
		this.setOrder(0);
		this.setProperties(null);
	}
	
	public AbstractModelBean(AbstractModelBean bean) {
		CheckUtils.isNotNull(bean);
		
		this.setModelId(bean.getModelId());
		this.setModelReferenceIds(bean.getModelReferenceIds());
		this.setModelStatus(bean.getModelStatus());
		this.setModelCreationDate(bean.getModelCreationDate());
		this.setModelUpdateDate(bean.getModelUpdateDate());
		this.setTitle(bean.getTitle());
		this.setOrder(bean.getOrder());
		this.setProperties(bean.getProperties());
	}
	
	@Override
	public ModelId getModelId() {
		return this.modelId;
	}
	
	@Override
	public void setModelId(ModelId modelId) {
		this.modelId = modelId;
	}
	
	@Override
	public Map<String, String> getModelReferenceIds() {
		return this.modelReferenceIds;
	}
	
	@Override
	public void setModelReferenceIds(Map<String, String> modelReferenceIds) {
		this.modelReferenceIds = modelReferenceIds;
	}
	
	@Override
	public ModelStatus getModelStatus() {
		return this.modelStatus;
	}
	
	@Override
	public void setModelStatus(ModelStatus modelStatus) {
		this.modelStatus = modelStatus;
	}
	
	@Override
	public Calendar getModelCreationDate() {
		return this.modelCreationDate;
	}
	
	@Override
	public void setModelCreationDate(Calendar modelCreationDate) {
		this.modelCreationDate = modelCreationDate;
	}
	
	@Override
	public Calendar getModelUpdateDate() {
		return this.modelUpdateDate;
	}
	
	@Override
	public void setModelUpdateDate(Calendar modelUpdateDate) {
		this.modelUpdateDate = modelUpdateDate;
	}
	
	@Override
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public int getOrder() {
		return this.order;
	}
	
	@Override
	public void setOrder(int order) {
		this.order = order;
	}
	
	@Override
	public PropertyMap getProperties() {
		return this.properties;
	}
	
	@Override
	public void setProperties(PropertyMap properties) {
		this.properties = properties;
	}
	
}
