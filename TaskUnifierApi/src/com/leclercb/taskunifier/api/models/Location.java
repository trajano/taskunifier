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
import com.leclercb.taskunifier.api.models.beans.LocationBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;

public class Location extends AbstractModel {
	
	public static final String PROP_DESCRIPTION = "description";
	public static final String PROP_LATITUDE = "latitude";
	public static final String PROP_LONGITUDE = "longitude";
	
	private String description;
	private double latitude;
	private double longitude;
	
	protected Location(LocationBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Location(String title) {
		this(new ModelId(), title);
	}
	
	protected Location(ModelId modelId, String title) {
		super(modelId, title);
		
		this.setDescription(null);
		this.setLatitude(0);
		this.setLongitude(0);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Location clone(ModelId modelId) {
		Location location = this.getFactory().create(modelId, this.getTitle());
		
		location.setDescription(this.getDescription());
		location.setLatitude(this.getLatitude());
		location.setLongitude(this.getLongitude());
		
		// After all other setXxx methods
		location.setOrder(this.getOrder());
		location.addProperties(this.getProperties());
		location.setModelStatus(this.getModelStatus());
		location.setModelCreationDate(Calendar.getInstance());
		location.setModelUpdateDate(Calendar.getInstance());
		
		return location;
	}
	
	@Override
	public LocationFactory<Location, LocationBean> getFactory() {
		return LocationFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.LOCATION;
	}
	
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		CheckUtils.isInstanceOf(b, LocationBean.class);
		
		LocationBean bean = (LocationBean) b;
		
		this.setDescription(bean.getDescription());
		this.setLatitude(bean.getLatitude());
		this.setLongitude(bean.getLongitude());
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public LocationBean toBean() {
		LocationBean bean = (LocationBean) super.toBean();
		
		bean.setDescription(this.getDescription());
		bean.setLatitude(this.getLatitude());
		bean.setLongitude(this.getLongitude());
		
		return bean;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		if (!this.checkBeforeSet(this.getDescription(), description))
			return;
		
		String oldDescription = this.description;
		this.description = description;
		this.updateProperty(PROP_DESCRIPTION, oldDescription, description);
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(double latitude) {
		if (!this.checkBeforeSet(this.getLatitude(), latitude))
			return;
		
		double oldLatitude = this.latitude;
		this.latitude = latitude;
		this.updateProperty(PROP_LATITUDE, oldLatitude, latitude);
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		if (!this.checkBeforeSet(this.getLongitude(), longitude))
			return;
		
		double oldLongitude = this.longitude;
		this.longitude = longitude;
		this.updateProperty(PROP_LONGITUDE, oldLongitude, longitude);
	}
	
	@Override
	public String toDetailedString() {
		StringBuffer buffer = new StringBuffer(super.toDetailedString());
		
		if (this.getDescription() != null)
			buffer.append("Description: " + this.getDescription() + "\n");
		buffer.append("Latitude: " + this.getLatitude() + "\n");
		buffer.append("Longitude: " + this.getLongitude() + "\n");
		
		return buffer.toString();
	}
	
}
