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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.ModelType;

public interface ModelBean extends Serializable {
	
	public abstract ModelType getModelType();
	
	public abstract ModelId getModelId();
	
	public abstract void setModelId(ModelId id);
	
	public abstract Map<String, String> getModelReferenceIds();
	
	public abstract void setModelReferenceIds(
			Map<String, String> modelReferenceIds);
	
	public abstract ModelStatus getModelStatus();
	
	public abstract void setModelStatus(ModelStatus status);
	
	public abstract Calendar getModelCreationDate();
	
	public abstract void setModelCreationDate(Calendar creationDate);
	
	public abstract Calendar getModelUpdateDate();
	
	public abstract void setModelUpdateDate(Calendar updateDate);
	
	public abstract String getTitle();
	
	public abstract void setTitle(String title);
	
	public abstract int getOrder();
	
	public abstract void setOrder(int order);
	
	public abstract PropertyMap getProperties();
	
	public abstract void setProperties(PropertyMap properties);
	
}
